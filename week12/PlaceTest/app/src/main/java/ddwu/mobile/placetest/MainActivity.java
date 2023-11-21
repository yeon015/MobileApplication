package ddwu.mobile.placetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import com.google.android.libraries.places.api.model.PlaceTypes;

import ddwu.mobile.place.placebasic.OnPlaceBasicResult;
import ddwu.mobile.place.placebasic.PlaceBasicManager;
import ddwu.mobile.place.placebasic.pojo.PlaceBasic;

public class MainActivity extends AppCompatActivity {
    private PlaceBasicManager placeBasicManager;
    final String TAG = "PlaceBasicTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placeBasicManager = new PlaceBasicManager(getString(R.string.google_api_key));
        placeBasicManager.setOnPlaceBasicResult(new OnPlaceBasicResult() {
            @Override
            public void onPlaceBasicResult(List<PlaceBasic> list) {
                for (PlaceBasic place : list) {
                    Log.d(TAG, place.toString());
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                placeBasicManager.searchPlaceBasic(37.604094, 127.042463, 100, PlaceTypes.CAFE);


                break;
            case R.id.detail_btn:



                break;
        }
    }
}