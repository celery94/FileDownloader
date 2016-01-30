package com.celery.filedownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
    private Button btnCancel;
    private Button btnAdd;

    private FileItem fileItem;

    public AddFileDialog() {

    }

    public static AddFileDialog newInstance() {
        AddFileDialog addFileDialog = new AddFileDialog();
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
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        etUrl.requestFocus();
        etUrl.setOnEditorActionListener(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btn add click");

                if (fileItem != null && fileItem.IsValid() && fileItem.getFileSize() != "-2") {
                    AddClickListener clickListener = (AddClickListener) getActivity();
                    clickListener.onAddClick(fileItem);
                    dismiss();
                }
            }
        });

        etUrl.setText("http://zhstatic.zhihu.com/pkg/store/zhihu/zhihu-android-app-zhihu-release-2.4.4-244.apk");
        String urlStr = etUrl.getText().toString();
        if (urlStr != "") {
            getFileItem(urlStr);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            getFileItem(etUrl.getText().toString());
        }
        return false;
    }

    private void getFileItem(String urlStr) {
        fileItem = new FileItem(urlStr);
        if (fileItem.IsValid()) {

            etAddFileName.setText(fileItem.getFileName());

            new HttpTask().execute(fileItem);
        }
    }

    public interface AddClickListener {
        void onAddClick(FileItem fileItem);
    }

    private class HttpTask extends AsyncTask<FileItem, Void, String> {

        @Override
        protected String doInBackground(FileItem... params) {
            FileItem fileItem = params[0];

            int fileSize = getFileSize(fileItem.getUrl());
            fileItem.setFileSize(fileSize);

            return fileItem.getFileSize();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == "") {
                tvFileSize.setText("(Unable to resolve this url.)");
            } else {
                tvFileSize.setText("(Size: " + s + ")");
            }
        }

        private int getFileSize(URL url) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int contentLength = connection.getContentLength();

                System.out.println("get file(" + url.getPath() + ") size result:" + contentLength);
                return contentLength;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return -2;
        }
    }
}
