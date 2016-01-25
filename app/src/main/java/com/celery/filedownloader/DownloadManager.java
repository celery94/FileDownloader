package com.celery.filedownloader;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private List<FileItem> files;

    public List<FileItem> getFiles() {
        return files;
    }

    public DownloadManager() {
        files = new ArrayList<>();
    }
}
