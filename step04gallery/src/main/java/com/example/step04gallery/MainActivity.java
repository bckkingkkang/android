package com.example.step04gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Util.RequestListener {

    // 필드
    List<GalleryDto> list;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listview 의 참조값 얻기
        ListView listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new GalleryAdapter(this, R.layout.listview_cell, list);
        listView.setAdapter(adapter);

        Util.sendGetRequest(0,
                "http://192.168.0.67:9000/boot07/android/gallery/list",
                null,
                this);
    }

    @Override
    public void onSuccess(int requestId, Map<String, Object> result) {
        if(requestId == 0) {
            // Map에는 "data"라는 키 값으로 [{}, {}, {}] 형식의 json 문자열이 들어있다.
            String json = (String)result.get("data");
            try {
                JSONArray arr = new JSONArray(json);

                // 반복문 -> JSONObject 객체를 하나씩 얻어낸다.
                for (int i = 0; i < json.length(); i++) {
                    JSONObject tmp = arr.getJSONObject(i);
                    // JSONObject 안에 있는 정보를 추출해서 GalleryDto 에 담는다
                    GalleryDto dto = new GalleryDto();
                    dto.setNum(tmp.getInt("num"));
                    dto.setWriter(tmp.getString("writer"));
                    dto.setCaption(tmp.getString("caption"));
                    String url = "http://192.168.0.67:9000/boot07/android/gallery/images/"+tmp.getString("imagePath");
                    dto.setImagePath(url);
                    // galleryDto 를 list 에 누적시킨다.
                    list.add(dto);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onFail(int requestId, Map<String, Object> result) {

    }
}