package com.celery.filedownloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

public class AddFileDialog extends DialogFragment {
    private EditText etUrl;

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

        etUrl = (EditText) view.findViewById(R.id.etUrl);
        String title = getArguments().getString("title", "Enter Url");
        getDialog().setTitle(title);
        etUrl.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
