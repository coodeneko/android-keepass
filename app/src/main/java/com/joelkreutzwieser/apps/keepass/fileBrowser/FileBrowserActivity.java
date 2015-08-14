package com.joelkreutzwieser.apps.keepass.fileBrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.dropbox.DropboxFileAdapter;
import com.joelkreutzwieser.apps.keepass.dropbox.DropboxFileDownloader;
import com.joelkreutzwieser.apps.keepass.fileBrowser.fileList.FileListDropBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBrowserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String origin;

    private List<DropboxAPI.Entry> dropboxFiles;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    final static private String APP_KEY = "XXXX";
    final static private String APP_SECRET = "XXXXX";

    private File[] localFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent inputIntent = getIntent();
        if (!inputIntent.hasExtra("ORIGIN")) {
            throw new IllegalArgumentException("File Browser called with improper Intent");
        }

        origin = inputIntent.getStringExtra("ORIGIN");
        switch (origin) {
            case "DropBox":
                //TODO: Check for internet permission
                if (!checkInternetConnect(getApplicationContext())) {
                    throw new IllegalArgumentException("No Internet Access");
                }
                AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
                AndroidAuthSession session = new AndroidAuthSession(appKeys);
                mDBApi = new DropboxAPI<>(session);
                mDBApi.getSession().startOAuth2Authentication(FileBrowserActivity.this);
                adapter = new DropboxFileAdapter(new ArrayList<DropboxAPI.Entry>());
                break;
            case "Local":
                //TODO: Check for local file reading permission
                localFiles = Environment.getRootDirectory().listFiles();
                adapter = new FileBrowserAdapter(listFromFiles(localFiles));
                break;
            default:
                throw new IllegalArgumentException("Invalid file origin: " + origin);
        }

        setContentView(R.layout.activity_dropbox_file);
        recyclerView = (RecyclerView) findViewById(R.id.fileList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (origin.equals("DropBox") & mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
            try {
                FileListDropBox list = new FileListDropBox(mDBApi, "/", recyclerView, adapter);
                dropboxFiles = list.execute().get();
            } catch (Exception e) {
                Log.i("DbFileLog", "Error getting dropboxFiles", e);
            }
        }
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

        switch (origin) {
            case "DropBox":
                DropboxAPI.Entry file = dropboxFiles.get(selectedItemPosition);
                if (file.isDir) {
                    try {
                        FileListDropBox list = new FileListDropBox(mDBApi, file.parentPath() + file.fileName(), recyclerView, adapter);
                        dropboxFiles = list.execute().get();
                    } catch (Exception e) {
                        Log.i("DbFileLog", "Error getting files", e);
                    }
                } else {
                    try {
                        DropboxFileDownloader downloader = new DropboxFileDownloader(mDBApi, file.path, file.fileName(), this);
                        downloader.execute();
                        File localFile = downloader.get();
                        Intent intent = new Intent();
                        intent.putExtra("fileName", localFile.getName());
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        Log.i("DbFileLog", "Error saving file to disk", e);
                    }
                }
                break;
            case "Local":
                File localFile = localFiles[selectedItemPosition];
                if(localFile.isDirectory()) {
                    localFiles = localFile.listFiles();
                    adapter = new FileBrowserAdapter(listFromFiles(localFiles));
                    recyclerView.swapAdapter(adapter, false);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid file origin: " + origin);
        }
    }

    private boolean checkInternetConnect(Context context) {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<FileBrowserEntry> listFromFiles(File[] files) {
        List<FileBrowserEntry> output = new ArrayList<FileBrowserEntry>();
        for (File file : files) {
            FileBrowserEntry fileBrowserEntry = new FileBrowserEntry();
            fileBrowserEntry.name = file.getName();
            fileBrowserEntry.parent = file.getParent();
            fileBrowserEntry.isDir = file.isDirectory();
            fileBrowserEntry.mimeType = "";
            output.add(fileBrowserEntry);
        }
        return output;
    }

    private List<FileBrowserEntry> listFromDropBox(List<DropboxAPI.Entry> files) {
        List<FileBrowserEntry> output = new ArrayList<FileBrowserEntry>();
        for (DropboxAPI.Entry file : files) {
            FileBrowserEntry fileBrowserEntry = new FileBrowserEntry();
            fileBrowserEntry.name = file.fileName();
            fileBrowserEntry.parent = file.parentPath();
            fileBrowserEntry.isDir = file.isDir;
            fileBrowserEntry.mimeType = file.mimeType;
            output.add(fileBrowserEntry);
        }
        return output;
    }
}
