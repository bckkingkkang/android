package com.example.step01activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 카운트를 셀 필드
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clicked(View v) {
        // Toast.makeText(this, "버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();

        // 필드의 값을 1 증가
        count++;
        // 필드의 값을 TextView 에 출력하기
        // res/layout/activity_main.xml 문서를 전개해서 레이아웃을 구성
        // 거기에서 Textview 의 참조값을 얻어와야 한다.

        TextView a = findViewById(R.id.textView2);
        a.setText(Integer.toString(count));
    }
}