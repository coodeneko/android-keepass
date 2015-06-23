package com.joelkreutzwieser.apps.keepass.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KeePassListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "listOfDatabases.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_NAME_ID = "entryID";
    public static final String COLUMN_NAME_LOCAL_FILE_NAME = "localFileName";
    public static final String COLUMN_NAME_ORIGIN = "origin";
    public static final String COLUMN_NAME_REVISION = "revision";
    public static final String COLUMN_NAME_REMOTE_PATH = "remotePath";
    public static final String COLUMN_NAME_LAST_USED_DATE = "lastUsed";

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "("
            + COLUMN_NAME_ID + " integer primary key autoincrement, "
            + COLUMN_NAME_LOCAL_FILE_NAME + " text not null, "
            + COLUMN_NAME_ORIGIN + " text not null, "
            + COLUMN_NAME_REVISION + " text not null, "
            + COLUMN_NAME_REMOTE_PATH + " text not null, "
            + COLUMN_NAME_LAST_USED_DATE + " integer not null);";

    public KeePassListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(KeePassListDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion
                        + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
