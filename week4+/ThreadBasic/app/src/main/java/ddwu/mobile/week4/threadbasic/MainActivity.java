package ddwu.mobile.week4.threadbasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
//                TestThread t = new TestThread();
                //핸들러 사용. 쓰레드 객체 생성 및 핸들러 객체 전달
                TestThread t = new TestThread(handler);
                t.start();
                etText.setText("Thread start!");
                Toast.makeText(this, "Running!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //핸들러 생성(선언). 메세지를 받고 수신. handler.sendMessage()를 통해 받음
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {  //i값을 받음
            int i = msg.arg1;   //i값 꺼냄
            //핸들러에 전달 받은 Message 사용
            etText.setText("i: " + i); //main 쓰레드이기 때문에 etText에 넣어도 문제되지 않음!
        }
    };



    class TestThread extends Thread {
        //핸들러 보관하기 위한 매개변수
        Handler handler;

        //전달받은 핸들러 보관
        public TestThread(Handler handelr){  //생성자에게 전달됨
            this.handler = handelr;
       }
        @Override
        public void run() {
            for (int i=0; i < 100; i++) {
//                Log.d(TAG, "i: " + i );  //editText 사용 불가능(값 받아오는 것). 메인 쓰레드의 UI항목을 다른 쓰레드에서 직접 접근할 수 없움.
                
                //값 보내기. 쓰레드 작업 실행
                Message msg = Message.obtain();
                msg.arg1 = i;    //작업 결과를 Message에 저장. 현재 i값 저장   arg1은 내장된 변수임.
                handler.sendMessage(msg);  //handler에게 i값을 담은 메세지를 전달  이를 통해 Handler안에 있는 handlerMessage가 호출됨.
                
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

