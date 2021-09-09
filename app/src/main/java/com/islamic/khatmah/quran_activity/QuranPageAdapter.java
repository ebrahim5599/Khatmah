package com.islamic.khatmah.quran_activity;

import static com.islamic.khatmah.MainActivity.fileNotFound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.islamic.khatmah.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class QuranPageAdapter extends RecyclerView.Adapter<QuranPageAdapter.PageViewHolder>{


    private ViewPager2 viewPager2;
    private Context context;
    private Bitmap bit;

    QuranPageAdapter(Context context, ViewPager2 viewPager2) {

        this.context = context;
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
                Bitmap bit = BitmapFactory.decodeStream(is);
                holder.setBitmap(bit);
            } catch (FileNotFoundException e) {
                fileNotFound = true;
                holder.setUrl("https://quran-images-api.herokuapp.com/show/page/"+(position+1));
            }

    }

    @Override
    public int getItemCount() {
        return 604;
    }



    class PageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private Bitmap bitmap;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_container);
        }

        void setUrl(String url){
            Picasso.get().load(url).into(imageView);
        }

        void setBitmap(Bitmap bitmap){
            this.bitmap = bitmap;
            imageView.setImageBitmap(bitmap);
        }

    }
}
