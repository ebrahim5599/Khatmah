package com.islamic.khatmah.free_reading;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;


import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder>{
    private Context context;
    private List<Surah> list;

    public SurahAdapter(Context context, List<Surah> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SurahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.surah_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahAdapter.ViewHolder holder, int position) {
        holder.surahNo.setText(String.valueOf(list.get(position).getNumber()));
        holder.arabicName.setText(list.get(position).getName());
        holder.englishName.setText(list.get(position).getEnglishName());
        holder.totalAya.setText("Aya : "+String.valueOf(list.get(position).getNumberOfAyahs()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView surahNo,arabicName,englishName,totalAya;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahNo = itemView.findViewById(R.id.surah_number);
            arabicName = itemView.findViewById(R.id.arabic_name);
            englishName = itemView.findViewById(R.id.english_name);
            totalAya = itemView.findViewById(R.id.total_aya);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SurahDetailActivity.class);
//                    intent.putExtra("position",getAdapterPosition()+1);
//                    context.startActivity(intent);
//                }
//            });

        }
    }
}
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.islamic.khatmah.R;
//
//import java.util.ArrayList;
//
//public class SurahAdapter extends ArrayAdapter {
//    public SurahAdapter(@NonNull Context context, ArrayList<SurahClass> surah) {
//        super(context, 0, surah);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.surah_details_container,
//                    parent,false);
//        }
//
//        SurahClass surah = (SurahClass) getItem(position);
//        TextView number = convertView.findViewById(R.id.number);
//        number.setText(surah.getNumber());
//
//        TextView name = convertView.findViewById(R.id.name);
//        name.setText(surah.getName());
//
//        TextView type = convertView.findViewById(R.id.type);
//        type.setText(surah.getType());
//
//        TextView number_of_verses = convertView.findViewById(R.id.number_of_verses);
//        number_of_verses.setText(surah.getNumber_of_verses());
//
//        return convertView;
//    }
//}
