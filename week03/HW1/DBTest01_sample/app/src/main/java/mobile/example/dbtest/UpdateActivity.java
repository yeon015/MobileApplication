package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateActivity extends Activity {
    final static String TAG = "UpdateActivity";

    EditText etUpdateName;
    EditText etUpdatePhone;
    EditText etUpdateCategory;

    ContactDB contactDB;
    ContactDao contactDao;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    Contact updateContact = new Contact();
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etUpdateName = findViewById(R.id.etUpdateName);
        etUpdatePhone = findViewById(R.id.etUpdatePhone);
        etUpdateCategory = findViewById(R.id.etUpdateCategory);

//        updateContact = (Contact) getIntent().getSerializableExtra("contact");

        Intent intent = getIntent();
        String name = intent.getStringExtra("contactName");
        String phone = intent.getStringExtra("contactPhone");
        String category = intent.getStringExtra("contactCategory");
        index = intent.getIntExtra("index", 0);

        contactDB = ContactDB.getDatabase(getApplicationContext());
        contactDao = contactDB.contactDao();

        etUpdateName.setText(name);
        etUpdatePhone.setText(phone);
        etUpdateCategory.setText(category);
    }


    //화면 나오기 전에 실행됨. all에서 가져온 값 반환
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btnUpdateContact:
                String contactName = etUpdateName.getText().toString();
                String contactPhone = etUpdatePhone.getText().toString();
                String contactCategory = etUpdateCategory.getText().toString();

                updateContact.setName(contactName);
                updateContact.setPhone(contactPhone);
                updateContact.setCategory(contactCategory);

                Completable updateResult = contactDao.updateContact(updateContact);
                updateResult
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Log.d(TAG, "Update success: "),
                        throwable -> Log.d(TAG, "error"));
                setResult(RESULT_OK);
                break;
            case R.id.btnUpdateContactClose:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}
