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
        //files.add(new FileItem("http://zhstatic.zhihu.com/pkg/store/zhihu/zhihu-android-app-zhihu-release-2.4.4-244.apk"));
    }
}
