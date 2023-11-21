package mobile.example.dbtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AllContactsActivity extends Activity {

	final static String TAG = "AllContactsActivity";
	final static int REQ_CODE = 100;
	private ListView lvContacts = null;

	ArrayAdapter<Contact> adapter;
//	private ContactDBHelper helper;
//	private ArrayList<ContactDto> contactList;

	ContactDB contactDB;
	ContactDao contactDao;

	boolean isUpdated = true;

	private final CompositeDisposable mDisposable = new CompositeDisposable();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);

//		helper = new ContactDBHelper(this);
//		contactList = new ArrayList<ContactDto>();

		lvContacts = (ListView)findViewById(R.id.lvContacts);
		adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, new ArrayList<Contact>());

		lvContacts.setAdapter(adapter);

		contactDB = ContactDB.getDatabase(getApplicationContext());
		contactDao = contactDB.contactDao();


		Flowable<List<Contact>> resultContacts = contactDao.getAllContacts();

		mDisposable.add(
				resultContacts.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(contacts -> {
									for (Contact aContact : contacts){
										Log.d(TAG, aContact.toString());
										Log.d(TAG, "id: " + lvContacts.getAdapter());
									}

									adapter.clear();
									adapter.addAll(contacts);
								},
								throwable -> Log.d(TAG, "error", throwable))    );

		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				Contact contact = (Contact) lvContacts.getAdapter().getItem(i);
				Log.d(TAG, "id: " + lvContacts.getAdapter().getItem(i));
				Completable deleteResult = contactDao.deleteContact(contact);
				mDisposable.add(
						deleteResult.subscribeOn(Schedulers.io())
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(
										() -> Log.d(TAG, "deletion success"),
										throwable -> Log.d(TAG, "error")
								)
				);
				return false;
			}
		});

		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Contact contact = (Contact) lvContacts.getAdapter().getItem(i);

				Intent intent = new Intent(AllContactsActivity.this, UpdateActivity.class);

				intent.putExtra("index", i);
				intent.putExtra("contactName", contact.getName());
				intent.putExtra("contactPhone", contact.getPhone());
				intent.putExtra("contactCategory", contact.getCategory());


				//startActivityForResult(intent, REQ_CODE);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDisposable.clear();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch(requestCode) {
			case RESULT_OK:
				isUpdated = true;		// update 를 수행하였을 경우
				break;
			case RESULT_CANCELED:
				isUpdated = false;		// update 를 취소하였을 경우
				break;
		}
	}


}




