package com.joelkreutzwieser.apps.keepass.keepass;

import android.os.AsyncTask;

import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import java.io.InputStream;

public class KeePassAsync extends AsyncTask<Void, Void, KeePassFile> {

    InputStream inputStream;
    String password;

    public KeePassAsync(InputStream inputStream, String password) {
        this.inputStream = inputStream;
        this.password = password;
    }

    @Override
    protected KeePassFile doInBackground(Void... params) {
        return KeePassDatabase.getInstance(inputStream).openDatabase(password);
    }
}
