package com.example.rkmvimu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.rkmvimu.ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int RC_PHOTO_PICKER =  2;
    ProgressBar progressBar;


    private StorageReference storageReference;

    Cursor c;
    String number;
    BasicInformationObject bio;

    TextView t1,t2,t3,t4,t5;


    Uri u;
    ImageView i;

    String name="",num="",email="",department="",role="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView img;
    Uri image;
    ImageView dp;
    Button status;

    ProgressBar imageLoad;


    private static final int PICK_IMAGE = 100;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.rkmvimu.ProfileFragment newInstance(String param1, String param2) {
        com.example.rkmvimu.ProfileFragment fragment = new com.example.rkmvimu.ProfileFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar=view.findViewById(R.id.getting_info);
        progressBar.setVisibility(View.VISIBLE);

        imageLoad=view.findViewById(R.id.image_load);
        imageLoad.setVisibility(View.VISIBLE);


/**Here lies the code for reading data from local databse ,then go to firebase for that number and then load all information onto the screen*/
        i=view.findViewById(R.id.image_profile);
        t1=view.findViewById(R.id.name_profile);
        t2=view.findViewById(R.id.number_profile);
        t3=view.findViewById(R.id.email_profile);
        t4=view.findViewById(R.id.dept_profile);
        t5=view.findViewById(R.id.role_profile);
        status=view.findViewById(R.id.add_status);

        ActualJob aj=new ActualJob(getContext());
        SQLiteDatabase read=aj.getReadableDatabase();
        String[] pro={BasicInfo.EntryColumns.COLUMN_NAME_MOBILE};
        try {
            c=read.query(
              BasicInfo.EntryColumns.TABLE_NAME,
              pro,
              null,
              null,
              null,
              null,
                    null
            );
        }catch (Exception e){}
        int x = c.getColumnIndex(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE);
        if(c.moveToLast())
        {
            number=c.getString(x);
        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), com.example.rkmvimu.UploadStatus.class);
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });

        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("Users").child(number);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bio=snapshot.getValue(BasicInformationObject.class);

                loadImage();

                name=bio.getName();
                if(name.length()>22)
                {
                    t1.setText(""+name.substring(0,22)+"...");
                }
                else {
                    t1.setText("" + name);
                }

                num=bio.getNumber();
                if(num.length()>13)
                {
                    t2.setText("Invalid number...");
                }
                else {
                    t2.setText("" + num);
                }

                email=bio.getEmail();
                if(email.length()>35)
                {
                    t3.setText(""+email.substring(0,35)+"...");
                }
                else {
                    t3.setText("" + email);
                }

                department=bio.getDept();
                if(department.length()>35)
                {
                    t4.setText(""+department.substring(0,35)+"...");
                }
                else {
                    t4.setText("" + department);
                }

                role=bio.getRole();
                if(role.length()>22)
                {
                    t5.setText(""+role.substring(0,22)+"...");
                }
                else
                {
                    t5.setText(""+role);
                }


                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(valueEventListener);
/**Here lies the code for logging out of the app and deleting the SQLiteDatabase from local memory.*/
        final SQLiteDatabase dlt=aj.getWritableDatabase();
        TextView tv=view.findViewById(R.id.logout);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d=dlt.delete(BasicInfo.EntryColumns.TABLE_NAME,null,null);


                MailActualJob mailActualJob=new MailActualJob(getContext());
                SQLiteDatabase sqLiteDatabase=mailActualJob.getWritableDatabase();
                int dl=sqLiteDatabase.delete(MailBasicColumns.EntryColumns.TABLE_NAME,null,null);

                Toast.makeText(getContext(),"Logged out successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), com.example.rkmvimu.LoginActivity.class));
            }
        });

 /**Here lies the code for editing the information entered by the user.*/
        TextView et=view.findViewById(R.id.edit_info);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),EditInfoActivity.class);
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });


/** Here lies the code to pick an image from Gallery and set the ImageView ,later to update it in the database */
        img = (ImageView)view.findViewById(R.id.image_profile);
        dp=view.findViewById(R.id.change_dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_PHOTO_PICKER ){

            /*image = data.getData();
            storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(number).child("profile_photo.jpg");
            storageReference.putFile(image);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(getContext(),"Uploading...",Toast.LENGTH_SHORT).show();
                }
            });*/

/** Compression of image procedure starts */
            image=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);

                storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(number).child("profile_photo.jpg");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);
                byte[] datax = baos.toByteArray();
                Toast.makeText(getContext(),"Uploading...",Toast.LENGTH_SHORT);

                UploadTask uploadTask2 = storageReference.putBytes(datax);
                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                        System.out.println(e+"+++++++++++++++++++++++++++++++++++++++");

                    }
                });
            } catch (IOException e) { }
/** Compression of image procedure ends. */





        }
        else {
            Toast.makeText(getContext()," Not uploaded ! ",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage()
    {
        storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(number).child("profile_photo.jpg");
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                i.setImageBitmap(bmp);
                imageLoad.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                    imageLoad.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}