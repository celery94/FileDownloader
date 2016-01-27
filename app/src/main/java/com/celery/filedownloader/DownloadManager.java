package com.celery.filedownloader;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    private static DownloadManager instance = null;
    private static final String DL_DIR = Environment.getExternalStorageDirectory() + "/Download/";

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

    public void addFileItem(FileItem item) {
        files.add(item);

        new DownloadTask().execute(item);
    }

    private class DownloadTask extends AsyncTask<FileItem, Integer, Integer> {

        @Override
        protected Integer doInBackground(FileItem... params) {
            System.out.println("DownloadTask doInBackground");

            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection conn = null;
            long total = 0;

            try {
                FileItem fileItem = params[0];

                URL url = fileItem.getUrl();
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("", "response code is: " + responseCode);
                Log.d("", "response content length is: " + conn.getContentLength() / 1024 + " k");

                inputStream = conn.getInputStream();

                File dir = new File(DL_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                File file = new File(DL_DIR + fileItem.getFileName());
                Log.d("", "File download start: " + file.getPath());
                file.createNewFile();

                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 4];
                int count;
                while ((count = inputStream.read(buffer)) != -1) {
                    total += count;

                    outputStream.write(buffer, 0, count);
                }
                Log.d("", "File download complete, size: " + String.valueOf(total / 1024) + " k");

                return (int) total;
            } catch (Exception e) {
                e.printStackTrace();
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

            return (int) total;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer i) {
            System.out.println("Downloaded: " + i);
            super.onPostExecute(i);
        }
    }
}
