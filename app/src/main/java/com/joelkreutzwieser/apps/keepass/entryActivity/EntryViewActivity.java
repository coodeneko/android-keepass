package com.joelkreutzwieser.apps.keepass.entryActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joelkreutzwieser.apps.keepass.ApplicationBase;
import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;

public class EntryViewActivity extends AppCompatActivity {

    private EntryViewFragment propertyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String UUID = extras.getString("UUID");

        Entry entry = ((ApplicationBase)getApplication()).getDatabaseRoot().getEntryByUUID(UUID);

        setTitle(entry.getTitle());

        propertyListFragment = (EntryViewFragment)getSupportFragmentManager().findFragmentById(R.id.propertyListFragment);
        propertyListFragment.setActiveEntry(entry);

        Intent intent = new Intent(this, EntryViewActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Data avaiable for" + entry.getTitle())
                .setContentText(entry.getTitle())
                .setSmallIcon(R.drawable.ic_lock_outline_black_24dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_lock_outline_black_24dp, "Copy User", pIntent)
                .addAction(R.drawable.ic_lock_outline_black_24dp, "Copy Password", pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickCopyProperty(View view) {
        propertyListFragment.clickCopyProperty(view);
    }

    public void clickVisibilityProperty(View view) {
        propertyListFragment.clickVisibilityProperty(view);
    }
}
