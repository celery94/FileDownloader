package com.celery.filedownloader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadManager {

    private static DownloadManager instance = null;

    private List<FileItem> files;

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

    public int addFileItem(FileItem item) {
        item.setLastModifyTimeStamp(new Date().getTime());

        files.add(item);

        return files.indexOf(item);
    }

}
