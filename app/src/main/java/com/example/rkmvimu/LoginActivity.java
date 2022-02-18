package com.example.rkmvimu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.data.BasicInformationObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    int invalid = 0;
    BasicInformationObject bio;
    ValueEventListener postListener;
    String pus="",numb="";
    String n,p;
    int flag=0;
    ActualJob dbH;
    ContentValues values;

    int f1=0;
    EditText Number,Password;
    EditText pass;
    TextView sh;

    String namee,emailyy,dptyy,rolyy;
    SQLiteDatabase db;
    private DatabaseReference ref2;
    ProgressBar progressBar;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=findViewById(R.id.login_wait);

        button=findViewById(R.id.login);
        button.setOnClickListener(v -> {
            invalid=0;
            progressBar.setVisibility(View.VISIBLE);

            Number = findViewById(R.id.Number);
            Password = findViewById(R.id.Password);
            n = Number.getText().toString().trim();
            p = Password.getText().toString().trim();

            if (n.length() != 10) {
                invalid = 1;
                Toast tl = Toast.makeText(getApplicationContext(), " Number should be of 10 digits ! ", Toast.LENGTH_SHORT);
                tl.show();
            }
            if (p.length() < 1) {
                invalid = 1;
                Toast tl = Toast.makeText(getApplicationContext(), " Password can not be empty ! ", Toast.LENGTH_SHORT);
                tl.show();
            }

            if(invalid==0) {
                n="+91"+n;

                ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(n);
                postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            flag = 1;
                            bio = dataSnapshot.getValue(BasicInformationObject.class);
                            assert bio != null;
                            numb = bio.getNumber();
                            pus = bio.getPassword();
                            namee = bio.getName();
                            emailyy = bio.getEmail();
                            dptyy = bio.getDept();
                            rolyy = bio.getRole();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            flag = 0;
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast t = Toast.makeText(getApplicationContext(), " Not registered. ", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                ref2.addValueEventListener(postListener);

                if (flag==1)
                {
                    if (p.equals(pus))
                    {
                        dbH = new ActualJob(getApplicationContext());
                        db = dbH.getWritableDatabase();
                        values = new ContentValues();
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE,numb);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_NAME,namee);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_PASSWORD,pus);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_EMAIL,emailyy);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_DEPARTMENT,dptyy);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_ROLE,rolyy);
                        values.put(BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN,"1");

                        long newRowId = db.insert(BasicInfo.EntryColumns.TABLE_NAME, null, values);
                        db.close();

                        if(newRowId!= -1)
                        {
                            Toast tl = Toast.makeText(getApplicationContext(), " Login Successful. ", Toast.LENGTH_SHORT);
                            tl.show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else
                        {
                            Toast tl = Toast.makeText(getApplicationContext(), " Some Error ! ", Toast.LENGTH_SHORT);
                            tl.show();
                        }
                    }
                    else
                    {
                        Toast tl = Toast.makeText(getApplicationContext(), " Check your password and try again ! ", Toast.LENGTH_SHORT);
                        tl.show();
                    }
                }
        }
    });
}

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void passShowHide(View v)
    {
        sh=findViewById(R.id.ShowHide);
        pass=findViewById(R.id.Password);
        if(f1==0) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            sh.setText("Hide Password.");
            f1=1;
        }
        else
        {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            sh.setText("Show Password.");
            f1=0;
        }

    }

    /*public void login(View v)
    {
        invalid=0;

        progressBar.setVisibility(View.VISIBLE);

        Number = findViewById(R.id.Number);
        Password = findViewById(R.id.Password);
        n = Number.getText().toString().trim();
        p = Password.getText().toString().trim();

        if (n.length() != 10) {
            invalid = 1;
            Toast tl = Toast.makeText(this, " Number should be of 10 digits ! ", Toast.LENGTH_SHORT);
            tl.show();
        }
        if (p.length() < 1) {
            invalid = 1;
            Toast tl = Toast.makeText(this, " Password can not be empty ! ", Toast.LENGTH_SHORT);
            tl.show();
        }

        if(invalid==0) {
            n="+91"+n;

            ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(n);
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        flag = 1;
                        bio = dataSnapshot.getValue(BasicInformationObject.class);
                        assert bio != null;
                        numb = bio.getNumber();
                        pus = bio.getPassword();
                        namee = bio.getName();
                        emailyy = bio.getEmail();
                        dptyy = bio.getDept();
                        rolyy = bio.getRole();
                    } else {
                        flag = 0;
                        Toast t = Toast.makeText(getApplicationContext(), " Not registered. ", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };


            ref2.addValueEventListener(postListener);

            insertInfo();
        }
        }*/

    public void forgotPassword(View v)
    {
        startActivity(new Intent(com.example.rkmvimu.LoginActivity.this, com.example.rkmvimu.ForgotPassword1.class));
    }

    public void newAcc(View v)
    {
        startActivity(new Intent(com.example.rkmvimu.LoginActivity.this, Register1.class));
    }


        /*public void insertInfo()
        {
            if (flag==1)
            {
                if (p.equals(pus))
                {
                    dbH = new ActualJob(this);
                    db = dbH.getWritableDatabase();
                    values = new ContentValues();
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE,numb);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_NAME,namee);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_PASSWORD,pus);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_EMAIL,emailyy);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_DEPARTMENT,dptyy);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_ROLE,rolyy);
                    values.put(BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN,"1");

                    long newRowId = db.insert(BasicInfo.EntryColumns.TABLE_NAME, null, values);
                    db.close();

                    if(newRowId!= -1)
                    {
                        Toast tl = Toast.makeText(this, " Login Successful. ", Toast.LENGTH_SHORT);
                        tl.show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else
                    {
                        Toast tl = Toast.makeText(this, " Some Error ! ", Toast.LENGTH_SHORT);
                        tl.show();
                    }
                }
                else
                {
                    Toast tl = Toast.makeText(this, " Check your password and try again ! ", Toast.LENGTH_SHORT);
                    tl.show();
                }
            }
        }*/
}
