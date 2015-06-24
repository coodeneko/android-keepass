package com.joelkreutzwieser.apps.keepass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joelkreutzwieser.apps.keepass.KeePassList.KeePassListEntry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.List;

/**
 * Created by joel.kreutzwieser on 2015-06-24.
 */
public class NavigationDataBaseAdapter extends RecyclerView.Adapter<NavigationDataBaseAdapter.ViewHolder> {

    private List<KeePassListEntry> mDataset;

    // Provide a reference to the views for each data item_navigation
    // Complex data items may need more than one view per item_navigation, and
    // you provide access to all the views for a data item_navigation in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item_navigation is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.textView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NavigationDataBaseAdapter(List<KeePassListEntry> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NavigationDataBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_database, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).localFileName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
