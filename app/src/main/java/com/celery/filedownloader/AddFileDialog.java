package com.celery.filedownloader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
        return inflater.inflate(R.layout.fragment_add_file, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                if (fileItem != null && fileItem.isValid() && fileItem.getFileSize() != String.valueOf(FileItem.URL_ERROR)) {
                    if (fileItem.exists()) {

                    }

                    AddClickListener clickListener = (AddClickListener) getActivity();
                    clickListener.onAddClick(fileItem);
                    dismiss();
                }
            }
        });

        String copiedText = getCopiedText();
        if (copiedText != "") {
            if (getFileItem(copiedText)) {
                etUrl.setText(copiedText);
            }
        }
    }

    private String getCopiedText() {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager.getPrimaryClip() == null) return "";

            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            return item.getText().toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "";
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

    private boolean getFileItem(String urlStr) {
        FileItem fItem = new FileItem(urlStr.trim());
        if (fItem.isValid()) {
            fileItem = fItem;
            etAddFileName.setText(fileItem.getFileName());
            new HttpTask().execute(fileItem);

            return true;
        } else {
            return false;
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
        protected void onPostExecute(String result) {
            if (result == String.valueOf(FileItem.URL_ERROR)) {
                tvFileSize.setText("(Unable to resolve this url.)");
            } else {
                tvFileSize.setText("(Size: " + result + ")");
            }
        }

        private int getFileSize(URL url) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int contentLength = connection.getContentLength();

                Log.d("HttpTask", "get file(" + url.getPath() + ") size result:" + contentLength);
                return contentLength;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return FileItem.URL_ERROR;
        }
    }
}
