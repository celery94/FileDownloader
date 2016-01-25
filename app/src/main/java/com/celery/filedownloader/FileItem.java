package com.celery.filedownloader;

public class FileItem {
    private long fileDownloadId;
    private String fileName;
    private int fileSize;
    private int fileSizeTotal;
    private String status;
    private long lastModifyTimeStamp;

    private String uriString;
    private String urlDownload;

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

    public void setFileDownloadId(long fileDownloadId) {
        this.fileDownloadId = fileDownloadId;
    }

    public long getFileDownloadId() {
        return fileDownloadId;
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

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }
}
