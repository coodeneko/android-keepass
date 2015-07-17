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

    public void setDatabase(KeePassFile keePassFile) {
        this.database = keePassFile;
    }
}
