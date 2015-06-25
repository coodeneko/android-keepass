package com.joelkreutzwieser.apps.keepass;

import android.app.Application;
import android.util.Log;

import com.joelkreutzwieser.apps.keepass.keepass.KeePassAsync;
import com.joelkreutzwieser.apps.keepass.keepass.KeePassDatabase;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import java.io.InputStream;

public class ApplicationBase extends Application {

    private KeePassFile database = null;

    public Group getDatabaseRoot() {
        if(database != null) {
            return database.getMainGroup();
        }
        return null;
    }

    public boolean isDatabaseOpen() {
        return (database != null);
    }

    public void openDatabase(InputStream inputStream, String password) {
        KeePassAsync keePassAsync = new KeePassAsync(inputStream, password);
        try {
            database = keePassAsync.execute().get();
        } catch (Exception e) {
            Log.i("KPLoading", "Failed to load db", e);
        }
        //database = KeePassDatabase.getInstance(inputStream).openDatabase(password);
    }

}
