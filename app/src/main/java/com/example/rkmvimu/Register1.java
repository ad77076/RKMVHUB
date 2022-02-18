package com.example.rkmvimu;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register1 extends AppCompatActivity {

    Intent intent;
    int invalid=0;
    int i=0;
    int f1=0,f2=0;
    TextView sh;
    EditText pass;
    public String num;

    EditText cache;
    public String name,password1,password2,sc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
    }

    public void b(View v)
    {
        finish();
    }

    public void passSH1(View v)
    {
        sh=findViewById(R.id.ShowHide1);
        pass=findViewById(R.id.password1);
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


    public void passSH2(View v)
    {
        sh=findViewById(R.id.ShowHide2);
        pass=findViewById(R.id.password2);
        if(f2==0) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            sh.setText("Hide Password.");
            f2=1;
        }
        else
        {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            sh.setText("Show Password.");
            f2=0;
        }

    }

    public void nextPage(View v)
    {
        invalid=0;
        i=0;
        cache=findViewById(R.id.Number);
        num=cache.getText().toString().trim();

        cache=findViewById(R.id.fullname);
        name=cache.getText().toString().trim();

        cache=findViewById(R.id.password1);
        password1=cache.getText().toString().trim();

        cache=findViewById(R.id.password2);
        password2=cache.getText().toString().trim();

        cache=findViewById(R.id.special);
        sc=cache.getText().toString().trim();

        if(num.length() != 10)
        {
            invalid=1;
            Toast t=Toast.makeText(this," Number should be of 10 digits ! ",Toast.LENGTH_SHORT);
            t.show();
        }
        if(name.length()<1)
        {
            invalid=1;
            Toast t=Toast.makeText(this," Name section cannot be empty ! ",Toast.LENGTH_SHORT);
            t.show();
        }
        if(password1.length()<1)
        {
            invalid=1;
            Toast t=Toast.makeText(this," Password section cannot be empty ! ",Toast.LENGTH_SHORT);
            t.show();
        }
        if(password2.length()<1)
        {
            invalid=1;
            Toast t=Toast.makeText(this," Password section cannot be empty ! ",Toast.LENGTH_SHORT);
            t.show();
        }
        if(!password1.equals(password2))
        {
            invalid=1;
            Toast t=Toast.makeText(this," Passwords are not matching. ",Toast.LENGTH_SHORT);
            t.show();
        }
        if(!sc.equals("999"))
        {
            invalid=1;
            Toast t=Toast.makeText(this," Invalid Special Code. ",Toast.LENGTH_SHORT);
            t.show();
        }

        num="+91"+num;

        if(invalid==0)
        {
            intent = new Intent(com.example.rkmvimu.Register1.this, com.example.rkmvimu.Registration2.class);
            intent.putExtra("num", num);
            intent.putExtra("name", name);
            intent.putExtra("password1", password1);
            startActivity(intent);
        }

    }

}