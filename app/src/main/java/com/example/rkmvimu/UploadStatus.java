package com.example.rkmvimu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.BasicInformationObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadStatus extends AppCompatActivity {

    String number;
    String name="";
    BasicInformationObject bio;
    DatabaseReference db;
    ValueEventListener valueEventListener;
    EditText statusMessage;
    String message="";
    String[] arr1,arr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);

        Bundle bundle=getIntent().getExtras();
        number=bundle.getString("number");

        db= FirebaseDatabase.getInstance().getReference().child("Users").child(number);
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bio=snapshot.getValue(BasicInformationObject.class);
                name=bio.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(valueEventListener);

    }

    public void uploadStatus(View v)
    {
        statusMessage=findViewById(R.id.status_message);
        message=statusMessage.getText().toString().trim();
        if(message.length()<1)
        {
            Toast.makeText(this,"Empty status message ! \uD83D\uDE1E ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(" dd.MM.yyyy , hh:mm aa", Locale.getDefault());
            String currentDateAndTime = sdf.format(currentTime);

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Status");

            StatusDetails statusDetails=new StatusDetails(name,message,number,"Uploaded on "+currentDateAndTime);
            databaseReference.child(number).setValue(statusDetails);
            Toast.makeText(this,"Status uploaded \uD83D\uDE03 ",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void deleteStatus(View v)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Status").child(number);
        databaseReference.getRef().removeValue();
        Toast.makeText(this,"Status deleted successfully.",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void backs(View v)
    {
        finish();
    }
}