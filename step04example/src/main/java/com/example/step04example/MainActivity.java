package com.example.step04example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/*
    문자열을 입력하고 "전송" 버튼을 누르면  Util 클래스를 이용해서
    GET 방식으로 http://아이피주소:9000/boot07/android/tweet 에 요청을 하는예제
    요청 파라미터는 msg 라는 파라미터 명으로 입력한 문자열이 전송되도록 한다.
    서버가 응답하는 문자열은 오렌지색 EditText 에 출력하기
 */
public class MainActivity extends AppCompatActivity implements Util.RequestListener{
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // activity_main.xml 문서에 정의된 객체의 참조값 얻어오기
        editText=(EditText) findViewById(R.id.editText);
        EditText inputMsg=(EditText) findViewById(R.id.inputMsg);
        Button sendBtn=(Button) findViewById(R.id.sendBtn);
        // 버튼에 리스너 등록하기
        sendBtn.setOnClickListener(view->{
            //입력한 문자열
            String msg=inputMsg.getText().toString();
            //요청 파라미터로 전달하기 위해 "msg" 라는 키값으로 Map 에 담는다.
            Map<String, String> map=new HashMap<>();
            map.put("msg", msg);
            //Util 을 이용해서 http 요청을 한다.
            Util.sendGetRequest(0,
                    "http://192.168.0.31:9000/boot07/android/tweet",
                    map,
                    this);
        });
    }

    @Override
    public void onSuccess(int requestId, Map<String, Object> result) {
        if(requestId == 0){
            String data=(String)result.get("data");
            editText.setText(data);
        }
    }

    @Override
    public void onFail(int requestId, Map<String, Object> result) {
        if(requestId == 0){
            String data=(String)result.get("data");
            editText.setText(data);
        }
    }
}