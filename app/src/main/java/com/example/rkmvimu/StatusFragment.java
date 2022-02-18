package com.example.rkmvimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.rkmvimu.StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    com.example.rkmvimu.ModifiedArrayAdapter modifiedArrayAdapter;

    ArrayList<StatusDetails> arrayList;
    public ListView listView;

    StatusDetails status;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int x;

    ProgressBar progressBar;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.rkmvimu.StatusFragment newInstance(String param1, String param2) {
        com.example.rkmvimu.StatusFragment fragment = new com.example.rkmvimu.StatusFragment();
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
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar=view.findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);


        listView=view.findViewById(R.id.status_list);
        arrayList=new ArrayList<>();



        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Status");
        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                    status=snapshot.getValue(StatusDetails.class);
                if (status != null) {
                    modifiedArrayAdapter.add(status);
                }
                    progressBar.setVisibility(View.INVISIBLE);

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

        modifiedArrayAdapter=new com.example.rkmvimu.ModifiedArrayAdapter(getContext(),0,arrayList);
        listView.setAdapter(modifiedArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatusDetails list=modifiedArrayAdapter.getItem(position);

                Intent intent=new Intent(getActivity(),ClearStatus.class);
                intent.putExtra("from",list.getStatusBy());
                intent.putExtra("time",list.getDateTime());
                intent.putExtra("status",list.getStatus());
                intent.putExtra("number",list.getNum());
                startActivity(intent);
            }
        });


        /*final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Status");
                databaseReference.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                    {
                        status=snapshot.getValue(StatusDetails.class);
                        if (status != null) {
                            modifiedArrayAdapter.add(status);
                        }
                        progressBar.setVisibility(View.INVISIBLE);

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

                modifiedArrayAdapter=new ModifiedArrayAdapter(getContext(),0,arrayList);
                listView.setAdapter(modifiedArrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StatusDetails list=modifiedArrayAdapter.getItem(position);

                        Intent intent=new Intent(getActivity(),ClearStatus.class);
                        intent.putExtra("from",list.getStatusBy());
                        intent.putExtra("time",list.getDateTime());
                        intent.putExtra("status",list.getStatus());
                        intent.putExtra("number",list.getNum());
                        startActivity(intent);
                    }
                });



                pullToRefresh.setRefreshing(false);
            }
        });*/

    }
}