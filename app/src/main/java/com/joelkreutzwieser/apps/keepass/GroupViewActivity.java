package com.joelkreutzwieser.apps.keepass;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.dropbox.DropboxFileActivity;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class GroupViewActivity extends AppCompatActivity implements NavigationDrawerFragment.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    public NavigationDrawerFragment drawerFragment;
    private KeePassDrawerLayout drawerLayout;
    private EntryListFragment entryListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerLayout = (KeePassDrawerLayout) findViewById(R.id.drawer_layout);
        entryListFragment = (EntryListFragment) getSupportFragmentManager().findFragmentById(R.id.entryList);
        drawerFragment.setUp(R.id.navigation_drawer, drawerLayout, toolbar);
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

    public void clickNavigationDrawer(View view) {
    }

    public void clickItem(View view) {
        drawerFragment.clickItem(view);
    }

    public void clickNavigationArrow(View view) {
        drawerFragment.clickNavigationArrow(view);
    }

    public void clickNewDatabase(View view) {
        Toast.makeText(this, "New Database load", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(this, DropboxFileActivity.class), 52);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "DB copied", Toast.LENGTH_SHORT).show();
        try {
            if (requestCode == 52 && resultCode == RESULT_OK && data != null) {
                File localFile = new File(getApplicationContext().getFilesDir(), data.getStringExtra("fileName"));
                InputStream inputStream = new FileInputStream(localFile);
                drawerFragment.clickNavigationLoad(inputStream);
            }
        } catch (Exception e) {
            Log.i("KeePass", "Failed to load file", e);
        }
    }

    public void clickDatabaseLoad(View view) {
        drawerFragment.clickDatabaseLoad(view);
    }

    public void clickEntry(View view) {
        entryListFragment.clickItem(view);
    }

    public void onNavigationItemSelected(Group item) {
        Toast toast = Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT);
        toast.show();
        setTitle(item.getName());
        EntryListFragment entryListFragment = (EntryListFragment) getSupportFragmentManager().findFragmentById(R.id.entryList);
        entryListFragment.changeEntries(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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

    public void clickPasswordVisibility(View view) {
        drawerFragment.clickPasswordVisibility(view);
    }
}
