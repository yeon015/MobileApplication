package mobile.database.dbtest02;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AllContactsActivity extends AppCompatActivity {

	final static String TAG = "AllContactsActivity";
	final static int REQ_CODE = 100;
	
	ListView lvContacts = null;
	ContactDBHelper helper;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	MyCursorAdapter myCursorAdapter;

	boolean isUpdated = true;		// update 수행 결과 기록

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
        adapter = new SimpleCursorAdapter ( this, android.R.layout.simple_list_item_2, null,
					new String[] { ContactDBHelper.COL_NAME, ContactDBHelper.COL_PHONE },
					new int[] { android.R.id.text1, android.R.id.text2 },
					CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );

		myCursorAdapter = new MyCursorAdapter(this, R.layout.listview_layout, null);

		lvContacts.setAdapter(myCursorAdapter);

//		리스트 뷰 클릭 처리
		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String sid = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_ID));
				Toast.makeText(AllContactsActivity.this, "id " + sid, Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(AllContactsActivity.this, UpdateActivity.class);
				intent.putExtra("id", id);
//				startActivity(intent);
				isUpdated = true;
				startActivityForResult(intent, REQ_CODE);
			}
		});


//		리스트 뷰 롱클릭 처리
		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				final long targetId = id;	// id 값을 다이얼로그 객체 내부에서 사용하기 위하여 상수로 선언
				TextView tvName = view.findViewById(R.id.tvContactName);	// 리스트 뷰의 클릭한 위치에 있는 뷰 확인

				String dialogMessage = "'" + tvName.getText().toString() + "' 연락처 삭제?";	// 클릭한 위치의 뷰에서 문자열 값 확인

				new AlertDialog.Builder(AllContactsActivity.this).setTitle(R.string.title_dialog)
						.setMessage(dialogMessage)
						.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {

//							삭제 수행
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SQLiteDatabase db = helper.getWritableDatabase();

								String whereClause = ContactDBHelper.COL_ID + "=?";
								String[] whereArgs = new String[] { String.valueOf(targetId) };

								db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);
								helper.close();
								readAllContacts();		// 삭제 상태를 반영하기 위하여 전체 목록을 다시 읽음
							}
						})
						.setNegativeButton(R.string.cancel_dialog, null)
						.show();

				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isUpdated) {
			readAllContacts();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//        cursor 사용 종료
		if (cursor != null) cursor.close();
	}


	/*onActivityResult() 는 onResume 보다 먼저 수행됨.
	 UpdateActivity 의 결과에 따라 readAllContacts() 수행 여부 결정 */
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

	private void readAllContacts() {
//        DB에서 데이터를 읽어와 Adapter에 설정 -> notifysetdataChanged 대신하는거!
		SQLiteDatabase db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

		myCursorAdapter.changeCursor(cursor);
		helper.close();
	}

}




