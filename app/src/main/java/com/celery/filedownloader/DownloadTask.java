package com.celery.filedownloader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<FileItem, Integer, Integer> {

    private static final String LOG_TAG = "DownloadTask";

    private FilesAdapter filesAdapter;

    public DownloadTask(FilesAdapter filesAdapter) {
        this.filesAdapter = filesAdapter;
    }

    @Override
    protected Integer doInBackground(FileItem... params) {
        System.out.println("DownloadTask doInBackground");

        FileItem fileItem = params[0];

        final DownloadTask me = this;
        fileItem.setOnCancelListener(new FileItem.OnCancelListener() {
            @Override
            public void onCancel() {
                me.cancel(true);
            }
        });

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection conn = null;
        long total = 0;

        try {

            URL url = fileItem.getUrl();
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.d(LOG_TAG, "response code is: " + responseCode);
            Log.d(LOG_TAG, "response content length is: " + conn.getContentLength() / 1024 + " k");

            inputStream = conn.getInputStream();

            File dir = new File(DownloadManager.DL_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(DownloadManager.DL_DIR + fileItem.getFileName());  //TODO check file exist
            Log.d(LOG_TAG, "File download start: " + file.getPath());
            file.createNewFile();

            fileItem.setStatus(FileItem.STATUS_STARTED);

            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 4];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                total += count;

                outputStream.write(buffer, 0, count);

                fileItem.setFileSizeDownload(total);
                publishProgress(fileItem.getPosition());

                if (isCancelled()) {
                    Log.d(LOG_TAG, "doInBackground: task cancelled.");
                    fileItem.setStatus(FileItem.STATUS_PENDING);

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

                    return fileItem.getPosition();
                }
            }

            Log.d(LOG_TAG, "File download complete, size: " + String.valueOf(total / 1024) + " k");
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

        return fileItem.getPosition();
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
}

