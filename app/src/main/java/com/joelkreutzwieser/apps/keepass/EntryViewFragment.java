package com.joelkreutzwieser.apps.keepass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Property;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    Entry activeEntry;
    List<Property> properties;


    public EntryViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_entry_view, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.propertyList);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //properties = activeEntry.getProperties();
        adapter = new EntryViewPropertyAdapter(null);
        recyclerView.setAdapter(adapter);

        return layout;
    }

    public void setActiveEntry(Entry entry) {
        activeEntry = entry;
        properties = activeEntry.getProperties();
        adapter = new EntryViewPropertyAdapter(properties);
        recyclerView.swapAdapter(adapter, false);
    }
}
