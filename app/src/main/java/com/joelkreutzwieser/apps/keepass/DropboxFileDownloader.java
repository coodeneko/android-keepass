package com.joelkreutzwieser.apps.keepass;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localFile;
    }
}