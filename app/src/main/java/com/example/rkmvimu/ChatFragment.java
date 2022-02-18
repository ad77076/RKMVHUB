package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.data.BasicInformationObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.rkmvimu.ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    ProgressBar progressBar;
    Cursor c;
    String myNum="";

    BasicInformationObject basicInformationObject;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.rkmvimu.ChatFragment newInstance(String param1, String param2) {
        com.example.rkmvimu.ChatFragment fragment = new com.example.rkmvimu.ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView=view.findViewById(R.id.search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });

        ActualJob con=new ActualJob(getContext());
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


        progressBar=view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        listView=view.findViewById(R.id.chat_list);
        ArrayList<com.example.rkmvimu.MessageBody> arrayList=new ArrayList<>();
        final com.example.rkmvimu.MessageAdapter messageAdapter=new com.example.rkmvimu.MessageAdapter(getContext(),0,arrayList);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                com.example.rkmvimu.MessageBody messageBody=messageAdapter.getItem(position);
                String u=messageBody.getUser();
                String n=messageBody.getNum();
                String d=messageBody.getDept();
                String role=messageBody.getRole();
                String email=messageBody.getEmail();

                Intent intent=new Intent(getActivity(),FullChat.class);
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

                //System.out.println(snapshot.getValue(BasicInformationObject.class).getName()+"-----------------------------------------------------------------------");
                basicInformationObject=snapshot.getValue(BasicInformationObject.class);

                if(basicInformationObject!=null) {

                    String name = basicInformationObject.getName();
                    String num = basicInformationObject.getNumber();

                    if (num.equals(myNum)) {
                        System.out.println();
                    } else {
                        String dept = basicInformationObject.getDept();
                        String role = basicInformationObject.getRole();
                        String email = basicInformationObject.getEmail();
                        String online = basicInformationObject.isOnline();
                        messageAdapter.add(new com.example.rkmvimu.MessageBody(name, num, dept, role, email, online));
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.fragment_chat, container, false);

        return v;
    }

    public void searchUser()
    {
        startActivity(new Intent(getContext(), com.example.rkmvimu.SearchUser.class));
    }

}