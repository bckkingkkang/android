package com.example.step04httprequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        EditText inputUrl=findViewById(R.id.inputUrl);
        //요청 버튼을 클릭했을때 동작할 준비
        Button requestBtn=findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(view -> {
            //1. 입력한 url 주소를 읽어와서
            String url=inputUrl.getText().toString();
            //2. http 요청을 하고
            new RequestTask().execute(url); //이 메소드는 호출 즉시 리턴된다.
        });
    }
    //비동기 Task 객체를 생성할 클래스
    class RequestTask extends AsyncTask<String, Void, String>{

        //백그라운드(새로운 스레드의 run() 메소드) 에서 작업할 내용을 이 메소드 에서 작업하면된다.
        @Override
        protected String doInBackground(String... strings) {
            //문자열을 누적시킬 객체
            StringBuilder builder=new StringBuilder();
            //strings 의 0 번방에 요청 url 이 들어 있다.
            try {
                //요청 url 을 생성자의 인자로 전달해서 URL 객체를 생성한다.
                URL url = new URL(strings[0]);
                //URLConnection 객체를 원래 type (자식 type) 으로 casting 해서 받는다.
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                //정상적으로 연결이 되었다면
                if(conn != null){
                    Log.d("####", "ok");
                    conn.setConnectTimeout(20000); //응답을 기다리는 최대 대기 시간
                    conn.setRequestMethod("GET");// 요청 메소드 설정 (Default 는 GET)
                    conn.setUseCaches(false);//케쉬 사용 여부
                    //응답 코드를 읽어온다.
                    int responseCode=conn.getResponseCode();
                    Log.d("####", "code:"+responseCode);
                    if(responseCode == HttpURLConnection.HTTP_OK){ //정상 응답이라면
                        //문자열을 읽어들일수 있는 객체의 참조값 얻어오기
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        //반복문 돌면서
                        while(true){
                            //문자열을 한줄씩 읽어 들인다.
                            String line=br.readLine();
                            if(line==null)break;
                            //StringBuilder 객체에 읽어들인 문자열을 누적 시킨다.
                            builder.append(line);
                        }
                    }
                }
            }catch (Exception e){
                Log.e("RequestTask 클래스", e.getMessage());
            }
            //StringBuilder 객체에 누적된 문자열을 String type 으로 한번에 얻어내서 리턴해 준다.
            return builder.toString();
        }
        //doInBackground() 메소드에서 리턴된 문자열이 이메소드가 호출되면서 메소드의 인자로 전달된다.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //여기는 UI 스레드 상에서 동작하는 메소드 이다.
            editText.setText(s);
        }
    }
}