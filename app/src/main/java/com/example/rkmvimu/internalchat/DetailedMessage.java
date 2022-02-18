package com.example.rkmvimu.internalchat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.R;
import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;

public class DetailedMessage extends AppCompatActivity {

    String message="";
    String from="";
    String time="";
    String role="";
    String num="";

    TextView f,m,t;

    Cursor c;
    String myNum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_message);

        ActualJob con=new ActualJob(this);
        SQLiteDatabase help=con.getReadableDatabase();

        String[] pro={BasicInfo.EntryColumns.COLUMN_NAME_MOBILE};
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
            int x = c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE);
            if (c.moveToLast()) {
                myNum = c.getString(x);
            }
        }finally {
            c.close();
        }

        Bundle bundle=getIntent().getExtras();
        num=bundle.getString("num");

        message=bundle.getString("message");
        from=bundle.getString("from");
        time=bundle.getString("time");
        role=bundle.getString("role");

        f=findViewById(R.id.from);

        if(myNum.equals(num))
        {
            f.setText("From you:");
        }
        else {
            f.setText(from);
        }
        m=findViewById(R.id.message);
        m.setText(message);

        t=findViewById(R.id.time);
        t.setText(time);
    }

    public void back(View v)
    {
        finish();
    }
}