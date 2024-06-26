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
    private final static int FADE_DURATION = 500;// milliseconds
    private int lastPosition = -1;
    private int selectedPosition = -1;
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

        holder.surahNo.setText(ArbNum(list.get(position).getNumber()));
        holder.arabicName.setText(list.get(position).getName());
        holder.englishName.setText(list.get(position).getEnglishName());
        holder.totalAya.setText("عدد الآيات: "+ convertToArbNum(list.get(position).getNumberOfAyahs()));
        holder.page_number = list.get(position).getPage_number();
        holder.begin_at.setText("صفحة: "+convertToArbNum(list.get(position).getPage_number()));
        if(list.get(position).getRevelationType().equals("Meccan"))
            holder.place.setText("مكية");
        else
            holder.place.setText("مدنية");

        setAnimation(holder.cardView, position);
    }
    // this method converts English numbers to Indian number [Arabic].
    private String convertToArbNum(int number) {

        String stNum = String.valueOf(number);
        String result = "";

        for (int i = 0; i < stNum.length(); i++) {
            char num = stNum.charAt(i);
            int ArabicNum = num + 1584;
            result += (char) ArabicNum;
        }
        return result;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //animation 1
            AlphaAnimation anim = new AlphaAnimation(0.75f, 1.0f);
            anim.setDuration(FADE_DURATION);
            viewToAnimate.startAnimation(anim);

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
        private TextView surahNo, arabicName, englishName, totalAya, begin_at, place;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahNo = itemView.findViewById(R.id.surah_number);
            arabicName = itemView.findViewById(R.id.arabic_name);
            englishName = itemView.findViewById(R.id.english_name);
            totalAya = itemView.findViewById(R.id.total_aya);
            begin_at = itemView.findViewById(R.id.begin_at);
            place = itemView.findViewById(R.id.place);
            cardView = (CardView) itemView.findViewById(R.id.surah_rv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, QuranActivity.class);
                    intent.putExtra("PAGE_NUMBER",page_number);
                    intent.putExtra("POSITION", selectedPosition);
                    context.startActivity(intent);
                }
            });
        }
    }

    // converts English numbers to Indian number [Arabic].
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