package com.joelkreutzwieser.apps.keepass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.Database.KeePassListDatabase;
import com.joelkreutzwieser.apps.keepass.Database.KeePassListEntry;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDataBaseSubFragment extends Fragment {


    public NavigationDataBaseSubFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        KeePassListDatabase database = new KeePassListDatabase(getActivity());
        try {
            database.open();
            List<KeePassListEntry> entries = database.getAllEntries();
            database.close();
            for(KeePassListEntry entry : entries) {
                System.out.println(entry.localFileName);
                System.out.println(entry.lastUsed);
            }
        } catch (Exception e) {
            Log.i("DB", "Error with DB", e);
        }

        return inflater.inflate(R.layout.fragment_navigation_database_dialog, container, false);
    }


}
