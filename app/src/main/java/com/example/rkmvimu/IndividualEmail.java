package com.example.rkmvimu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IndividualEmail extends AppCompatActivity {

    String s1, s2, s3;
    int x = 0, y = 0, z = 0;
    EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_email);

        Bundle bundle=getIntent().getExtras();
        String m=bundle.getString("email");

        e1 = findViewById(R.id.email_address);
        e1.setText(m);


        Button b = findViewById(R.id.send);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s1 = e1.getText().toString().trim();

                EditText e2 = findViewById(R.id.email_subject);
                s2 = e2.getText().toString().trim();

                EditText e3 = findViewById(R.id.email_message);
                s3 = e3.getText().toString().trim();


                if (s1.length()<1) {
                    Toast.makeText(getApplicationContext(), "Email address should not be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    x = 1;
                }
                if (s2.length()<1) {
                    Toast.makeText(getApplicationContext(), "Subject should not be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    y = 1;
                }
                if (s3.length()<1) {
                    Toast.makeText(getApplicationContext(), "Message should not be empty", Toast.LENGTH_SHORT).show();

                }
                else {
                    z = 1;
                }

                if (x == 1 && y == 1 && z == 1) {
                    String[] arr={s1};
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, arr);
                    intent.putExtra(Intent.EXTRA_SUBJECT, s2);
                    intent.putExtra(Intent.EXTRA_TEXT, s3);
                    if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email was not sent.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });

    }

    public void back(View v)
    {
        finish();
    }
}