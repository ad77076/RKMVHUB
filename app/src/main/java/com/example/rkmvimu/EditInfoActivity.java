package com.example.rkmvimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditInfoActivity extends AppCompatActivity {

    String num="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Bundle bundle=getIntent().getExtras();
        num=bundle.getString("number");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void backPressed(View v)
    {
        finish();
    }

    public void saveCalled(View v)
    {
        int w=0,x=0,y=0,z=0;
        EditText e1=findViewById(R.id.new_name);
        String s1=e1.getText().toString().trim();

        EditText e2=findViewById(R.id.new_email);
        String s2=e2.getText().toString().trim();

        EditText e3=findViewById(R.id.new_dept);
        String s3=e3.getText().toString().trim();

        EditText e4=findViewById(R.id.new_role);
        String s4=e4.getText().toString().trim();

        if(s1.equals(""))
        {
            w=1;
        }
        else
        {
            DatabaseReference r= FirebaseDatabase.getInstance().getReference().child("Users").child(num).child("name");
            r.setValue(s1);
        }
        if(s2.equals(""))
        {
            x=1;
        }
        else
        {
            DatabaseReference r= FirebaseDatabase.getInstance().getReference().child("Users").child(num).child("email");
            r.setValue(s2);
        }
        if(s3.equals(""))
        {
            y=1;
        }
        else
        {
            DatabaseReference r= FirebaseDatabase.getInstance().getReference().child("Users").child(num).child("dept");
            r.setValue(s3);
        }
        if(s4.equals(""))
        {
            z=1;
        }
        else
        {
            DatabaseReference r= FirebaseDatabase.getInstance().getReference().child("Users").child(num).child("role");
            r.setValue(s4);
        }

        if(x==0 || y==0 || w==0 || z==0)
        {
            Toast.makeText(this,"Updated Successfully.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"No changes were made.",Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this,MainActivity.class));
    }
}