package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.BasicInformationObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClearStatus extends AppCompatActivity {

    String num = "";
    String from = "";
    String date = "";
    String status = "";

    TextView t1, t2, t3,t4;
    private DatabaseReference databaseReference;
    private DatabaseReference xyz;

    Intent intent;

    BasicInformationObject bio;

    ProgressBar progressBar;
    Cursor c;
    String s="";
    Integer i=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_status);
        progressBar=findViewById(R.id.progress_back);
        progressBar.setVisibility(View.VISIBLE);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("from");
        date = bundle.getString("time");
        status = bundle.getString("status");
        num = bundle.getString("number");

        t1 = findViewById(R.id.detail_from);
        t1.setText(from+" (See details)");
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileCheck(num);
            }
        });

        t2 = findViewById(R.id.detail_date);
        t2.setText(date);

        t3 = findViewById(R.id.detail_status);
        t3.setText(status);


        xyz = databaseReference.child("Users").child(num);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bio=snapshot.getValue(BasicInformationObject.class);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        xyz.addValueEventListener(valueEventListener);
    }

    public void backed(View v) {
        finish();
    }

    public void profileCheck(String num) {
        intent = new Intent(com.example.rkmvimu.ClearStatus.this, com.example.rkmvimu.CheckProfile.class);
        intent.putExtra("name", bio.getName());
        intent.putExtra("number", bio.getNumber());
        intent.putExtra("email", bio.getEmail());
        intent.putExtra("department", bio.getDept());
        intent.putExtra("role", bio.getRole());
        startActivity(intent);
    }
}