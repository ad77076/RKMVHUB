package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.data.BasicInformationObject;
import com.example.rkmvimu.internalchat.DetailedMessage;
import com.example.rkmvimu.internalchat.MessageArrayAdapter;
import com.example.rkmvimu.internalchat.MessageDetails;
import com.example.rkmvimu.mails.Vmail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FullChat extends AppCompatActivity {

    String u,n,d,r,e;
    TextView t1,t2;
    ImageView imageView,mailView,profile;
    Cursor c;
    String myNum="";
    String myName="";
    Button button;
    String msg1="",msg2="";
    int f=0;
    StorageReference storageReference;

    ListView listView;
    ArrayList<MessageDetails> arrayList;
    MessageArrayAdapter messageArrayAdapter;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_chat);

        //progressBar=findViewById(R.id.wait_load);
        //progressBar.setVisibility(View.VISIBLE);



        Bundle bundle=getIntent().getExtras();
        u=bundle.getString("user");
        n=bundle.getString("number");
        d=bundle.getString("dept");
        r=bundle.getString("role");
        e=bundle.getString("email");

        /*DatabaseReference someReference= FirebaseDatabase.getInstance().getReference().child("Users").child(n);
        someReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BasicInformationObject basicInformationObject=snapshot.getValue(BasicInformationObject.class);
                String of=basicInformationObject.isOnline();
                if(of.equals("0"))
                {
                    TextView textView=findViewById(R.id.online_offline);
                    textView.setText("Offline");
                    ImageView imageView=findViewById(R.id.chat_online);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.offline));
                }
                else if(of.equals("1"))
                {
                    TextView textView=findViewById(R.id.online_offline);
                    textView.setText("Online");
                    ImageView imageView=findViewById(R.id.chat_online);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_name));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        profile=findViewById(R.id.profile_image);

        storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(n).child("profile_photo.jpg");
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profile.setImageBitmap(bmp);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        t1=findViewById(R.id.name);
        t1.setOnClickListener(v -> profileCheck());

        if(u.length()>13)
        {
            t1.setText(u.substring(0,13)+" ...");
        }
        else {
            t1.setText(u);
        }

        t2=findViewById(R.id.dept);
        t2.setText(d);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileCheck();
            }
        });

        imageView=findViewById(R.id.ic_call);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + n);
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
            }
        });

        /*mailView=findViewById(R.id.mail);
        mailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailTo();
            }
        });*/



        ActualJob con=new ActualJob(this);
        SQLiteDatabase help=con.getReadableDatabase();

        String[] pro={BasicInfo.EntryColumns.COLUMN_NAME_MOBILE,BasicInfo.EntryColumns.COLUMN_NAME_NAME};
        try {
            c = help.query(
                    BasicInfo.EntryColumns.TABLE_NAME,
                    pro,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            int x = c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE);
            int z=c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_NAME);
            if (c.moveToLast()) {
                myNum = c.getString(x);
                myName=c.getString(z);

            }
        }finally {
            c.close();
        }

        button=findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        msg1="from"+n+"to"+myNum;

        /*listView=findViewById(R.id.message_list);
        arrayList=new ArrayList<>();
        messageArrayAdapter=new MessageArrayAdapter(getApplicationContext(),0,arrayList);
        listView.setAdapter(messageArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDetails messageDetails=(MessageDetails) listView.getItemAtPosition(position);
                String msg=messageDetails.getMessage();
                String from=messageDetails.getFrom();
                String time=messageDetails.getTime();

                Intent intent=new Intent(getApplicationContext(), DetailedMessage.class);
                intent.putExtra("message",msg);
                intent.putExtra("num",from);
                intent.putExtra("from",u);
                intent.putExtra("time",time);
                intent.putExtra("role",r);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Message").child(msg1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MessageDetails messageDetails=snapshot.getValue(MessageDetails.class);
                if(messageDetails==null)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println();
                }
                else {
                    MessageDetails xyz=new MessageDetails(messageDetails.getFrom(),messageDetails.getToo(),messageDetails.getTime(),messageDetails.getMessage(),"1");
                    final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.ios_notification);
                    mp.start();
                    progressBar.setVisibility(View.INVISIBLE);
                    messageArrayAdapter.add(xyz);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        callMyself();*/
    }

    public void back(View v)
    {
        finish();
    }

    public void profileCheck()
    {
        Intent intent=new Intent(getApplicationContext(), com.example.rkmvimu.CheckProfile.class);
        intent.putExtra("number",n);
        intent.putExtra("name",u);
        intent.putExtra("email",e);
        intent.putExtra("department",d);
        intent.putExtra("role",r);
        startActivity(intent);
    }

    public void mailTo()
    {
        Intent intent=new Intent(getApplicationContext(), com.example.rkmvimu.IndividualEmail.class);
        intent.putExtra("email",e);
        startActivity(intent);
    }

    public void sendMessage()
    {

        final String SENDER_ID_MINE="345138098285";

        f=1;
        String send="from"+myNum+"to"+n;
        EditText editText=findViewById(R.id.text_message);
        EditText editText1=findViewById(R.id.text_subject);
        String subject=editText1.getText().toString().trim();
        String message=editText.getText().toString().trim();
        if(message.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Empty message",Toast.LENGTH_SHORT).show();
        }
        else if(subject.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Empty subject",Toast.LENGTH_SHORT).show();
        }
        else
        {

            FirebaseMessaging fm = FirebaseMessaging.getInstance();
            fm.send(new RemoteMessage.Builder(SENDER_ID_MINE + "@fcm.googleapis.com")
                    .setMessageId(Integer.toString(999))
                    .addData("my_message", message)
                    .addData("my_action","Testing.")
                    .build());



            //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Message").child(send);

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(" dd MM yyyy  hh:mm:ss aa", Locale.getDefault());
            String currentDateAndTime = sdf.format(currentTime);

            /*MessageDetails messageDetails=new MessageDetails(myNum,n,currentDateAndTime,message,"0");
            databaseReference.setValue(messageDetails);
            messageArrayAdapter.add(messageDetails);
            editText.setText("");*/


            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().child("Mails").child("to"+n);
            Vmail vmail=new Vmail(myName,myNum,n,subject,message,currentDateAndTime,"0","0");
            databaseReference1.child(currentDateAndTime).setValue(vmail);

            Toast.makeText(getApplicationContext(),"Mail Sent " + "\uD83D\uDE03",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void callMyself()
    {
        msg2="from"+myNum+"to"+n;
        DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference().child("Message").child(msg2);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MessageDetails messageDetails=snapshot.getValue(MessageDetails.class);
                if(messageDetails==null)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println();
                }
                else
                {
                    if(f==0) {
                        progressBar.setVisibility(View.INVISIBLE);
                        MessageDetails xyz = new MessageDetails(messageDetails.getFrom(), messageDetails.getToo(), messageDetails.getTime(), messageDetails.getMessage(), "0");
                        messageArrayAdapter.add(xyz);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}