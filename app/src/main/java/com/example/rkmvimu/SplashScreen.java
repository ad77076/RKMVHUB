package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;

public class SplashScreen extends AppCompatActivity
{
    Cursor c;
    String s="0";
    String toAddress="";
    String n="";
    String check="1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ActualJob con=new ActualJob(this);
        SQLiteDatabase help=con.getReadableDatabase();

        String[] pro={BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN,BasicInfo.EntryColumns.COLUMN_NAME_MOBILE};
        try {
            c = help.query(
                    BasicInfo.EntryColumns.TABLE_NAME,
                    pro,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            int x = c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN);
            int z=c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE);
            if (c.moveToLast()) {
                s = c.getString(x);
                n=c.getString(z);
            }
        }finally {
            c.close();
        }
        Thread splashTread;
        if(s.equals(check)) {
            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (Exception e) {
                    } finally {
                        startActivity(new Intent(com.example.rkmvimu.SplashScreen.this, MainActivity.class));
                        finish();
                    }
                }
            };
        }
        else
        {
            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (Exception e) {
                    } finally {
                        startActivity(new Intent(com.example.rkmvimu.SplashScreen.this, com.example.rkmvimu.LoginActivity.class));
                        finish();
                    }
                }
            };
        }
        splashTread.start();
    }
}