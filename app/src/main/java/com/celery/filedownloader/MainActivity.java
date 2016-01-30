package com.celery.filedownloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements AddFileDialog.AddClickListener {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FilesAdapter filesAdapter;
    DownloadManager downloadManager;

    public static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                AddFileDialog addFileDialog = AddFileDialog.newInstance();
                addFileDialog.show(fm, "fragment_add_file");
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rvFiles);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        downloadManager = new DownloadManager(this);

        filesAdapter = new FilesAdapter(downloadManager);
        recyclerView.setAdapter(filesAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_SETTINGS);
            }
        }
    }

    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                ((FloatingActionButton) findViewById(R.id.fab)).hide();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            downloadManager.clearList();
            filesAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_setting) {
            startActivityForResult(new Intent(this, SettingsActivity.class), RESULT_SETTINGS);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddClick(FileItem fileItem) {
        new DownloadTask(filesAdapter, downloadManager).execute(fileItem);
    }
}
