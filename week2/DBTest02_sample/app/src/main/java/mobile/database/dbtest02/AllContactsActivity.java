package mobile.database.dbtest02;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class AllContactsActivity extends AppCompatActivity {
	
	ListView lvContacts = null;
	ContactDBHelper helper;
	Cursor cursor;
//	SimpleCursorAdapter adapter;
	MyCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
//        adapter = new SimpleCursorAdapter ( /* 매개변수 설정*/ );
		//list_item_1
//		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
//				new String[] {"name"}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		//list_item_2
//		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
//				new String[] {"name", "phone"}, new int[] {android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		//커스텀 cursorAdapter
		adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);

		lvContacts.setAdapter(adapter);

//		리스트 뷰 클릭 처리
//        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//           return true;
//            }
//        });

////		리스트 뷰 롱클릭 처리
//		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//				final long dbId = id;  //이걸 dialog에서 사용
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
//
//				builder.setTitle("delete")
//						.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialogInterface, int i) {
//								String whereClause = "_id=?";
//								String[] whereArgs = new String[] { String.valueOf(dbId) };
//
//								db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);
//							}
//						});
//
//
//				return true;
//			}
//		});


	}

	@Override
	protected void onResume() {
		super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        helper.close();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//        cursor 사용 종료
		if (cursor != null) cursor.close();
	}

}




