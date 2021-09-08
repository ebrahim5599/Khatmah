package com.islamic.khatmah.free_reading;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;
import com.islamic.khatmah.quran_activity.QuranActivity;


import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {
    private Context context;
    private List<Surah> list;
    private boolean mScrollable;
    ///
    private boolean isSelected;
    private final static int FADE_DURATION = 500;// milliseconds
    private int lastPosition = -1;
    Context cont;
    private String[] strname;
    private int[] icon;
    private int selectedPosition = -1;

    ////
    public SurahAdapter(Context context, List<Surah> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SurahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.surah_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahAdapter.ViewHolder holder, int position) {
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(Color.GRAY);
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }
        holder.surahNo.setText(String.valueOf(list.get(position).getNumber()));
        holder.arabicName.setText(list.get(position).getName());
        holder.englishName.setText(list.get(position).getEnglishName());
        holder.totalAya.setText("Aya : " + String.valueOf(list.get(position).getNumberOfAyahs()));
        holder.page_number = list.get(position).getPage_number();
        setAnimation(holder.cardView, position);
    }
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //animation 1
            AlphaAnimation anim = new AlphaAnimation(0.75f, 1.0f);
            anim.setDuration(FADE_DURATION);
            viewToAnimate.startAnimation(anim);

            //animation 2
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int page_number;
        private TextView surahNo, arabicName, englishName, totalAya;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahNo = itemView.findViewById(R.id.surah_number);
            arabicName = itemView.findViewById(R.id.arabic_name);
            englishName = itemView.findViewById(R.id.english_name);
            totalAya = itemView.findViewById(R.id.total_aya);
            cardView = (CardView) itemView.findViewById(R.id.surah_rv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, QuranActivity.class);
                    intent.putExtra("PAGE_NUMBER",page_number);
                    context.startActivity(intent);
                }
            });

        }
    }

    // TODO: this method converts English numbers to Indian number [Arabic].
        private String ArbNum(int number) {

        String stNum = String.valueOf(number);
        String result ="";

        for (int i = 0; i < stNum.length(); i++) {
            char num = String.valueOf(stNum).charAt(i);
            int ArabicNum = num + 1584;
            result += (char) ArabicNum;
        }
        return result;
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
