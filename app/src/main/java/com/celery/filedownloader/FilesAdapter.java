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

    private List<FileItem> list;
    private SparseBooleanArray selectedItems;

    public FilesAdapter(List<FileItem> list) {
        this.list = list;

        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FileItem item = list.get(i);
        viewHolder.bindFileItem(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(FileItem item) {
        list.add(item);

        notifyDataSetChanged();
    }

//    public void updateFileItem(long fileId, FileItem itemNew) {
//        for (int i = 0; i < list.size(); i++) {
//            FileItem item = list.get(i);
//            if (item.getFileDownloadId() == fileId) {
//                item.setFileSize(itemNew.getFileSize());
//                item.setFileSizeTotal(itemNew.getFileSizeTotal());
//                item.setStatus(itemNew.getStatus());
//                item.setLastModifyTimeStamp(itemNew.getLastModifyTimeStamp());
//                item.setUriString(itemNew.getUriString());
//
//                adapter.notifyItemChanged(i);
//            }
//        }
//    }
//
//    public void removeSelected() {
//        for (int i = selectedItems.size() - 1; i >= 0; i--) {
//            int position = selectedItems.keyAt(i);
//
//            long fileId = list.get(position).getFileDownloadId();
//            downloadManager.remove(fileId);
//
//            list.remove(selectedItems.keyAt(i));
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }

        notifyItemChanged(position);
    }

    public void clearSelection() {
        selectedItems.clear();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }

        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public String getSelectUrls() {
        String urls = "";
        for (int i = 0; i < selectedItems.size(); i++) {
            int position = selectedItems.keyAt(i);

            urls += list.get(position).getUrl() + ",";

        }
        return urls.substring(0, urls.length() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView tvFileName;
        private TextView tvFileSize;
        private TextView tvCreateTime;
        private TextView tvStatus;
        //private ImageView ivIcon;
        private FileItem mItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvFileSize = (TextView) itemView.findViewById(R.id.tvFileSize);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tvCreateTime);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            //ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bindFileItem(FileItem item) {
            mItem = item;

            tvFileName.setText(item.getFileName());
            tvStatus.setText(item.getStatus());
            tvFileSize.setText(getAppSize(item.getFileSize()) + "/" + getAppSize(item.getFileSizeTotal()));
            tvCreateTime.setText(getTimeString(item.getLastModifyTimeStamp()));
//            if (mItem.getSelected()) {
//                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_grey600));
//            } else {
//                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_attachment_grey600));
//            }
        }

        @Override
        public void onClick(View view) {
            if (mItem == null) {
                return;
            }

//            if (adapter.getSelectedItemCount() > 0) {
//                itemSelected(getPosition());
//            } else {
//                if (mItem.getStatus() == view.getResources().getString(R.string.download_status_successful)) {
//                    File file = new File(Uri.parse(mItem.getUriString()).getPath());
//                    try {
//                        FileOpen.openFile(view.getContext(), file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(view.getContext(), tvStatus.getText().toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
        }

        @Override
        public boolean onLongClick(View view) {
            itemSelected(getPosition());

            return true;
        }

        private void itemSelected(int position) {
//            adapter.toggleSelection(position);
//
//            if (adapter.getSelectedItemCount() > 0) {
//                if (mActionMode == null) {
//                    mActionMode = startSupportActionMode(actionModeCallback);
//                }
//
//                mActionMode.setTitle(adapter.getSelectedItemCount() + getString(R.string.txt_selected));
//
//                btnAddLink.hide();
//            } else {
//                mActionMode.finish();
//            }
//
//            if (adapter.getSelectedItems().contains(position)) {
//                mItem.setSelected(true);
//            } else {
//                mItem.setSelected(false);
//            }
        }

        private final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

        private static final int MB_2_BYTE = 1024 * 1024;
        private static final int KB_2_BYTE = 1024;

        private CharSequence getAppSize(long size) {
            if (size <= 0) {
                return "0M";
            }

            if (size >= MB_2_BYTE) {
                return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double) size / MB_2_BYTE)).append("M");
            } else if (size >= KB_2_BYTE) {
                return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double) size / KB_2_BYTE)).append("K");
            } else {
                return size + "B";
            }
        }

        private String getTimeString(long timeStamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String string = dateFormat.format(new Date(timeStamp));

            return string;
        }
    }
}



