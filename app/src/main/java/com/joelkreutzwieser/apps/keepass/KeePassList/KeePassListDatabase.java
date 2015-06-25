package com.joelkreutzwieser.apps.keepass.KeePassList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeePassListDatabase {
    private SQLiteDatabase database;
    private KeePassListDatabaseHelper keePassListDatabaseHelper;
    private String[] allColumns = {
            KeePassListDatabaseHelper.COLUMN_NAME_ID,
            KeePassListDatabaseHelper.COLUMN_NAME_LOCAL_FILE_NAME,
            KeePassListDatabaseHelper.COLUMN_NAME_ORIGIN,
            KeePassListDatabaseHelper.COLUMN_NAME_REVISION,
            KeePassListDatabaseHelper.COLUMN_NAME_REMOTE_PATH,
            KeePassListDatabaseHelper.COLUMN_NAME_LAST_USED_DATE};

    public KeePassListDatabase(Context context) {
        keePassListDatabaseHelper = new KeePassListDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = keePassListDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        keePassListDatabaseHelper.close();
    }

    public KeePassListEntry createEntry(KeePassListEntry entry) {
        ContentValues values = new ContentValues();
        values.put(KeePassListDatabaseHelper.COLUMN_NAME_LOCAL_FILE_NAME, entry.localFileName);
        values.put(KeePassListDatabaseHelper.COLUMN_NAME_ORIGIN, entry.origin);
        values.put(KeePassListDatabaseHelper.COLUMN_NAME_REVISION, entry.revision);
        values.put(KeePassListDatabaseHelper.COLUMN_NAME_REMOTE_PATH, entry.remotePath);
        values.put(KeePassListDatabaseHelper.COLUMN_NAME_LAST_USED_DATE, entry.lastUsed);
        long insertID = database.insert(KeePassListDatabaseHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(KeePassListDatabaseHelper.TABLE_NAME, allColumns, KeePassListDatabaseHelper.COLUMN_NAME_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        KeePassListEntry newEntry = cursorToEntry(cursor);
        cursor.close();
        return newEntry;
    }

    public void deleteEntry(KeePassListEntry entry) {
        long id = entry.id;
        database.delete(KeePassListDatabaseHelper.TABLE_NAME, KeePassListDatabaseHelper.COLUMN_NAME_ID + " = " + id, null);
    }

    public List<KeePassListEntry> getAllEntries() {
        List<KeePassListEntry> entries = new ArrayList<>();

        Cursor cursor = database.query(KeePassListDatabaseHelper.TABLE_NAME, allColumns, null, null, null, null, KeePassListDatabaseHelper.COLUMN_NAME_LAST_USED_DATE + " DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            KeePassListEntry entry = cursorToEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    private KeePassListEntry cursorToEntry(Cursor cursor) {
        KeePassListEntry entry = new KeePassListEntry();
        entry.id = cursor.getLong(0);
        entry.localFileName = cursor.getString(1);
        entry.origin = cursor.getString(2);
        entry.revision = cursor.getString(3);
        entry.remotePath = cursor.getString(4);
        entry.lastUsed = cursor.getLong(5);
        return entry;
    }
}
