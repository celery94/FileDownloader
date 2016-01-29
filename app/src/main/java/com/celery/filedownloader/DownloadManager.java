package com.celery.filedownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownloadManager {

    private static final String PREF_KEY = "FilesList";
    public static final String DL_DIR = Environment.getExternalStorageDirectory() + "/Download/";

    private List<FileItem> files;
    private Context context;
    private SharedPreferences pref;

    public List<FileItem> getFiles() {
        return files;
    }

    public DownloadManager(Context context) {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.files = new ArrayList<>();

        restoreAllFileItems();
    }

    private void restoreAllFileItems() {

        Set<String> nameList = pref.getStringSet(PREF_KEY, null);
        if (nameList != null) {

            for (String urlString : nameList) {

                FileItem fileItem = new FileItem(urlString);

                String localUrl = DL_DIR + fileItem.getFileName();
                File file = new File(localUrl);
                if (file.exists()) {
                    fileItem.setFileSize(file.length());
                    fileItem.setFileSizeDownload(file.length());
                    fileItem.setStatus(FileItem.STATUS_COMPLETE);
                    fileItem.setLastModifyTimeStamp(file.lastModified());
                } else {
                    fileItem.setStatus(FileItem.STATUS_REMOVED);
                }

                files.add(fileItem);
            }
        }
    }

    private void refreshPref() {
        SharedPreferences.Editor editor = pref.edit();
        Set<String> fileNames = new HashSet<>();
        for (FileItem fileItem : files) {
            fileNames.add(fileItem.getUrlString());
        }
        editor.putStringSet(PREF_KEY, fileNames);
        editor.commit();
    }

    public int addFileItem(FileItem item) {
        item.setLastModifyTimeStamp(new Date().getTime());

        files.add(item);

        refreshPref();

        return files.indexOf(item);
    }

    public void clearList() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        files = new ArrayList<>();
    }
}
