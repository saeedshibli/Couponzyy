package com.example.couponzy.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.couponzy.MyApplication;

@Database(entities = {Coupon.class, User.class}, version = 12)

abstract class CouponzyLocalDbRepository extends RoomDatabase {
    public abstract CouponDao couponDao();
    public abstract UserDao userDao();
}

public class CouponzyLocalDB {
    static public CouponzyLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    CouponzyLocalDbRepository.class,
                    "dbCouponzyLocal.db")
                    .fallbackToDestructiveMigration()
                    .build();
}


