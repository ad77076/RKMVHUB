package com.example.rkmvimu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActualJob extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BasicInfo.EntryColumns.TABLE_NAME + " (" +
                    BasicInfo.EntryColumns.COLUMN_NAME_MOBILE + " TEXT PRIMARY KEY," +
                    BasicInfo.EntryColumns.COLUMN_NAME_NAME + " TEXT," +
                    BasicInfo.EntryColumns.COLUMN_NAME_PASSWORD + " TEXT,"+
                    BasicInfo.EntryColumns.COLUMN_NAME_EMAIL+ " TEXT,"+
                    BasicInfo.EntryColumns.COLUMN_NAME_DEPARTMENT+" TEXT,"+
                    BasicInfo.EntryColumns.COLUMN_NAME_ROLE+" TEXT,"+
                    BasicInfo.EntryColumns.COLUMN_NAME_PICTURE+" TEXT,"+
                    BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN+" TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BasicInfo.EntryColumns.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LocalInformation.db";

    public ActualJob(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}