package mobile.example.dbtest;

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
public interface ContactDao {
    //Room
//    @Insert
//    long insertContacts(Contact contacts);
//
//    @Update
//    long updateContact(Contact contact);
//
//    @Delete
//    void deleteContact(Contact contact);
//
//    @Query("SELECT * FROM contact_table")
//    List<Contact> getAllContacts();
//
//    @Query("SELECT * FROM contact_table WHERE id = :id")
//    Contact getContact(int id);

    //비동기 RxJava
    @Query("SELECT * FROM contact_table")
    Flowable<List<Contact>> getAllContacts();

    @Insert
    Single<Long> insertContact(Contact contact);

    @Update
    Completable updateContact(Contact contact);

    @Delete
    Completable deleteContact(Contact contact);

    @Query("SELECT * FROM contact_table WHERE phone = :phone")
    Flowable<List<Contact>> getContactByPhone(String phone);
}
