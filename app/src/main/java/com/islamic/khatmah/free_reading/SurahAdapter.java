package com.islamic.khatmah.free_reading;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.islamic.khatmah.R;

import java.util.ArrayList;

public class SurahAdapter extends ArrayAdapter {
    public SurahAdapter(@NonNull Context context, ArrayList<SurahClass> surah) {
        super(context, 0, surah);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.surah_details_container,
                    parent,false);
        }

        SurahClass surah = (SurahClass) getItem(position);
        TextView number = convertView.findViewById(R.id.number);
        number.setText(surah.getNumber());

        TextView name = convertView.findViewById(R.id.name);
        name.setText(surah.getName());

        TextView type = convertView.findViewById(R.id.type);
        type.setText(surah.getType());

        TextView number_of_verses = convertView.findViewById(R.id.number_of_verses);
        number_of_verses.setText(surah.getNumber_of_verses());

        return convertView;
    }
}
