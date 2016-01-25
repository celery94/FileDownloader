package com.celery.filedownloader;

import java.util.Date;

public class FileItem {
    private String fileName;

    private int fileSize;

    private int fileSizeTotal;

    private String status;

    private long lastModifyTimeStamp;

    private String url;

    private boolean selected;

    public FileItem(String url) {
        this.url = url;
        this.lastModifyTimeStamp = new Date().getTime();
        this.fileName = "Test";
        this.status = "TODO";
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
