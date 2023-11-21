package ddwu.mobile.network.myretrofittest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ddwu.mobile.network.myretrofittest.model.json.BoxOfficeRoot;
import ddwu.mobile.network.myretrofittest.model.json.DailyBoxOffice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    private Retrofit retrofit;
    private IKobisAPIService kobisAPIService;

    EditText editText;
    TextView tvResult;

    String apiUrl;
    String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.etDate);
        tvResult = findViewById(R.id.tvResult);

        apiUrl = getResources().getString(R.string.api_url);
        apiKey = getResources().getString(R.string.kobis_key);

        if (retrofit == null){
            try{
                retrofit = new Retrofit.Builder()
                        .baseUrl(apiUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        kobisAPIService = retrofit.create(IKobisAPIService.class);
    }


    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.button:
                String targetDate = editText.getText().toString();
                Call<BoxOfficeRoot> apiCall = kobisAPIService.getDailyBoxOfficeResult("json", apiKey, targetDate);
                apiCall.enqueue(apiCallback);

                break;
        }
    }

    Callback<BoxOfficeRoot> apiCallback = new Callback<BoxOfficeRoot>() {

        @Override
        public void onResponse(Call<BoxOfficeRoot> call, Response<BoxOfficeRoot> response) { //call -> 요청 정보, response -> 실제 응답 결과
            if(response.isSuccessful()) {
                BoxOfficeRoot boxOfficeRoot = response.body();
                List<DailyBoxOffice> list = boxOfficeRoot.getBoxOfficeResult().getDailyBoxOfficeList();  //영화정보 list 꺼냄

                for(DailyBoxOffice dbo : list) {
                    Log.d(TAG, dbo.toString());
                }
            }
        }

        @Override
        public void onFailure(Call<BoxOfficeRoot> call, Throwable t) {
            //서버 응답 이상
            Log.e(TAG, t.toString());
        }
    };
}