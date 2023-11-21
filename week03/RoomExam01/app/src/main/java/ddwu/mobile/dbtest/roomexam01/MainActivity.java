package ddwu.mobile.dbtest.roomexam01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etFood;
    EditText etNation;
    ListView listView;

    ArrayAdapter<Food> adapter;

    //멤버변수
    FoodDB foodDB;
    FoodDao foodDao;

    //CompositeDisposable
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFood = findViewById(R.id.etFood);
        etNation = findViewById(R.id.etNation);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<Food>(this, android.R.layout.simple_list_item_1, new ArrayList<Food>());
        listView.setAdapter(adapter);


//        //Room DB 생성
//        foodDB = Room.databaseBuilder(this, FoodDB.class, "food_db.db").build();
//        foodDao = foodDB.foodDao();

        //Singleton(이거 사용하는 경우 위의 두줄 코드 대신 아래 작성)
        foodDB = FoodDB.getDatabase(this);
        foodDao = foodDB.foodDao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    public void onClick(View v) {

        final String food = etFood.getText().toString();
        final String nation = etNation.getText().toString();

        switch (v.getId()) {
            case R.id.btnInsert:   //데이터 삽입
//                //새로운 스레드
//                new Thread(new Runnable(){
//                    @Override
//                    public void run() {
//                        foodDao.insertFoods(new Food(food, nation));
//                    }
//                }).start();

//                foodDao.insertFoods(new Food(food, nation));   //새로운 스레드 생성해서 위처럼 해야함

                //비동기
                Single<Long> insertResult = foodDao.insertFood(new Food(food, nation));

                mDisposable.add(
                insertResult.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> Log.d(TAG, "Insertion success: " + result),
                                throwable -> Log.d(TAG, "error")
                        )  );
                break;
            case R.id.btnUpdate:

                break;
            case R.id.btnDelete:

                break;
            case R.id.btnShow:  //select
                //새로운 스레드
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<Food> foodList = foodDao.getAllFoods();
//                        for (Food aFood : foodList) {
//                            Log.d(TAG, aFood.toString());
//                        }
//                    }
//                }).start();

                //새로운 스레드 생성해서 필요x
//                List<Food> foodList = foodDao.getAllFoods();
//                for (Food aFood : foodList) {
//                    Log.d(TAG, aFood.toString());
//                }

                //비동기
                Flowable<List<Food>> resultFoods = foodDao.getAllFoods();

                mDisposable.add(
                resultFoods.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(foods -> {   //자동으로 DB 변경된 부분 감지해서 자동으로 수행함. 최신 데이터 가져올 수 있음.
                            for (Food aFood : foods){
                                Log.d(TAG, aFood.toString());
                            }

                            adapter.clear();
                            adapter.addAll(foods);
                        },
                        throwable -> Log.d(TAG, "error", throwable))    );
                break;
        }
    }
}