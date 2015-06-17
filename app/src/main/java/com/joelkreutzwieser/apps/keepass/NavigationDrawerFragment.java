package com.joelkreutzwieser.apps.keepass;


import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements DatabaseCredentialsDialogFragment.NoticeDialogListener {

    public static final String PREFERENCE_FILE_NAME = "com.joelkreutzwieser.app.keepass.NAVIGATION_PREFERENCE";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private View containerView;

    private boolean userLearnedDrawer;
    private boolean fromSavedInstanceState;

    private boolean isNavigationGroup;
    private Fragment navigationGroup;

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
        manageNavigationFragment();
    }

    private void manageNavigationFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();

        if(((ApplicationBase) getActivity().getApplication()).isDatabaseOpen()) {
            navigationGroup = new NavigationGroupSubFragment();
            isNavigationGroup = true;
        } else {
            navigationGroup = new NavigationDataBaseSubFragment();
            isNavigationGroup = false;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.navigationFragment, navigationGroup);
        fragmentTransaction.addToBackStack("A");
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false);
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
        if ((!userLearnedDrawer && !fromSavedInstanceState) || !((ApplicationBase) getActivity().getApplication()).isDatabaseOpen()) {
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
        Group item = ((NavigationGroupSubFragment) navigationGroup).getChildLayoutPosition(view);
        sendToActivity.onNavigationItemSelected(item);
        drawerLayout.closeDrawers();
    }

    public void clickNavigationArrow(View view) {

        ImageView downArrow = (ImageView) getActivity().findViewById(R.id.navigationHeaderDownArrow);
        if (isNavigationGroup || !((ApplicationBase) getActivity().getApplication()).isDatabaseOpen()) {
            downArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            navigationGroup = new NavigationDataBaseSubFragment();
            isNavigationGroup = false;
        } else {
            downArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            navigationGroup = new NavigationGroupSubFragment();
            isNavigationGroup = true;
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navigationFragment, navigationGroup);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void clickNavigationLoad(View view) {
        DatabaseCredentialsDialogFragment dialogFragment = new DatabaseCredentialsDialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "DBLOAD");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText password = (EditText) dialog.getDialog().findViewById(R.id.password);
        try {
            ((ApplicationBase) getActivity().getApplication()).openDatabase(getResources().openRawResource(R.raw.testdatabase), password.getText().toString());
            sendToActivity.onNavigationItemSelected(((ApplicationBase) getActivity().getApplication()).getDatabaseRoot());
            Toast.makeText(getActivity(), ((ApplicationBase) getActivity().getApplication()).getDatabaseRoot().getName(), Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to Decrpyt", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendToActivity = (OnNavigationItemSelectedListener) activity;
    }
}
