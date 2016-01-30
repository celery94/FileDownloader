package com.celery.filedownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<FileItem, Integer, Integer> {

    private FilesAdapter filesAdapter;
    private DownloadManager downloadManager;

    public DownloadTask(FilesAdapter filesAdapter, DownloadManager downloadManager) {
        this.filesAdapter = filesAdapter;
        this.downloadManager = downloadManager;
    }

    @Override
    protected Integer doInBackground(FileItem... params) {
        System.out.println("DownloadTask doInBackground");

        FileItem fileItem = params[0];
        fileItem.setStatus(FileItem.STATUS_PENDING);
        int position = downloadManager.addFileItem(fileItem);

        if (!isNetworkAvailable()) {
            return position;
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection conn = null;
        long total = 0;

        try {

            URL url = fileItem.getUrl();
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.d("", "response code is: " + responseCode);
            Log.d("", "response content length is: " + conn.getContentLength() / 1024 + " k");

            inputStream = conn.getInputStream();

            File dir = new File(DownloadManager.DL_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(DownloadManager.DL_DIR + fileItem.getFileName());  //TODO check file exist
            Log.d("", "File download start: " + file.getPath());
            file.createNewFile();

            fileItem.setStatus(FileItem.STATUS_STARTED);

            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 4];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                total += count;

                outputStream.write(buffer, 0, count);

                fileItem.setFileSizeDownload(total);
                publishProgress(position);
            }

            Log.d("", "File download complete, size: " + String.valueOf(total / 1024) + " k");
            fileItem.setStatus(FileItem.STATUS_COMPLETE);
            fileItem.setFileSizeDownload(total);
        } catch (Exception e) {
            e.printStackTrace();
            fileItem.setStatus(FileItem.STATUS_ERROR);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (conn != null)
                conn.disconnect();
        }

        return position;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("onPreExecute");
        filesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        filesAdapter.notifyItemChanged(values[0]);
    }

    @Override
    protected void onPostExecute(Integer i) {
        System.out.println("Downloaded and result position: " + i);
        filesAdapter.notifyItemChanged(i);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) downloadManager.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(downloadManager.getContext());
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

