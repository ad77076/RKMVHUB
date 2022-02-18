package com.example.rkmvimu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.mails.MailActualJob;
import com.example.rkmvimu.mails.MailBasicColumns;
import com.example.rkmvimu.mails.Vmail;
import com.example.rkmvimu.mails.VmailArrayAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.rkmvimu.EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Cursor c;
    String myNum = "";
    String s1, s2, s3;
    int x = 0, y = 0, z = 0;
    View l;
    String s="";

    ArrayAdapter<Vmail> arrayAdapter;

    String toAddress = "";

    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.rkmvimu.EmailFragment newInstance(String param1, String param2) {
        com.example.rkmvimu.EmailFragment fragment = new com.example.rkmvimu.EmailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.mail_loading);
        progressBar.setVisibility(View.VISIBLE);


        MailActualJob dbHelper = new MailActualJob(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER,
                MailBasicColumns.EntryColumns.COLUMN_NAME_TIME,
                MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT,
                MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT,
                MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL,
                MailBasicColumns.EntryColumns.COLUMN_NAME_READ,
                MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM
        };

        Cursor cursor = database.query(
                MailBasicColumns.EntryColumns.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        ListView listView = view.findViewById(R.id.vmail_list);
        ArrayList<Vmail> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String m = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL));
            String r = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_READ));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT));
            String f = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER));
            String time = cursor.getString((cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_TIME)));
            String s = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT));
            String sNum=cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM));


            Vmail vmail = new Vmail(f,sNum, t, s, m, time, r, "1");
            arrayList.add(0,vmail);
            progressBar.setVisibility(View.INVISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
        cursor.close();

        arrayAdapter = new VmailArrayAdapter(getContext(), 0, arrayList);
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Vmail vmail=arrayAdapter.getItem(position);
                String f=vmail.getFrom();
                String fnum=vmail.getFrom_num();
                String sub=vmail.getSubject();
                String mail=vmail.getMail();
                String time=vmail.getTime();


                Intent intent=new Intent(getActivity(),DetailedMail.class);
                intent.putExtra("from",f);
                intent.putExtra("fnum",fnum);
                intent.putExtra("sub",sub);
                intent.putExtra("mail",mail);
                intent.putExtra("time",time);

                startActivity(intent);
            }
        });


        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pullToRefresh.setRefreshing(true);

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
                        s = c.getString(x);
                    }
                }finally {
                    c.close();
                }

                toAddress="to"+s;
                DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference().child("Mails").child(toAddress);
                databaseReference2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Vmail vmail=snapshot.getValue(Vmail.class);
                        MailActualJob dbHelper=new MailActualJob(getActivity());
                        SQLiteDatabase database=dbHelper.getWritableDatabase();

                        if(vmail.getSent().equals("0") && vmail.getTo().equals(s)) {

                            ContentValues values = new ContentValues();
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_TIME, vmail.getTime());
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER, vmail.getFrom());
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT, vmail.getTo());
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT, vmail.getSubject());
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL, vmail.getMail());
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_READ, "0");
                            values.put(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM,vmail.getFrom_num());

                            long newRowId = database.insert(MailBasicColumns.EntryColumns.TABLE_NAME, null, values);
                            if(newRowId!=0) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Mails").child(toAddress);
                                databaseReference.child(vmail.getTime()).child("sent").setValue("1");
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

                MailActualJob dbHelper = new MailActualJob(getContext());
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                String[] projection = {
                        MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_TIME,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_READ,
                        MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM
                };

                Cursor cursor = database.query(
                        MailBasicColumns.EntryColumns.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                ListView listView = view.findViewById(R.id.vmail_list);
                ArrayList<Vmail> arrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    String m = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL));
                    String r = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_READ));
                    String t = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT));
                    String f = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER));
                    String time = cursor.getString((cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_TIME)));
                    String s = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT));
                    String sNum=cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM));


                    Vmail vmail = new Vmail(f,sNum, t, s, m, time, r, "1");

                    arrayList.add(0,vmail);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
                cursor.close();

                arrayAdapter = new VmailArrayAdapter(getContext(), 0, arrayList);
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Vmail vmail=arrayAdapter.getItem(position);
                        String f=vmail.getFrom();
                        String fnum=vmail.getFrom_num();
                        String sub=vmail.getSubject();
                        String mail=vmail.getMail();
                        String time=vmail.getTime();


                        Intent intent=new Intent(getActivity(),DetailedMail.class);
                        intent.putExtra("from",f);
                        intent.putExtra("fnum",fnum);
                        intent.putExtra("sub",sub);
                        intent.putExtra("mail",mail);
                        intent.putExtra("time",time);

                        startActivity(intent);
                    }
                });


                pullToRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();


        MailActualJob dbHelper = new MailActualJob(getActivity());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER,
                MailBasicColumns.EntryColumns.COLUMN_NAME_TIME,
                MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT,
                MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT,
                MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL,
                MailBasicColumns.EntryColumns.COLUMN_NAME_READ,
                MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM
        };

        Cursor cursor = database.query(
                MailBasicColumns.EntryColumns.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        ListView listView = getView().findViewById(R.id.vmail_list);
        ArrayList<Vmail> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String m = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MAIL));
            String r = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_READ));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_MEANT));
            String f = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER));
            String time = cursor.getString((cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_TIME)));
            String s = cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SUBJECT));
            String sNum=cursor.getString(cursor.getColumnIndexOrThrow(MailBasicColumns.EntryColumns.COLUMN_NAME_SENDER_NUM));


            Vmail vmail = new Vmail(f,sNum, t, s, m, time, r, "1");
            arrayList.add(0,vmail);
        }
        cursor.close();

        final ArrayAdapter<Vmail> arrayAdapter = new VmailArrayAdapter(getActivity(), 0, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Vmail vmail=arrayAdapter.getItem(position);
                String f=vmail.getFrom();
                String fnum=vmail.getFrom_num();
                String sub=vmail.getSubject();
                String mail=vmail.getMail();
                String time=vmail.getTime();


                Intent intent=new Intent(getContext(),DetailedMail.class);
                intent.putExtra("from",f);
                intent.putExtra("fnum",fnum);
                intent.putExtra("sub",sub);
                intent.putExtra("mail",mail);
                intent.putExtra("time",time);

                startActivity(intent);
            }
        });

    }
}