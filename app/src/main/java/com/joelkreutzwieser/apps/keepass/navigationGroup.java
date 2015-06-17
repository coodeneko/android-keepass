package com.joelkreutzwieser.apps.keepass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationGroup extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Group> groups;

    public NavigationGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_group, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.navigationGroupList);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Group activeGroup = ((ApplicationBase) getActivity().getApplication()).getDatabaseRoot();
        if (activeGroup == null) {
            ((ApplicationBase) getActivity().getApplication()).openDatabase(getResources().openRawResource(R.raw.testdatabase), "abcdefg");
            activeGroup = ((ApplicationBase) getActivity().getApplication()).getDatabaseRoot();
        }

        groups = new ArrayList<>();
        groups.add(activeGroup);
        groups.addAll(activeGroup.getAllGroups());
        adapter = new NavigationGroupAdapter(groups);
        recyclerView.setAdapter(adapter);

        return layout;
    }


    public Group getChildLayoutPosition(View view) {
        return groups.get(recyclerView.getChildLayoutPosition(view));
    }
}
