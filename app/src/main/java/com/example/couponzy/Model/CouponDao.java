package com.example.couponzy.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CouponDao {

    @Query("select * from Coupon")
    List<Coupon> getAllPosts();

    @Query("select * from Coupon WHERE userId!=:userId")
    LiveData<List<Coupon>> getCoupons(String userId);

    @Query("select * from Coupon WHERE userId=:userId")
    LiveData<List<Coupon>> getMyCoupons(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Coupon... posts);

    @Delete
    void delete(Coupon post);
}
