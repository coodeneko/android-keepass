package com.joelkreutzwieser.apps.keepass;

import android.app.Application;

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

    public void openDatabase(InputStream inputStream, String password) {
        database = KeePassDatabase.getInstance(inputStream).openDatabase(password);
    }

}
