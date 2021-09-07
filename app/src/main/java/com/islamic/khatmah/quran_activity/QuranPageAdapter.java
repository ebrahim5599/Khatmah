package com.islamic.khatmah.quran_activity;

import static com.islamic.khatmah.MainActivity.bitmaps;
import static com.islamic.khatmah.MainActivity.fileNotFound;
import static com.islamic.khatmah.MainActivity.pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.islamic.khatmah.MainActivity;
import com.islamic.khatmah.R;
import com.islamic.khatmah.free_reading.FreeReadingFragment;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class QuranPageAdapter extends RecyclerView.Adapter<QuranPageAdapter.PageViewHolder>{

    private ViewPager2 viewPager2;
    private ArrayList<String> page_url;
    private ArrayList<Bitmap> bitmap;
    private Context context;
    private Bitmap bit;
    private String url;

    QuranPageAdapter(String url, ViewPager2 viewPager2) {
        this.url = url;
        this.viewPager2 = viewPager2;
    }

    QuranPageAdapter(Context context, ViewPager2 viewPager2) {
        this.context = context;
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

            InputStream is = null;
            try {
                is = context.openFileInput(String.valueOf(position+1));
                bit = BitmapFactory.decodeStream(is);
                holder.setBitmap(bit);
            } catch (FileNotFoundException e) {
                fileNotFound = true;
                holder.setUrl(url+position);
            }

    }

    @Override
    public int getItemCount() {
        return 604;
    }



    class PageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String url;
        private Bitmap bitmap;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_container);
        }

        void setUrl(String url){
            this.url = url;
            Picasso.get().load(url).into(imageView);
        }

        void setBitmap(Bitmap bitmap){
            this.bitmap = bitmap;
            imageView.setImageBitmap(bitmap);
        }

    }
}
