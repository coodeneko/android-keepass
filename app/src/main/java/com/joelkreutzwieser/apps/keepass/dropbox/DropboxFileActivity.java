package com.joelkreutzwieser.apps.keepass.dropbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.joelkreutzwieser.apps.keepass.ListDropboxFiles;
import com.joelkreutzwieser.apps.keepass.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DropboxFileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    final static private String APP_KEY = "XXXX";
    final static private String APP_SECRET = "XXXXX";

    private List<DropboxAPI.Entry> files;

    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
            try {
                ListDropboxFiles list = new ListDropboxFiles(mDBApi, "/", recyclerView, adapter);
                files = list.execute().get();
            } catch (Exception e) {
                Log.i("DbFileLog", "Error getting files", e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_file);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);
        mDBApi.getSession().startOAuth2Authentication(DropboxFileActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.fileList);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //properties = activeEntry.getProperties();
        adapter = new DropboxFileAdapter(new ArrayList<DropboxAPI.Entry>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dropbox_file, menu);
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

    public void clickFile(View view) {
        int selectedItemPosition = recyclerView.getChildLayoutPosition(view);
        DropboxAPI.Entry file = files.get(selectedItemPosition);
        if(file.isDir) {
            try {
                ListDropboxFiles list = new ListDropboxFiles(mDBApi, file.parentPath() + file.fileName(), recyclerView, adapter);
                files = list.execute().get();
            } catch (Exception e) {
                Log.i("DbFileLog", "Error getting files", e);
            }
        } else {
            try {
                DropboxFileDownloader downloader = new DropboxFileDownloader(mDBApi, file.path, file.fileName(), getApplicationContext());
                File localFile = downloader.execute().get();
                Intent intent = new Intent();
                intent.putExtra("fileName", localFile.getName());
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                Log.i("DbFileLog", "Error saving file to disk", e);
            }
        }
    }
}
