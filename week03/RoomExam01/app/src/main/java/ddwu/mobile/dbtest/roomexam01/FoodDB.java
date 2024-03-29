package ddwu.mobile.dbtest.roomexam01;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version=1)
public abstract class FoodDB extends RoomDatabase {
    public abstract FoodDao foodDao();

    //Singleton 하나의 객체만 생성 유지.
    private static volatile FoodDB INSTANCE;

    static FoodDB getDatabase(final Context context) {
        if (INSTANCE == null) {   //만들어진 객체 있는지 검사. 없으면
            synchronized (FoodDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodDB.class, "food_db.db").build();
                }
            }
        }
        return INSTANCE;  //있으면 있는거 반환
    }
}
