package com.example.rkmvimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration2 extends AppCompatActivity
{

    int x=0;
    Bundle bundle;
    int invalid;

    ProgressBar progressBar;

    public String dept="";
    public String role="";
    RadioButton rb;
    public String num,name,password,email;
    private DatabaseReference ref2;
    ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        progressBar=findViewById(R.id.checking);
        progressBar.setVisibility(View.VISIBLE);

        bundle = getIntent().getExtras();
        num = bundle.getString("num");

        ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(num);
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    TextView tv=(TextView) findViewById(R.id.accExist);
                    tv.setText("❗ Account already exists.Please Login ❗");
                    x=1;
                    Button b=findViewById(R.id.getotp);
                    b.setBackgroundColor(getResources().getColor(R.color.button));
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref2.addValueEventListener(postListener);
        }


    public void goBack(View v)
    {
        finish();
    }

    public void nextRegister(View v)
    {
        invalid=0;

                    rb=findViewById(R.id.cs);
                    if(rb.isChecked()) {
                        dept = "Computer Science";
                    }
                    rb=findViewById(R.id.elec);
                    if(rb.isChecked()) {
                            dept = "Electronics";
                    }
                    rb=findViewById(R.id.zoo);
                    if(rb.isChecked()) {
                            dept = "Zoology";
                    }
                    rb=findViewById(R.id.mtm);
                    if(rb.isChecked()) {
                            dept = "Mathematics";
                    }
                    rb=findViewById(R.id.mcb);
                    if(rb.isChecked()) {
                            dept = "Microbiology";
                    }
                    rb=findViewById(R.id.cem);
                    if(rb.isChecked()) {
                            dept = "Chemistry";
                    }
                    rb=findViewById(R.id.icm);
                    if(rb.isChecked()) {
                            dept = "Industrial Chemistry";
                    }
                    rb=findViewById(R.id.phs);
                    if(rb.isChecked()) {
                            dept = "Physics";
                    }
                    rb=findViewById(R.id.sns);
                    if(rb.isChecked()) {
                            dept = "Sanskrit";
                    }
                    rb=findViewById(R.id.bng);
                    if(rb.isChecked()) {
                            dept = "Bengali";
                    }
                    rb=findViewById(R.id.ecn);
                    if(rb.isChecked()) {
                            dept = "Economics";
                    }
                    rb=findViewById(R.id.st);
                    if(rb.isChecked()) {
                            dept = "Statistics";
                    }
                    rb=findViewById(R.id.hst);
                    if(rb.isChecked()) {
                            dept = "History";
                    }
                    rb=findViewById(R.id.eng);
                    if(rb.isChecked()) {
                            dept = "English";
                    }
                    rb=findViewById(R.id.ott);
                    if(rb.isChecked()) {
                            dept = "Other";
                    }

                    /**End of departments coding*/

                    rb=findViewById(R.id.p);
                    if(rb.isChecked())
                    {
                        role="Principal";
                    }
        rb=findViewById(R.id.vp);
        if(rb.isChecked())
        {
            role="Vice Principal";
        }
        rb=findViewById(R.id.teacher);
        if(rb.isChecked())
        {
            role="Teacher";
        }
        rb=findViewById(R.id.mjs);
        if(rb.isChecked())
        {
            role="Maharaj";
        }
        rb=findViewById(R.id.lass);
        if(rb.isChecked())
        {
            role="Lab Assistant";
        }
        rb=findViewById(R.id.stu);
        if(rb.isChecked())
        {
            role="Student";
        }
        rb=findViewById(R.id.stf);
        if(rb.isChecked())
        {
            role="Staff";
        }
        rb=findViewById(R.id.gtman);
        if(rb.isChecked())
        {
            role="Gateman";
        }rb=findViewById(R.id.foodie);
        if(rb.isChecked())
        {
            role="Canteen";
        }rb=findViewById(R.id.lib);
        if(rb.isChecked())
        {
            role="Librarian";
        }
        rb=findViewById(R.id.p);
        if(rb.isChecked())
        {
            role="Other";
        }


        bundle = getIntent().getExtras();
        num = bundle.getString("num");
        name=bundle.getString("name");
        password=bundle.getString("password1");

        EditText e=findViewById(R.id.email_id);
        email=e.getText().toString().trim();

        if(x==1)
        {
            invalid=1;
            Toast.makeText(this,"Login Please,Account already exists",Toast.LENGTH_SHORT).show();
        }
        else if(email.length()<1)
        {
            invalid=1;
            Toast t=Toast.makeText(this,"Email Id can not be empty.",Toast.LENGTH_SHORT);
            t.show();
        }
        else if(dept.equals(""))
        {
            invalid=1;
            Toast t=Toast.makeText(this,"Please select your department.",Toast.LENGTH_SHORT);
            t.show();
        }
        else if(role.equals(""))
        {
            invalid=1;
            Toast t=Toast.makeText(this,"Please select your role.",Toast.LENGTH_SHORT);
            t.show();
        }


        if(invalid==0)
        {
            Intent intent = new Intent(com.example.rkmvimu.Registration2.this, FinishRegistration.class);
            intent.putExtra("num", num);
            intent.putExtra("name", name);
            intent.putExtra("password", password);
            intent.putExtra("email",email);
            intent.putExtra("dept",dept);
            intent.putExtra("role",role);
            startActivity(intent);
        }
    }
}
