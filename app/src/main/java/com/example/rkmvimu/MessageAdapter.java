package com.example.rkmvimu;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<com.example.rkmvimu.MessageBody>
{
    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<com.example.rkmvimu.MessageBody> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        com.example.rkmvimu.MessageBody messageBody=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.message_user, parent, false);
        }

        final ImageView imageView=convertView.findViewById(R.id.image);
        String num=messageBody.getNum();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("photos").child(num).child("profile_photo.jpg");
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

        TextView t1=convertView.findViewById(R.id.name);


        if(messageBody.getUser().length()>20)
        {
            t1.setText(messageBody.getUser().substring(0,20)+" . . . ");
        }
        else {
            t1.setText(messageBody.getUser());
        }

        TextView t3=convertView.findViewById(R.id.dept);
        if(messageBody.getDept().length()>21)
        {
            t3.setText(messageBody.getDept().substring(0,21)+" . . . ");
        }
        else {
            t3.setText(messageBody.getDept());
        }

        ImageView t2=convertView.findViewById(R.id.online);
        if(messageBody.isOnline().equals("1")) {
            t2.setImageResource(R.drawable.ic_vector_name);
        }
        else if(messageBody.isOnline().equals("0")) {
            t2.setImageResource(R.drawable.offline);
        }



        return convertView;
    }
}
