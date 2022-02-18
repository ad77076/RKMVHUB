package com.example.rkmvimu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CheckProfile extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5;

    String number="";
    String name="";
    String email="";
    String dept="";
    String role="";

    ImageView imageView;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_profile);

        imageView=findViewById(R.id.image_profile);

        Bundle bundle=getIntent().getExtras();
        number=bundle.getString("number");
        name=bundle.getString("name");
        email=bundle.getString("email");
        dept=bundle.getString("department");
        role=bundle.getString("role");

        t1=findViewById(R.id.name_profile);
        if(name.length()>22)
        {
            t1.setText(""+name.substring(0,22)+"...");
        }
        else {
            t1.setText("" + name);
        }

        t2=findViewById(R.id.number_profile);
        if(number.length()>13)
        {
            t2.setText("Invalid number...");
        }
        else {
            t2.setText("" + number);
        }

        t3=findViewById(R.id.email_profile);
        if(email.length()>35)
        {
            t3.setText(""+email.substring(0,35)+"...");
        }
        else {
            t3.setText("" + email);
        }

        t4=findViewById(R.id.dept_profile);
        if(dept.length()>35)
        {
            t4.setText(""+dept.substring(0,35)+"...");
        }
        else {
            t4.setText("" + dept);
        }

        t5=findViewById(R.id.role_profile);
        if(role.length()>22)
        {
            t5.setText(""+role.substring(0,22)+"...");
        }
        else
        {
            t5.setText(""+role);
        }
        loadImage();
    }

    public void backed(View v)
    {
        finish();
    }

    private void loadImage()
    {
        final ProgressBar progressBar=findViewById(R.id.image_load);
        storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(number).child("profile_photo.jpg");
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"No image available ! ",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}