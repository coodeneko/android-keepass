package com.joelkreutzwieser.apps.keepass;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    Group activeGroup;
    List<Entry> entries;

    public EntryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_entry_list, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.entryList);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        entries = new ArrayList<>();

        changeEntries(((ApplicationBase) getActivity().getApplication()).getDatabaseRoot());

        return layout;
    }

    public void changeEntries(Group group){
        activeGroup = group;
        if(activeGroup != null) {
        entries = activeGroup.getAllEntries();}
        adapter = new EntryListEntryAdapter(entries);
        recyclerView.swapAdapter(adapter, false);
    }

    public void clickItem(View view) {
        int selectedItemPosition = recyclerView.getChildLayoutPosition(view);
        Entry entry = entries.get(selectedItemPosition);
        Intent intent = new Intent(getActivity(), EntryViewActivity.class);
        intent.putExtra("UUID", entry.getUuid());
        startActivity(intent);
    }
}
