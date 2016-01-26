package com.celery.filedownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddFileDialog extends DialogFragment {
    private EditText etUrl;
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
        return inflater.inflate(R.layout.fragment_add_file, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etUrl = (EditText) view.findViewById(R.id.etUrl);
        etUrl.requestFocus();

        etUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                UrlChangeListener listener = (UrlChangeListener) getActivity();
//                listener.onUrlChange(s.toString());

                new HttpTask().execute(s.toString());
            }
        });

        tvFileSize = (TextView) view.findViewById(R.id.tvFileSize);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
            tvFileSize.setText(s);
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

                String fileSize = String.valueOf(connection.getContentLength());
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
