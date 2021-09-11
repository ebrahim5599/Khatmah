//package com.islamic.khatmah.daily_portion;
//
//import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
//import static com.islamic.khatmah.MainActivity.fileNotFound;
//import static com.islamic.khatmah.MainActivity.sharedPreferences;
//import static com.islamic.khatmah.daily_portion.DailyPortionActivity.pos;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.islamic.khatmah.R;
//import com.islamic.khatmah.quran_activity.QuranPageAdapter;
//import com.squareup.picasso.Picasso;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//
//public class DailyPortionAdapter extends RecyclerView.Adapter<DailyPortionAdapter.PageViewHolder>{
//
//    private ViewPager2 viewPager2;
//    private Context context;
//    private Bitmap bit;
//    private int j;
//    private int number_of_pages;
//    public static int pos;
//
//    public DailyPortionAdapter(Context context,int j, ViewPager2 viewPager2) {
//        this.context = context;
//        this.viewPager2 = viewPager2;
//        this.j = j;
//    }
//
//    @NonNull
//    @Override
//    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new DailyPortionAdapter.PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.daily_portion_container,parent,false
//        ));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
//        InputStream is = null;
//        try {
//            is = context.openFileInput(String.valueOf(position+j));
//            bit = BitmapFactory.decodeStream(is);
//            holder.setBitmap(bit);
//        } catch (FileNotFoundException e) {
//            fileNotFound = true;
//            holder.setUrl("https://quran-images-api.herokuapp.com/show/page/"+(position+j));
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        number_of_pages = sharedPreferences.getInt(PAGES_PER_DAY,1);
//        return number_of_pages;
////        return 604;
//    }
//
//
//
//    class PageViewHolder extends RecyclerView.ViewHolder{
//        private ImageView imageView;
//        private Bitmap bitmap;
//        private LinearLayout layout;
//        private ImageButton check;
//        private boolean checked = false;
//        private TextView counter_text;
//        private int counter = 0;
//
//        public PageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            this.imageView = itemView.findViewById(R.id.img);
//
//            /*
//            layout = itemView.findViewById(R.id.read_linear);
//            counter_text = itemView.findViewById(R.id.counter_text);
//            check = itemView.findViewById(R.id.read);
//            check.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(checked == false){
//                        check.setImageResource(R.drawable.checked);
//                        checked = true;
//                        counter++;
//                        viewPager2.setCurrentItem(getAdapterPosition()+1);
//                        // save number of next page to start from it next time.
////                        editor.putInt(CURRENT_PAGE, getAdapterPosition()+2);
////                        editor.commit();
//                    }else{
//                        check.setImageResource(R.drawable.unchecked);
//                        checked = false;
//                        counter--;
//                    }
//                    counter_text.setText(counter+"/"+number_of_pages);
//                }
//            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                if(layout.getVisibility()==View.GONE){
//                    layout.setVisibility(View.VISIBLE);
//                }else{
//                    layout.setVisibility(View.GONE);
//                }
//
//                }
//            });
//            */
//        }
//
//        void setUrl(String url){
//            Picasso.get().load(url).into(imageView);
////            editor.putInt(CURRENT_PAGE, getAdapterPosition()+j);
////            editor.commit();
//        }
//
//        void setBitmap(Bitmap bitmap){
//            this.bitmap = bitmap;
//            imageView.setImageBitmap(bitmap);
////            pos = getAdapterPosition();
////            editor.putInt(CURRENT_PAGE, getAdapterPosition());
////            editor.commit();
//        }
//    }
//}

package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
import static com.islamic.khatmah.MainActivity.editor;
import static com.islamic.khatmah.MainActivity.fileNotFound;
import static com.islamic.khatmah.MainActivity.sharedPreferences;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islamic.khatmah.R;
import com.islamic.khatmah.quran_activity.QuranPageAdapter;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class DailyPortionAdapter extends RecyclerView.Adapter<DailyPortionAdapter.PageViewHolder>{

    private ViewPager2 viewPager2;
    private Context context;
    private Bitmap bit;
    private int j;
    private int number_of_pages;
    private boolean checked = false;
    private static int counter;

    public DailyPortionAdapter(Context context,int j, ViewPager2 viewPager2) {
        this.context = context;
        this.viewPager2 = viewPager2;
        this.j = j;
        counter = 0;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyPortionAdapter.PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.daily_portion_container,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        InputStream is = null;
        try {
            is = context.openFileInput(String.valueOf(position+j));
            bit = BitmapFactory.decodeStream(is);
            holder.setBitmap(bit);

        } catch (FileNotFoundException e) {
            fileNotFound = true;
            holder.setUrl("https://quran-images-api.herokuapp.com/show/page/"+(position+j));
        }

        // Check button listener.
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checked == false){
                    holder.check.setImageResource(R.drawable.checked);
                    checked = true;
                    holder.counter_text.setText(counter+"");
                    counter++;

                    viewPager2.setCurrentItem(holder.getAdapterPosition()+1);
                    // save number of next page to start from it next time.
//                        editor.putInt(CURRENT_PAGE, getAdapterPosition()+2);
//                        editor.commit();
                }else{
                    holder.check.setImageResource(R.drawable.unchecked);
                    checked = false;
//                    counter--;
                }
                holder.counter_text.setText(counter+"");
            }
        });

        // ItemView listener. [Make layout Visible / Gone]
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.layout.getVisibility()==View.GONE)
                    holder.layout.setVisibility(View.VISIBLE);
                else
                    holder.layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        number_of_pages = sharedPreferences.getInt(PAGES_PER_DAY,1);
        return number_of_pages;
    }



    class PageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private LinearLayout layout;
        private ImageButton check;
        private TextView counter_text;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.read_linear);
            counter_text = itemView.findViewById(R.id.counter_text);
            check = itemView.findViewById(R.id.read);
        }

        void setUrl(String url){
            Picasso.get().load(url).into(imageView);
//            editor.putInt(CURRENT_PAGE, getAdapterPosition()+j);
//            editor.commit();
        }

        void setBitmap(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
//            editor.putInt(CURRENT_PAGE, getAdapterPosition());
//            editor.commit();
        }

    }
}
