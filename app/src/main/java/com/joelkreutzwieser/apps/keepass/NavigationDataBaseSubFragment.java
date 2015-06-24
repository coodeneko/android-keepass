package com.joelkreutzwieser.apps.keepass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.KeePassList.KeePassListDatabase;
import com.joelkreutzwieser.apps.keepass.KeePassList.KeePassListEntry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDataBaseSubFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<KeePassListEntry> databases;

    public NavigationDataBaseSubFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_database_dialog, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.navigationDatabaseList);

        KeePassListDatabase database = new KeePassListDatabase(getActivity());
        try {
            database.open();
            databases = database.getAllEntries();
            database.close();
            for(KeePassListEntry entry : databases) {
                System.out.println(entry.localFileName);
                System.out.println(entry.lastUsed);
            }
        } catch (Exception e) {
            Log.i("DB", "Error with DB", e);
        }

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NavigationDataBaseAdapter(databases);
        recyclerView.setAdapter(adapter);

        return layout;
    }

    public KeePassListEntry getEntryByClick(View view) {
        return databases.get(recyclerView.getChildLayoutPosition(view));
    }
}
