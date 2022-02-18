package com.example.rkmvimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ForgotPassword1 extends AppCompatActivity {

    EditText et;
    String num;
    int invalid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);
    }

    public void check(View v)
    {
        invalid=0;
        et=findViewById(R.id.Number);
        num=et.getText().toString().trim();
        if(num.length()!=10)
        {
            invalid=1;
            Toast.makeText(this,"Number should be of 10 digits.",Toast.LENGTH_SHORT).show();
        }
        num="+91"+num;

        if(invalid==0) {
            Intent intent=new Intent(com.example.rkmvimu.ForgotPassword1.this,ForgotPassword2.class);
            intent.putExtra("num",num);
            startActivity(intent);
        }
    }

    public void b1(View v)
    {
        finish();
    }
}