package ddwu.mobile.placetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import ddwu.mobile.place.placebasic.OnPlaceBasicResult;
import ddwu.mobile.place.placebasic.PlaceBasicManager;
import ddwu.mobile.place.placebasic.pojo.PlaceBasic;

public class MainActivity extends AppCompatActivity {
    private PlaceBasicManager placeBasicManager;
    final String TAG = "PlaceBasicTest";

    private PlacesClient placesClient;

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

        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        placesClient = Places.createClient(this);
    }

    public void onClick(View v) {
        List<Place.Field>placeFields = Arrays.asList(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.PHONE_NUMBER, Place.Field.ADDRESS,
                Place.Field.BUSINESS_STATUS
        );

//       FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();  //placeId 바꿔가면서 넣어줘야하는데 여기서는 임시방편으로 옛날다방(아무거나 하나) 넣겠음
        FetchPlaceRequest request = FetchPlaceRequest.builder("CHIJSR6zW367fDUR8H11kzBSJdw", placeFields).build();

        switch (v.getId()) {
            case R.id.search_btn:
                placeBasicManager.searchPlaceBasic(37.604094, 127.042463, 100, PlaceTypes.CAFE);
                break;
            case R.id.detail_btn:
                placesClient.fetchPlace(request)
                        .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                            @Override
                            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                                Place place = fetchPlaceResponse.getPlace();
                                Log.d(TAG, place.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    Log.e(TAG, apiException.toString());
                                }
                            }
                        });


                break;
        }
    }
}