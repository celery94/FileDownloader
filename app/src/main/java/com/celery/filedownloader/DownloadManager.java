package com.celery.filedownloader;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private List<FileItem> files;

    private static DownloadManager instance = null;

    public List<FileItem> getFiles() {
        return files;
    }

    private DownloadManager() {
        files = new ArrayList<>();
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }

        return instance;
    }

    public void addFileItem(FileItem item) {
        files.add(item);
    }
}
