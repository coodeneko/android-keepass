package com.joelkreutzwieser.apps.keepass;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREFERENCE_FILE_NAME = "com.joelkreutzwieser.app.keepass.NAVIGATION_PREFERENCE";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String KEY_FROM_SAVED_INSTANCE = "from_saved_instance";

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private View containerView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean userLearnedDrawer;
    private boolean fromSavedInstanceState;

    List<Group> groups;

    OnNavigationItemSelectedListener sendToActivity;

    public interface OnNavigationItemSelectedListener {
        void onNavigationItemSelected(Group item);
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, false);
        if (savedInstanceState != null) {
            fromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation, container, false);
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


    public void setUp(int fragmentID, DrawerLayout drawerLayout, Toolbar toolbar) {
        this.drawerLayout = drawerLayout;
        this.containerView = getActivity().findViewById(fragmentID);
        this.drawerToggle = new ActionBarDrawerToggle(getActivity(), this.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!userLearnedDrawer) {
                    userLearnedDrawer = true;
                    savedToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, true);
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void syncState() {
                super.syncState();

            }
        };
        if (!userLearnedDrawer && !fromSavedInstanceState) {
            drawerLayout.openDrawer(this.containerView);
        }
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    public static void savedToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(preferenceName, defaultValue);
    }

    public void clickItem(View view) {
        int selectedItemPosition = recyclerView.getChildLayoutPosition(view);
        Group item = groups.get(selectedItemPosition);
        sendToActivity.onNavigationItemSelected(item);
        drawerLayout.closeDrawers();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendToActivity = (OnNavigationItemSelectedListener)activity;
    }
}
