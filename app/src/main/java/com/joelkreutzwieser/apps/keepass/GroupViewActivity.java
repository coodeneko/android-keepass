package com.joelkreutzwieser.apps.keepass;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

public class GroupViewActivity extends AppCompatActivity implements NavigationDrawerFragment.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    private KeePassDrawerLayout drawerLayout;

    private Button mQueen;
    private Button mHidden;
    private KeePassDrawerLayout mOuterLayout;
    private LinearLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerLayout = (KeePassDrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.navigation_drawer, drawerLayout, toolbar);

        mOuterLayout = (KeePassDrawerLayout) findViewById(R.id.drawer_layout);
        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mQueen = (Button) findViewById(R.id.queen_button);
        mQueen.setOnClickListener(this);
        mMainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mOuterLayout.isMoving()) {
                    v.setTop(oldTop);
                    v.setBottom(oldBottom);
                    v.setLeft(oldLeft);
                    v.setRight(oldRight);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_navigation clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickItem(View view) {
        drawerFragment.clickItem(view);
    }

    public void onNavigationItemSelected(Group item) {
        Toast toast = Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT);
        toast.show();
        setTitle(item.getName());
        EntryListFragment entryListFragment = (EntryListFragment)getSupportFragmentManager().findFragmentById(R.id.entryList);
        entryListFragment.changeEntries(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        Toast t = Toast.makeText(this, b.getText() + " clicked", Toast.LENGTH_SHORT);
        t.show();
    }
}
