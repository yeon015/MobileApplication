package ddwu.mobile.lecture.etc.myretrofittest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.lecture.etc.myretrofittest.model.json.Book;
import ddwu.mobile.lecture.etc.myretrofittest.model.json.BookRoot;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText editText;
    ListView listView;
    ImageView imageView;

    String naverId;
    String naverSecret;
    ArrayAdapter<Book> adapter;

    List<Book> books;

    INaverAPIService naverAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.etQuery);
        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.imageView);

        books = new ArrayList<Book>();
        adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, books);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        naverId = getResources().getString(R.string.client_id);
        naverSecret = getResources().getString(R.string.client_secret);

        //try-catch 묶어 줘야함
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")  //안에 주소 string에 넣어서 변수를 써야하는데 일단 주소만 넣음
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        naverAPIService = retrofit.create(INaverAPIService.class);
    }



    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                Call<BookRoot> apiCall = naverAPIService.getBookList(naverId, naverSecret, "안드로이드");
                apiCall.enqueue(apiCallback);
                break;
            case R.id.btnSearch:
                adapter.clear();
                String query = editText.getText().toString();


                break;
        }
    }

    Callback<BookRoot> apiCallback = new Callback<BookRoot>(){

        @Override
        public void onResponse(Call<BookRoot> call, Response<BookRoot> response) {
            BookRoot bookRoot = response.body();
            List <Book> items = bookRoot.getItems();

            for(Book book : items){
                Log.d(TAG, book.toString());
            }
        }

        @Override
        public void onFailure(Call<BookRoot> call, Throwable t) {

        }
    };


}
