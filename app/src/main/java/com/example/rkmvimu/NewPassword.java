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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPassword extends AppCompatActivity {

    int f1=0,f2=0;
    EditText pass1,pass2;
    TextView sh1,sh2;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void passShowHide1(View v)
    {
        sh1=findViewById(R.id.sh1);
        pass1=findViewById(R.id.Pass1);
        if(f1==0) {
            pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            sh1.setText("Hide Password.");
            f1=1;
        }
        else
        {
            pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            sh1.setText("Show Password.");
            f1=0;
        }

    }

    public void passShowHide2(View v)
    {
        sh2=findViewById(R.id.sh2);
        pass2=findViewById(R.id.Pass2);
        if(f2==0) {
            pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            sh2.setText("Hide Password.");
            f2=1;
        }
        else
        {
            pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            sh2.setText("Show Password.");
            f2=0;
        }

    }


    public void doneNewPassword(View v)
    {
        Bundle bundle=getIntent().getExtras();
        String num=bundle.getString("number");

        pass1=findViewById(R.id.Pass1);
        pass2=findViewById(R.id.Pass2);

        String p1=pass1.getText().toString().trim();
        String p2=pass2.getText().toString().trim();

        if (p1.equals(p2))
        {
            String u="Users";
            String p="password";
            if(num!=null) {
                ref = FirebaseDatabase.getInstance().getReference().child(u).child(num).child(p);
                ref.setValue(p1);

                Toast.makeText(this,"Password changed successfully.",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(com.example.rkmvimu.NewPassword.this, com.example.rkmvimu.LoginActivity.class));
            }
            else
            {
                Toast.makeText(this,"Number Error !",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords not same!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}