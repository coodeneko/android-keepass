package com.joelkreutzwieser.apps.keepass.dropbox;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.dropbox.client2.DropboxAPI;
import com.joelkreutzwieser.apps.keepass.keepassDatabase.KeePassListDatabase;
import com.joelkreutzwieser.apps.keepass.keepassDatabase.KeePassListEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class DropboxFileDownloader extends AsyncTask<Void, Void, File> {

    private final String name;
    private Context context;
    private DropboxAPI<?> dropbox;
    private String path;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public DropboxFileDownloader(DropboxAPI<?> dropbox, String path, String name, Context context) {
        this.dropbox = dropbox;
        this.path = path;
        this.context = context;
        this.name = name;
    }

    @Override
    protected File doInBackground(Void... params) {

        File localFile = new File(context.getFilesDir(), name);
        try {
            FileOutputStream outputStream = new FileOutputStream(localFile);
            DropboxAPI.DropboxFileInfo file = dropbox.getFile(path, null, outputStream, null);
            KeePassListDatabase database = new KeePassListDatabase(context);
            database.open();
            KeePassListEntry entry = new KeePassListEntry();
            entry.localFileName = localFile.getName();
            entry.origin = "Dropbox";
            entry.remotePath = path;
            Date date = new Date();
            entry.lastUsed = date.getTime();
            entry.revision = file.getMetadata().rev;
            entry.keyFile = "";
            database.createEntry(entry);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localFile;
    }
}