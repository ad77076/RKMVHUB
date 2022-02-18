package com.example.rkmvimu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.BasicInformationObject;
import com.example.rkmvimu.mails.MailActualJob;
import com.example.rkmvimu.mails.MailBasicColumns;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailedMail extends AppCompatActivity {

    String f="",fnum="",sub="",mail="",time="";
    String d="",role="",email="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_mail);

        Bundle bundle=getIntent().getExtras();
        f=bundle.getString("from");
        fnum=bundle.getString("fnum");
        sub=bundle.getString("sub");
        mail=bundle.getString("mail");
        time=bundle.getString("time");

        TextView t1=findViewById(R.id.sub_detailed);
        t1.setText(sub);

        TextView t2=findViewById(R.id.from_name);
        t2.setText(f);

        TextView t3=findViewById(R.id.time_sent);
        t3.setText(time);

        TextView t4=findViewById(R.id.mail_body);
        t4.setText(mail);


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(fnum);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BasicInformationObject basicInformationObject=snapshot.getValue(BasicInformationObject.class);
                d=basicInformationObject.getDept();
                role=basicInformationObject.getRole();
                email=basicInformationObject.getEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MailActualJob mailActualJob=new MailActualJob(getApplicationContext());
        SQLiteDatabase sqLiteDatabase=mailActualJob.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(MailBasicColumns.EntryColumns.COLUMN_NAME_READ,"1");

        sqLiteDatabase.update(MailBasicColumns.EntryColumns.TABLE_NAME,contentValues,"time = ?",new String[]{time});

        final ImageView imageView=findViewById(R.id.sender_image);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(fnum).child("profile_photo.jpg");
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }

    public void back(View v)
    {
        finish();
    }

    public void makeCall(View view)
    {
        Uri u = Uri.parse("tel:" + fnum);
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        startActivity(i);
    }

    public void makeReply(View v)
    {
        Intent intent=new Intent(getApplicationContext(),FullChat.class);
        intent.putExtra("user",f);
        intent.putExtra("number",fnum);
        intent.putExtra("dept",d);
        intent.putExtra("role",role);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}