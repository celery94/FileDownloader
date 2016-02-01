package com.celery.filedownloader;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private DownloadManager downloadManager;

    public FilesAdapter(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FileItem item = downloadManager.getFiles().get(i);
        viewHolder.bindFileItem(item, this);
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
        private LinearLayout layoutFileItem;
        private FileItem fileItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvFileSize = (TextView) itemView.findViewById(R.id.tvFileSize);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tvCreateTime);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            layoutFileItem = (LinearLayout) itemView.findViewById(R.id.llFileItem);
        }

        public void bindFileItem(FileItem item, final FilesAdapter filesAdapter) {
            fileItem = item;

            tvFileName.setText(item.getFileName());
            tvStatus.setText(item.getStatusStr());
            tvFileSize.setText(item.getFileSizeDownload());
            tvCreateTime.setText(item.getLastModifyTimeStamp());

            layoutFileItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileItem.getStatus() == FileItem.STATUS_COMPLETE) {
                        //TODO Open File
                    } else if (fileItem.getStatus() == FileItem.STATUS_PENDING) {
                        if (downloadManager.isNetworkAvailable()) {
                            new DownloadTask(filesAdapter).execute(fileItem);
                        } else {
                            Snackbar.make(v, "Please check network!", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    } else if (fileItem.getStatus() == FileItem.STATUS_REMOVED) {
                        //TODO Download again?
                    }else if (fileItem.getStatus() == FileItem.STATUS_STARTED) {
                        Snackbar.make(v, "Please wait for download complete!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    }
}



