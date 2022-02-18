package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.data.BasicInformationObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchUser extends AppCompatActivity {

    ListView listView;
    Cursor c;
    String myNum;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Button button=findViewById(R.id.search_for);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listUsers();
            }
        });
    }


    public void listUsers()
    {
        EditText editText=findViewById(R.id.name_search);
        final String nameUser=editText.getText().toString().trim().toLowerCase();

        progressBar=findViewById(R.id.userWait);
        progressBar.setVisibility(View.VISIBLE);

        ActualJob con=new ActualJob(this);
        SQLiteDatabase help=con.getReadableDatabase();

        String[] pro={BasicInfo.EntryColumns.COLUMN_NAME_MOBILE};
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
            if (c.moveToLast()) {
                myNum = c.getString(x);
            }
        }finally {
            c.close();
        }


        listView=findViewById(R.id.search_list);
        ArrayList<MessageBody> arrayList=new ArrayList<>();
        final MessageAdapter messageAdapter=new MessageAdapter(this,0,arrayList);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MessageBody messageBody=messageAdapter.getItem(position);
                String u=messageBody.getUser();
                String n=messageBody.getNum();
                String d=messageBody.getDept();
                String role=messageBody.getRole();
                String email=messageBody.getEmail();

                Intent intent=new Intent(getApplicationContext(),FullChat.class);
                intent.putExtra("user",u);
                intent.putExtra("number",n);
                intent.putExtra("dept",d);
                intent.putExtra("role",role);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });




        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BasicInformationObject basicInformationObject=snapshot.getValue(BasicInformationObject.class);
                String name=basicInformationObject.getName();
                String num=basicInformationObject.getNumber();

                if(num.equals(myNum))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if(name.toLowerCase().startsWith(nameUser)) {
                    String dept = basicInformationObject.getDept();
                    String role = basicInformationObject.getRole();
                    String email = basicInformationObject.getEmail();
                    String online=basicInformationObject.isOnline();
                    messageAdapter.add(new MessageBody(name, num, dept, role, email, online));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BasicInformationObject basicInformationObject=snapshot.getValue(BasicInformationObject.class);
                String name=basicInformationObject.getName();
                String num=basicInformationObject.getNumber();
                if(num.equals(myNum))
                {
                    System.out.println();
                }
                else if(name.toLowerCase().startsWith(nameUser))  {
                    String dept = basicInformationObject.getDept();
                    String role = basicInformationObject.getRole();
                    String email = basicInformationObject.getEmail();
                    String online=basicInformationObject.isOnline();
                    messageAdapter.add(new MessageBody(name, num, dept, role, email, online));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void back(View v)
    {
        finish();
    }
}