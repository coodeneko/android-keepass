package com.joelkreutzwieser.apps.keepass;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.List;

public class ListDropboxFiles extends AsyncTask<Void, Void, List<Entry>> {

    private DropboxAPI<?> dropbox;
    private String path;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ListDropboxFiles(DropboxAPI<?> dropbox, String path, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this.dropbox = dropbox;
        this.path = path;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    protected List<Entry> doInBackground(Void... params) {
        List<Entry> files = new ArrayList<>();
        try {
            Entry directory = dropbox.metadata(path, 1000, null, true, null);
            if(!directory.fileName().equals("")) {
                files.add(dropbox.metadata(directory.parentPath(), 1000, null, true, null));
            }
            for(Entry folder : directory.contents) {
                if(folder.isDir) {
                    files.add(folder);
                }
            }
            for(Entry folder : directory.contents) {
                if(!folder.isDir) {
                    files.add(folder);
                }
            }
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return files;
    }

    @Override
    protected void onPostExecute(List<Entry> files) {
        adapter = new DropboxFileAdapter(files);
        recyclerView.swapAdapter(adapter, false);
    }
}
