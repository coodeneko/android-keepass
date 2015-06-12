package com.joelkreutzwieser.apps.keepass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;

import java.util.List;

public class EntryListEntryAdapter extends RecyclerView.Adapter<EntryListEntryAdapter.ViewHolder> {

    private List<Entry> mDataset;

    // Provide a reference to the views for each data item_entry
    // Complex data items may need more than one view per item_entry, and
    // you provide access to all the views for a data item_entry in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item_entry is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.entryTitle);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EntryListEntryAdapter(List<Entry> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EntryListEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
