package com.islamic.khatmah.free_reading;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FreeReadingFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Surah> list;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.free_reading_fragment, container, false);

        recyclerView = view.findViewById(R.id.surahRV);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JsonDataFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject surahData = jsonArray.getJSONObject(i);
                list.add(new Surah(surahData.getInt("number"),
                        surahData.getString("name"),
                        surahData.getString("englishName"),
                        surahData.getString("englishNameTranslation"),
                        surahData.getInt("numberOfAyahs"),
                        surahData.getString("revelationType"),
                        surahData.getInt("start")
                ));
            }
            if (list.size() != 0) {
                SurahAdapter surahAdapter = new SurahAdapter(getContext(), list);
                recyclerView.setAdapter(surahAdapter);
                surahAdapter.notifyDataSetChanged();
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return view;
    }

    private String JsonDataFromAsset() {
        String json;
        try {
            InputStream inputStream = requireActivity().getAssets().open("surah.json");
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
}
