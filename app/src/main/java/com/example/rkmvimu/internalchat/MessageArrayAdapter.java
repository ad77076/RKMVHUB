package com.example.rkmvimu.internalchat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.rkmvimu.R;

import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter<MessageDetails>
{

    public MessageArrayAdapter(@NonNull Context context, int resource, @NonNull List<MessageDetails> objects)
    {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        MessageDetails messageDetails=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.display_message, parent, false);
        }

        TextView t1=convertView.findViewById(R.id.time);
        TextView t2=convertView.findViewById(R.id.message_to_display);
        String time=messageDetails.getTime();
        String message=messageDetails.getMessage();
        t1.setText(time);

        if(message.length()> 200)
        {
            t2.setText(message.substring(0,200)+" . . . ");
        }

        else {
            t2.setText(message);
        }
        if(messageDetails.getFlag().equals("0"))
        {
            t1.setGravity(Gravity.RIGHT);
            t1.setPadding(5,2,20,0);
            t1.setBackgroundResource(R.color.sent);
            t1.setTextColor(ContextCompat.getColor(getContext(),R.color.sent1));
            t2.setGravity(Gravity.RIGHT);
            t2.setPadding(5,2,20,5);
            t2.setTextColor(ContextCompat.getColor(getContext(),R.color.sent2));
            t2.setBackgroundResource(R.color.sent);
        }
        else if(messageDetails.getFlag().equals("1"))
        {
            t1.setGravity(Gravity.LEFT);
            t1.setPadding(20,2,5,0);
            t1.setTextColor(ContextCompat.getColor(getContext(),R.color.receive1));
            t1.setBackgroundResource(R.color.receive);
            t2.setGravity(Gravity.LEFT);
            t2.setPadding(20,2,5,5);
            t2.setTextColor(ContextCompat.getColor(getContext(),R.color.receive2));
            t2.setBackgroundResource(R.color.receive);
        }

        return convertView;
    }
}
