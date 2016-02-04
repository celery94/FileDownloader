package com.celery.filedownloader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
        FileItem fileItem = params[0];

        final DownloadTask me = this;
        fileItem.setOnCancelListener(new FileItem.OnCancelListener() {
            @Override
            public void onCancel() {
                me.cancel(true);
            }
        });

        InputStream inputStream = null;
        HttpURLConnection conn = null;
        RandomAccessFile randomAccessFile = null;

        try {
            File dir = new File(DownloadManager.DL_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(DownloadManager.DL_DIR + fileItem.getFileName());
            if (!file.exists()) {
                file.createNewFile();
            } else {
                Log.d(LOG_TAG, "file exist and length is: " + file.length());
            }

            URL url = fileItem.getUrl();
            conn = (HttpURLConnection) url.openConnection();

            long total = fileItem.getFileLength() == file.length() ? 0 : file.length();
            if (total != 0) {
                conn.setRequestProperty("RANGE", "bytes=" + total + "-");
            }

            conn.connect();
            Log.d(LOG_TAG, "response code is: " + conn.getResponseCode() + " and content length is: " + conn.getContentLength());

            inputStream = conn.getInputStream();

            fileItem.setStatus(FileItem.STATUS_STARTED);

            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(total);
            Log.d(LOG_TAG, "RandomAccessFile start at: " + total);

            byte[] buffer = new byte[1024 * 4];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                total += count;

                randomAccessFile.write(buffer, 0, count);

                fileItem.setFileSizeDownload(total);
                publishProgress(fileItem.getPosition());

                if (isCancelled()) {
                    Log.d(LOG_TAG, "doInBackground: task cancelled in length:" + total);
                    fileItem.setStatus(FileItem.STATUS_PENDING);

                    try {
                        randomAccessFile.close();
                        inputStream.close();
                    } catch (IOException ignored) {
                    }

                    conn.disconnect();

                    return fileItem.getPosition();
                }
            }

            Log.d(LOG_TAG, "File download complete, size: " + total);
            fileItem.setStatus(FileItem.STATUS_COMPLETE);
            fileItem.setFileSizeDownload(total);
        } catch (Exception e) {
            e.printStackTrace();
            fileItem.setStatus(FileItem.STATUS_ERROR);
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

        return fileItem.getPosition();
    }

    @Override
    protected void onPreExecute() {
        filesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        filesAdapter.notifyItemChanged(values[0]);
    }

    @Override
    protected void onPostExecute(Integer i) {
        filesAdapter.notifyItemChanged(i);
    }
}

