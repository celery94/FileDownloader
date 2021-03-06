package com.celery.filedownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileItem {

    public static final int STATUS_ERROR = -1;
    public static final int STATUS_STARTED = 0;
    public static final int STATUS_COMPLETE = 1;
    public static final int STATUS_PENDING = 2;
    public static final int STATUS_REMOVED = 3;

    public static final int URL_SIZE_UNKNOWN = -1;
    public static final int URL_ERROR = -2;

    private String fileName;
    private String fileExtension;
    private String fileNameWithoutExtension;
    private long fileSize;
    private long fileSizeDownload;
    private int status;
    private long lastModifyTimeStamp;
    private String urlString;
    private int position;

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        if (fileSize == URL_SIZE_UNKNOWN) {
            return "Unknown";
        } else if (fileSize == URL_ERROR || fileSize == 0) {    //means url incorrect
            return String.valueOf(URL_ERROR);
        } else {
            return formatSize(fileSize);
        }
    }

    public long getFileLength(){
        return fileSize;
    }

    public String getFileSizeDownload() {
        if (fileSize == URL_SIZE_UNKNOWN) {
            return formatSize(fileSizeDownload);
        } else if (fileSize == fileSizeDownload) {
            return formatSize(fileSize);
        } else {
            return formatSize(fileSize) + "/" + formatSize(fileSizeDownload);
        }
    }

    private String formatSize(long size) {
        long kb = size / 1024;
        if (kb == 0) {
            return String.valueOf(size) + "b";
        } else {
            long mb = kb / 1024;
            if (mb == 0) {
                return String.valueOf(kb) + "K";
            } else {
                return String.valueOf(mb) + "M " + String.valueOf(kb % 1024) + "K";
            }
        }
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusStr() {
        switch (status) {
            case STATUS_ERROR:
                return "Error";
            case STATUS_STARTED:
                return "Started";
            case STATUS_COMPLETE:
                return "Completed";
            case STATUS_PENDING:
                return "Pending";
            case STATUS_REMOVED:
                return "Removed";
            default:
                return "";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastModifyTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String string = dateFormat.format(new Date(lastModifyTimeStamp));

        return string;
    }

    private URL url;
    private boolean isValid;

    public FileItem(String urlString) {
        this.urlString = urlString;
        this.lastModifyTimeStamp = new Date().getTime();
        this.status = STATUS_PENDING;

        validateUrl();

        if (isValid) {
            //TODO check the url endwith param ? &
            fileName = urlString.substring(urlString.lastIndexOf('/') + 1, urlString.length());
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            fileExtension = urlString.substring(urlString.lastIndexOf("."));
        } else {
            fileName = "";
        }
    }

    public boolean isValid() {
        return isValid;
    }

    private void validateUrl() {
        try {
            url = new URL(urlString);
            isValid = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            isValid = false;
        }
    }

    public URL getUrl() {
        return url;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setFileSizeDownload(long fileSizeDownload) {
        this.fileSizeDownload = fileSizeDownload;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private OnCancelListener onCancelListener;

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void cancel() {
        if (onCancelListener != null) {
            onCancelListener.onCancel();
        }
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public String getStoreStr() {
        return String.valueOf(position) + "," + status + "," + String.valueOf(fileSize) + "," + urlString;
    }

    public static FileItem createByStoreStr(String storeStr) {
        try {
            int index = storeStr.indexOf(",");

            String str = storeStr.substring(index + 1);
            index = str.indexOf(",");
            int status = Integer.valueOf(str.substring(0, index));

            str = str.substring(index + 1);
            index = str.indexOf(",");
            long fileSize = Long.valueOf(str.substring(0, index));

            String urlString = str.substring(index + 1);

            FileItem fileItem = new FileItem(urlString);
            fileItem.setFileSize(fileSize);

            File file = new File(DownloadManager.DL_DIR + fileItem.getFileName());
            if (file.exists()) {
                fileItem.setFileSizeDownload(file.length());
                if (fileSize == file.length()) {
                    fileItem.setStatus(STATUS_COMPLETE);
                } else {
                    fileItem.setStatus(STATUS_PENDING);
                }
            } else {
                fileItem.setStatus(STATUS_REMOVED);
            }

            return fileItem;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean exists() {
        return new File(DownloadManager.DL_DIR + fileName).exists();
    }
}
