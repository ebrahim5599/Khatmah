package com.islamic.khatmah.quran_activity;

import static com.islamic.khatmah.MainActivity.fileNotFound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.islamic.khatmah.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class QuranPageAdapter extends RecyclerView.Adapter<QuranPageAdapter.PageViewHolder>{


    private Context context;
    private Bitmap bit;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private int pos;

    QuranPageAdapter(Context context, int pos) {
        this.context = context;
        this.pos = pos;
        try {
            jsonObject = new JSONObject(JsonDataFromAsset("surah.json"));
            jsonArray = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            JSONObject pageData = null;

            try {
                is = context.openFileInput(String.valueOf(position+1));
                Bitmap bit = BitmapFactory.decodeStream(is);
                holder.setBitmap(bit);
            } catch (FileNotFoundException e) {
                fileNotFound = true;
                holder.setUrl("https://quran-images-api.herokuapp.com/show/page/"+(position+1));
            }

            holder.page_number.setText(convertToArbNum(holder.getAdapterPosition()+1));
            holder.juz_number.setText("الجزء "+convertToArbNum((int) Math.min(((holder.getAdapterPosition() - 1) / 20)+1, 30)));

        try {
            pageData = jsonArray.getJSONObject(pos);
            if(holder.getAdapterPosition()+1 >= pageData.getInt("end")){
                pos++;
                pageData = jsonArray.getJSONObject(pos);
                Log.i("test","1st case");
            }
            if(holder.getAdapterPosition()+1 < pageData.getInt("start")){
                pos--;
                pageData = jsonArray.getJSONObject(pos);
                Log.i("test","2nd case");
            }
            holder.surah_name.setText(pageData.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    // this method to fetch surah name from JSON file.
    private String JsonDataFromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public int getItemCount() {
        return 604;
    }



    class PageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private Bitmap bitmap;
        private TextView page_number, juz_number, surah_name;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_container);
            this.page_number = itemView.findViewById(R.id.page_number_textview);
            this.juz_number = itemView.findViewById(R.id.juz_number_textview);
            this.surah_name = itemView.findViewById(R.id.surah_name_textView);
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
