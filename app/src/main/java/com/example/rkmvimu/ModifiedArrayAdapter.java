package com.example.rkmvimu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ModifiedArrayAdapter extends ArrayAdapter<StatusDetails>
{

    StatusDetails statusDetails;



    public ModifiedArrayAdapter(@NonNull Context context, int resource, @NonNull List<StatusDetails> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        statusDetails=getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.individual_status, parent, false);
        }

        TextView t1=convertView.findViewById(R.id.status_by);
        TextView t2=convertView.findViewById(R.id.upload_details);
        TextView t3=convertView.findViewById(R.id.status_message);

        t1.setText(statusDetails.getStatusBy());
        t2.setText(statusDetails.getDateTime());
        if(statusDetails.getStatus().length() > 400 )
        {
          t3.setText(statusDetails.getStatus().substring(0,400)+" . . .  ");
        }
        else {
            t3.setText(statusDetails.getStatus());
        }

        return convertView;
    }
}
