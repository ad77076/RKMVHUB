package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
{

    ImageView chat,status,profile,email;
    TextView ch,st,p,e;
    Cursor c;
    String s="";
    String toAddress="";

    @Override
    protected  void onResume()
    {
        super.onResume();
        //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("online");
        //databaseReference.setValue("1");
    }

    @Override
    protected void onStop()
    {
        //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("online");
        //databaseReference.setValue("0");
        super.onStop();
    }

    @Override
    protected void onStart()
    {
       //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("online");
       //databaseReference.setValue("1");
        super.onStart();
    }

    @Override
    protected void onDestroy()
    {
        //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("online");
        //databaseReference.setValue("0");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, com.example.rkmvimu.StatusService.class);
        startService(intent);

        email=findViewById(R.id.e);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag, new com.example.rkmvimu.EmailFragment());
        ft.commit();

        email.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.email1));
        e=findViewById(R.id.mail_text);
        e.setTextColor(Color.parseColor("#7e57c2"));


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
                s = c.getString(x);
            }
        }finally {
            c.close();
        }

        //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("online");
        //databaseReference.setValue("1");
    }

    @Override
    public void onBackPressed()
    {
     finishAffinity();
    }

    public void chatCalled(View v)
    {
        chat=findViewById(R.id.c);
        status=findViewById(R.id.s);
        profile=findViewById(R.id.p);
        email=findViewById(R.id.e);

        ch=findViewById(R.id.contacts_text);
        st=findViewById(R.id.status_text);
        p=findViewById(R.id.profile_text);
        e=findViewById(R.id.mail_text);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag, new ChatFragment());
        ft.commit();

        chat.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.chat2));
        status.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.status1));
        profile.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.profile1));
        email.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this,R.drawable.email2));

        ch.setTextColor(Color.parseColor("#7e57c2"));
        st.setTextColor(Color.parseColor("#fafafa"));
        p.setTextColor(Color.parseColor("#fafafa"));
        e.setTextColor(Color.parseColor("#fafafa"));
    }

    public void statusCalled(View v)
    {
        chat=findViewById(R.id.c);
        status=findViewById(R.id.s);
        profile=findViewById(R.id.p);
        email=findViewById(R.id.e);

        ch=findViewById(R.id.contacts_text);
        st=findViewById(R.id.status_text);
        p=findViewById(R.id.profile_text);
        e=findViewById(R.id.mail_text);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag, new com.example.rkmvimu.StatusFragment());
        ft.commit();

        chat.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.chat1));
        status.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.status2));
        profile.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.profile1));
        email.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this,R.drawable.email2));

        ch.setTextColor(Color.parseColor("#fafafa"));
        st.setTextColor(Color.parseColor("#7e57c2"));
        p.setTextColor(Color.parseColor("#fafafa"));
        e.setTextColor(Color.parseColor("#fafafa"));
    }

    public void profileCalled(View v)
    {
        chat=findViewById(R.id.c);
        status=findViewById(R.id.s);
        profile=findViewById(R.id.p);
        email=findViewById(R.id.e);

        ch=findViewById(R.id.contacts_text);
        st=findViewById(R.id.status_text);
        p=findViewById(R.id.profile_text);
        e=findViewById(R.id.mail_text);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag, new com.example.rkmvimu.ProfileFragment());
        ft.commit();

        chat.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.chat1));
        status.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.status1));
        profile.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.profile2));
        email.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this,R.drawable.email2));

        ch.setTextColor(Color.parseColor("#fafafa"));
        st.setTextColor(Color.parseColor("#fafafa"));
        p.setTextColor(Color.parseColor("#7e57c2"));
        e.setTextColor(Color.parseColor("#fafafa"));
    }

    public void emailCalled(View v)
    {
    chat=findViewById(R.id.c);
    status=findViewById(R.id.s);
    profile=findViewById(R.id.p);
    email=findViewById(R.id.e);

        ch=findViewById(R.id.contacts_text);
        st=findViewById(R.id.status_text);
        p=findViewById(R.id.profile_text);
        e=findViewById(R.id.mail_text);

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.frag, new com.example.rkmvimu.EmailFragment());
    ft.commit();

    chat.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.chat1));
    status.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.status1));
    profile.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this, R.drawable.profile1));
    email.setImageDrawable(ContextCompat.getDrawable(com.example.rkmvimu.MainActivity.this,R.drawable.email1));

        ch.setTextColor(Color.parseColor("#fafafa"));
        st.setTextColor(Color.parseColor("#fafafa"));
        p.setTextColor(Color.parseColor("#fafafa"));
        e.setTextColor(Color.parseColor("#7e57c2"));
    }



}