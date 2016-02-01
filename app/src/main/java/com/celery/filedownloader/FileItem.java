package com.celery.filedownloader;

import java.io.File;
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
    private File file;

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

    public String getStatus() {
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

        validateUrl();

        if (isValid) {
            //TODO check the url endwith param ? &
            fileName = urlString.substring(urlString.lastIndexOf('/') + 1, urlString.length());
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            fileExtension = urlString.substring(urlString.lastIndexOf("."));
            file = new File(DownloadManager.DL_DIR, fileName);

            if (file.exists()) {
                setFileSize(file.length());
                setFileSizeDownload(file.length());
                setStatus(STATUS_COMPLETE);
            } else {
                setStatus(STATUS_REMOVED);
            }
        } else {
            fileName = "";
        }
    }

    public boolean IsValid() {
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

    public boolean exists() {
        return file.exists();
    }
}
