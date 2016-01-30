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
                System.out.println("restore All FileItems: " + urlString);
                FileItem fileItem = new FileItem(urlString);

                files.add(fileItem);
            }
        }
    }

    public int addFileItem(FileItem item) {
        files.add(item);

        Set<String> nameList = pref.getStringSet(PREF_KEY, null);
        nameList.add(item.getUrlString());
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(PREF_KEY, nameList);
        editor.commit();

        System.out.println("add File Item and total count is: " + nameList.size());

        return files.indexOf(item);
    }

    public void clearList() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        files = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }
}
