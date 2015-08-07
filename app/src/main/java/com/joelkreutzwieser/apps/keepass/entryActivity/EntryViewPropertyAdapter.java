package com.joelkreutzwieser.apps.keepass.entryActivity;

import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Property;

import java.util.List;

public class EntryViewPropertyAdapter extends RecyclerView.Adapter<EntryViewPropertyAdapter.ViewHolder> {

    private List<Property> propertyList;

    // Provide a reference to the views for each data item_entry
    // Complex data items may need more than one view per item_entry, and
    // you provide access to all the views for a data item_entry in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView propertyTitle;
        public TextView propertyEntry;
        public ImageView propertyVisibility;

        public ViewHolder(View v) {
            super(v);
            this.propertyTitle = (TextView) v.findViewById(R.id.propertyTitle);
            this.propertyEntry = (TextView) v.findViewById(R.id.propertyEntry);
            this.propertyVisibility = (ImageView) v.findViewById(R.id.propertyVisibility);
        }
    }

    public EntryViewPropertyAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EntryViewPropertyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.propertyTitle.setText(propertyList.get(position).getKey());
        holder.propertyEntry.setText(propertyList.get(position).getValue());
        if(propertyList.get(position).isProtected()) {
            holder.propertyEntry.setTransformationMethod(PasswordTransformationMethod.getInstance());
            holder.propertyVisibility.setVisibility(View.VISIBLE);
        }
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return propertyList.size();
    }
}
