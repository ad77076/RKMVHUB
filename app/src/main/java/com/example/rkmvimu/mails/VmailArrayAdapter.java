package com.example.rkmvimu.mails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rkmvimu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class VmailArrayAdapter extends ArrayAdapter<Vmail> {

    Vmail vmail;
    String v="";


    public VmailArrayAdapter(@NonNull Context context, int resource, @NonNull List<Vmail> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        vmail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vmail_view, parent, false);
        }

        final ImageView imageView=convertView.findViewById(R.id.sender_image);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(vmail.getFrom_num()).child("profile_photo.jpg");
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

        TextView t1=convertView.findViewById(R.id.fromMail);
        if(vmail.getFrom().length()>22)
        {
            t1.setText(vmail.getFrom().substring(0,22)+"...");
        }
        else {
            t1.setText(vmail.getFrom());
        }

        TextView t2=convertView.findViewById(R.id.timeMail);
        t2.setText(vmail.getTime());

        TextView t3=convertView.findViewById(R.id.subjectMail);
        if(vmail.getSubject().length()>37)
        {
            t3.setText("Sub: "+vmail.getSubject().substring(0,37)+"...");
        }
        else {
            t3.setText("Sub: "+vmail.getSubject());
        }

        TextView t4=convertView.findViewById(R.id.mailMail);
        if(vmail.getMail().length()>37)
        {
            t4.setText("Mail: "+vmail.getMail().substring(0,37)+"...");
        }
        else {
            t4.setText("Mail: "+vmail.getMail());
        }

        String ru=vmail.getRead();
        TextView textView=convertView.findViewById(R.id.read_unread);

        if(ru.equals("0"))
        {
            textView.setText("Unread");

            /*t1.setTypeface(null,Typeface.BOLD);
            t1.setTextColor(Color.parseColor("#fafafa"));

            t3.setTypeface(null,Typeface.BOLD);
            t3.setTextColor(Color.parseColor("#fafafa"));

            t4.setTypeface(null,Typeface.BOLD);
            t4.setTextColor(Color.parseColor("#fafafa"));

            if(vmail.getFrom().length()>22)
            {
                t1.setText(vmail.getFrom().substring(0,22)+"..."+" ( Unread )");
            }
            else {
                t1.setText(vmail.getFrom()+" ( Unread )");
            }*/
        }
        else
        {
            textView.setText("");
        }


        return convertView;
    }
}