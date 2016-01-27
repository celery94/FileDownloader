package com.celery.filedownloader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddFileDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText etUrl;
    private EditText etAddFileName;
    private TextView tvFileSize;

    public AddFileDialog() {

    }

    public static AddFileDialog newInstance(String title) {
        AddFileDialog addFileDialog = new AddFileDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        addFileDialog.setArguments(args);

        return addFileDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreateView");
        return inflater.inflate(R.layout.fragment_add_file, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("onViewCreated");

        etUrl = (EditText) view.findViewById(R.id.etUrl);
        etAddFileName = (EditText) view.findViewById(R.id.etAddFileName);
        tvFileSize = (TextView) view.findViewById(R.id.tvFileSize);

        etUrl.requestFocus();
        etUrl.setOnEditorActionListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        System.out.println("onEditorAction:");
        if (i == EditorInfo.IME_ACTION_DONE) {
            FileItem fileItem = new FileItem(etUrl.getText().toString());
            if(fileItem.IsValid()){
                new HttpTask().execute(etUrl.getText().toString());
                etAddFileName.setText(fileItem.getFileName());
            }
        }
        return false;
    }

    public interface UrlChangeListener {
        void onUrlChange(String inputText);
    }

    private class HttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return getFileSize(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("onPostExecute:" + s);
            tvFileSize.setText("(Size: " + s + ")");
        }

        private String getFileSize(String urlStr) {
            try {
                System.out.println("getFileSize:" + urlStr);
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setRequestMethod("GET");
                connection.connect();

                String fileSize;
                int contentLength = connection.getContentLength();
                if (contentLength == -1) {
                    fileSize = "Unknown";
                } else {
                    int mb = contentLength / 1024 / 1024;
                    if (mb == 0) {
                        fileSize = String.valueOf(contentLength / 1024) + "K";
                    } else {
                        fileSize = String.valueOf(mb) + "M";
                    }
                }

                System.out.println("getFileSize result:" + fileSize);
                return fileSize;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
