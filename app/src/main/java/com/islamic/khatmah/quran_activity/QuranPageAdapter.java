package com.islamic.khatmah.quran_activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.islamic.khatmah.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuranPageAdapter extends RecyclerView.Adapter<QuranPageAdapter.PageViewHolder>{

    private ViewPager2 viewPager2;
    private ArrayList<String> page_url;

    QuranPageAdapter(ArrayList<String> page_url, ViewPager2 viewPager2) {
        this.page_url = page_url;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.page_container,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.setUrl(page_url.get(position));
    }

    @Override
    public int getItemCount() {
        return page_url.size();
    }



    class PageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String url;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_container);
        }

        void setUrl(String url){
            this.url = url;
            Picasso.get().load(url).into(imageView);
        }
    }
}
