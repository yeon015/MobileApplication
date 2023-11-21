package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InsertContactActivity extends Activity {

	final static String TAG = "InsertContactActivity";

	ContactDBHelper helper;
	EditText etName;
	EditText etPhone;
	EditText etCategory;

	ContactDB contactDB;
	ContactDao contactDao;

	private final CompositeDisposable mDisposable = new CompositeDisposable();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

//      DBHelper 생성
//		helper = new ContactDBHelper(this);
		
		etName = (EditText)findViewById(R.id.editText1);
		etPhone = (EditText)findViewById(R.id.editText2);
		etCategory = (EditText)findViewById(R.id.editText3);

		contactDB = ContactDB.getDatabase(getApplicationContext());
		contactDao = contactDB.contactDao();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDisposable.clear();
	}


	public void onClick(View v) {
		if(v.getId() == R.id.btnNewContactSave) {
			final String name = etName.getText().toString();
			final String phone = etPhone.getText().toString();
			final String category = etCategory.getText().toString();

			//비동기
			Single<Long> insertResult = contactDao.insertContact(new Contact(name, phone, category));

			mDisposable.add(
					insertResult.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(result -> Log.d(TAG, "Insertion success: " + result),
									throwable -> Log.d(TAG, "error")
							));
		}
		finish();
	}
	

}
