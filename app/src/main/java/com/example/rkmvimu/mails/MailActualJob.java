package com.example.rkmvimu.mails;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MailActualJob extends SQLiteOpenHelper {

    private static final String SQL_CREATE =
            "CREATE TABLE " + MailBasicColumns.EntryColumns.TABLE_NAME + " (" +
                    MailBasicColumns.EntryColumns.COLUMN_NAME_TIME + " TEXT PRIMARY KEY," +
                    MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER + " TEXT,"+
                    MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT + " TEXT," +
                    MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT+ " TEXT,"+
                    MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL+" TEXT,"+
                    MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM+" TEXT,"+
                    MailBasicColumns.EntryColumns.COLUMN_NAME_READ+" TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MailBasicColumns.EntryColumns.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MailMessages.db";

    public MailActualJob(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
