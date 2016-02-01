package com.celery.filedownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
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

        Set<String> nameList = pref.getStringSet(PREF_KEY, new HashSet<String>());
        if (nameList != null) {

            for (String urlString : nameList) {
                System.out.println("restore All FileItems: " + urlString);
                FileItem fileItem = new FileItem(urlString);

                File file = new File(DownloadManager.DL_DIR, fileItem.getFileName());
                if (file.exists()) {
                    fileItem.setFileSize(file.length());
                    fileItem.setFileSizeDownload(file.length());
                    fileItem.setStatus(FileItem.STATUS_COMPLETE);
                } else {
                    fileItem.setStatus(FileItem.STATUS_REMOVED);
                }

                files.add(fileItem);
            }
        }
    }

    public void addFileItem(FileItem item) {
        files.add(item);
        item.setPosition(files.indexOf(item));

        Set<String> nameList = pref.getStringSet(PREF_KEY, new HashSet<String>());
        nameList.add(item.getUrlString());
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(PREF_KEY, nameList);
        editor.commit();

        System.out.println("add File Item and total count is: " + nameList.size());
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isOnlyWIFI = preferences.getBoolean("prefOnlyWifi", true);

        NetworkInfo networkInfo;
        if (isOnlyWIFI) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } else {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
