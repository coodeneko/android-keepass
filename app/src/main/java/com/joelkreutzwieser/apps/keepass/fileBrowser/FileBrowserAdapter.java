package com.joelkreutzwieser.apps.keepass.fileBrowser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelkreutzwieser.apps.keepass.R;

import java.util.List;

public class FileBrowserAdapter extends RecyclerView.Adapter<FileBrowserAdapter.ViewHolder> {

    private List<FileBrowserEntry> files;

    // Provide a reference to the views for each data item_entry
    // Complex data items may need more than one view per item_entry, and
    // you provide access to all the views for a data item_entry in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item_entry is just a string in this case
        public TextView mTextView;
        public TextView fileType;
        public ImageView fileImage;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.fileName);
            fileType = (TextView) v.findViewById(R.id.fileType);
            fileImage = (ImageView) v.findViewById(R.id.fileImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FileBrowserAdapter(List<FileBrowserEntry> files) {
        this.files = files;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FileBrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(files.get(position).name);
        if (files.get(position).isDir) {
            holder.fileType.setText("Directory");
            holder.fileImage.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            holder.fileType.setText(files.get(position).mimeType);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return files.size();
    }

}
