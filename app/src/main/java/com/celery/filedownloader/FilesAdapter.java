package com.celery.filedownloader;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private DownloadManager downloadManager;

    public FilesAdapter() {
        downloadManager = DownloadManager.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FileItem item = downloadManager.getFiles().get(i);
        viewHolder.bindFileItem(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return downloadManager.getFiles().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;
        private TextView tvFileSize;
        private TextView tvCreateTime;
        private TextView tvStatus;
        private ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvFileSize = (TextView) itemView.findViewById(R.id.tvFileSize);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tvCreateTime);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }

        public void bindFileItem(FileItem item) {
            tvFileName.setText(item.getFileName());
            tvStatus.setText(item.getStatus());
            tvFileSize.setText(item.getFileSizeDownload());
            tvCreateTime.setText(item.getLastModifyTimeStamp());
//            if (mItem.getSelected()) {
//                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_grey600));
//            } else {
//                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_attachment_grey600));
//            }
        }
    }
}



