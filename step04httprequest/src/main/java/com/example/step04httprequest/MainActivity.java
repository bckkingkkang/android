package com.example.step04httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 액티비티가 활성화되는 시점에 main thread (UI thread) 가 여기를 실행한다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText.findViewById(R.id.editText);
        // 요청 버튼을 클리했을 때 동작할 준비
        EditText inputUrl = findViewById(R.id.inputUrl);
        Button requestBtn = findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(view -> {
            // 요청 버튼을 누를 때마다 main thread 가 이 부분을 실행

            // 1. 입력한 url 주소를 읽어와서

            // 2. http 요청을 하고
            new RequestTask().execute("http://acornacademy.co.kr");
            // 3. 정상적으로 응답이 되면 응답된 문자열을 EditText 출력하기
        });
    }

    // 비동기 Task 객체를 생성할 클래스
    class RequestTask extends AsyncTask<String, Void, String> {

        // 백그라운드(새로운 thread의 run() 메소드)에서 작업할 내용을 이 메소드에서 작업

        @Override
        protected String doInBackground(String... strings) {
            // 몇 초의 시간을 소비해서 얻어낸 결과라고 가정
            String result = "<div>hello</div>";

            // 이 부분은 UI thread 가 아니기 때문에 UI 업데이트를 할 수 없다.
            editText.setText(result);

            // 결과 리턴
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
                    conn.setConnectTimeout(20000); //응답을 기다리는 최대 대기 시간
                    conn.setRequestMethod("GET");// 요청 메소드 설정 (Default 는 GET)
                    conn.setUseCaches(false);//케쉬 사용 여부
                    //응답 코드를 읽어온다.
                    int responseCode=conn.getResponseCode();
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

        // doInBackground() 메소드에서 리턴된 문자열이 이 메소드가 호출되면서 메소드이 인자로 전달된다.
        @Override
        protected  void onPostExecute(String s) {
            super.onPostExecute(s);
            // 여기는 UI thread 상에서 동작하는 메소드이다.
            editText.setText(s);
        }
    }
}