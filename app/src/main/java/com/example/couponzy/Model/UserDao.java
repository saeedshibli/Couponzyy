package com.example.couponzy.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface UserDao {

    @Query("select * from User where isShop=1")
    List<User> getAllSellers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);
}
