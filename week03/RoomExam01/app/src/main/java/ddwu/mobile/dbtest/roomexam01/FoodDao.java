package ddwu.mobile.dbtest.roomexam01;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FoodDao {
    //Room
//    @Insert
//    long insertFoods(Food foods);
//
//    @Update
//    long updateFood(Food food);
//
//    @Delete
//    void deleteFood(Food food);
//
//    @Query("SELECT * FROM food_table")
//    List<Food> getAllFoods();
//
//    @Query("SELECT * FROM food_table WHERE id = :id")
//    Food getFood(int id);

    //비동기 RxJava
    @Query("SELECT * FROM food_table")
    Flowable<List<Food>> getAllFoods();

    @Insert
    Single<Long> insertFood(Food food);

    @Update
    Completable updateFood(Food food);

    @Delete
    Completable deleteFood(Food food);

    @Query("SELECT * FROM food_table WHERE nation = :nation")
    Flowable<List<Food>> getFoodByNation(String nation);

    @Query("SELECT * FROM food_table WHERE id = :id")
    Single<Food> getFood(int id);   //반환값 필요
}
