package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InsertContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etName;
	EditText etPhone;
	EditText etCategory;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

		//DB에 대한 기본적인 작업
//      DBHelper 생성
		helper = new ContactDBHelper(this);
		
		etName = (EditText)findViewById(R.id.editText1);
		etPhone = (EditText)findViewById(R.id.editText2);
		etCategory = (EditText)findViewById(R.id.editText3);
	}
	
	
	public void onClick(View v) {   //onclick하면 입력한 값들을 db에 넣어주는 작업
		//DB생성(helper를 통해)
		SQLiteDatabase db = helper.getWritableDatabase();

		//메소드 사용 방식
		ContentValues row = new ContentValues();
		row.put(ContactDBHelper.COL_NAME, etName.getText().toString());
		row.put(ContactDBHelper.COL_PHONE, etPhone.getText().toString());
		row.put(ContactDBHelper.COL_CAT, etCategory.getText().toString());

		db.insert(ContactDBHelper.TABLE_NAME, null, row);  //반환값이 0. 반환값이 0보다 크면 정상적으로 반환되었다는 뜻

		//SQL문
		db.execSQL("insert into " + ContactDBHelper.TABLE_NAME + " values ( NULL, '"
				+  etName.getText().toString() + "', '" + etPhone.getText().toString()
				+ "', '" + etCategory.getText().toString() + "');");  //반환값 X. 따라서 잘 실행됐는지 알려면 try-catch문 써야함.

		helper.close();
	}
	

}
