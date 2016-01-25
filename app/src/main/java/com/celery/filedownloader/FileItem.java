package com.celery.filedownloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class FileItem {

    private String fileName;
    private String fileExtension;
    private String fileNameWithoutExtension;

    private int fileSize;

    private int fileSizeTotal;

    private String status;

    private long lastModifyTimeStamp;

    private String urlString;

    private boolean selected;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFileSizeTotal() {
        return fileSizeTotal;
    }

    public void setFileSizeTotal(int fileSizeTotal) {
        this.fileSizeTotal = fileSizeTotal;
    }

    public void setLastModifyTimeStamp(long lastModifyTimeStamp) {
        this.lastModifyTimeStamp = lastModifyTimeStamp;
    }

    public long getLastModifyTimeStamp() {
        return lastModifyTimeStamp;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    private URL url;
    private boolean isValid;

    public FileItem(String url) {
        this.urlString = url;
        this.lastModifyTimeStamp = new Date().getTime();

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
}
