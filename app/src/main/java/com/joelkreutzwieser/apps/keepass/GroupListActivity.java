package com.joelkreutzwieser.apps.keepass;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends Activity
        implements NavigationDrawerFragmentOld.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragmentOld mNavigationDrawerFragmentOld;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public final static String NEXT_ITEM_UUID = "com.joelkreutzwieser.app.keepass.NEXT_ITEM_UUID";

    List<ActiveItem> items;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        Intent intent = getIntent();

        String UUID = intent.getStringExtra(GroupListActivity.NEXT_ITEM_UUID);

        mNavigationDrawerFragmentOld = (NavigationDrawerFragmentOld)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragmentOld.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        Group activeGroup;
        if(((ApplicationBase)this.getApplication()).getDatabaseRoot() == null || UUID == null) {
            ((ApplicationBase)this.getApplication()).openDatabase(getResources().openRawResource(R.raw.testdatabase), "abcdefg");
            activeGroup = ((ApplicationBase)this.getApplication()).getDatabaseRoot();
        } else {
            activeGroup = ((ApplicationBase)this.getApplication()).getDatabaseRoot().getGroupByUUID(UUID);
            if(activeGroup == null) {
                activeGroup = ((ApplicationBase)this.getApplication()).getDatabaseRoot();
            }
        }

        setTitle(activeGroup.getName());
        List<Entry> entries = activeGroup.getEntries();
        List<Group> groups = activeGroup.getGroups();
        items = new ArrayList<>();
        for(Group group : groups)
            items.add(new ActiveItem(group.getName(), "Group", group));
        for(Entry entry : entries)
            items.add(new ActiveItem(entry.getTitle(), "Entry", entry));
        mAdapter = new MyAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragmentOld.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar navigation_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_entry_list, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((GroupListActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void clickItem(View view) {
        int selectedItemPosition = mRecyclerView.getChildPosition(view);
        ActiveItem item = items.get(selectedItemPosition);
        Intent intent;
        if(item.type.equals("Group")) {
            intent = new Intent(this, GroupListActivity.class);
        } else {
            intent = new Intent(this, EntryViewActivity.class);
        }
        intent.putExtra(NEXT_ITEM_UUID, item.getUUID());
        startActivity(intent);
    }
}
