package mobile.example.dbtest;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version=1)
public abstract class ContactDB extends RoomDatabase {
    public abstract ContactDao contactDao();

    //Singleton 하나의 객체만 생성 유지.
    private static volatile ContactDB INSTANCE;

    static ContactDB getDatabase(final Context context) {
        if (INSTANCE == null) {   //만들어진 객체 있는지 검사. 없으면
            synchronized (ContactDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactDB.class, "contact_db.db").build();
                }
            }
        }
        return INSTANCE;  //있으면 있는거 반환
    }
}
