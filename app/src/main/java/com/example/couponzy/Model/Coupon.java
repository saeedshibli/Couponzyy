package com.example.couponzy.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Coupon {
    public String id;
    public String userName, userId,timestamp,profileImg,postImg,expireDate,couponCode,description,title,distance;
    public double price,discountPrice;




    public Coupon(String userName, String userId, String timestamp, String profileImg, String postImg, String expireDate, String couponCode, String description, String title, double price, double discountPrice) {
        final int minc = 90000;
        final int maxc = 99999;
        final int minu = 900000000;
        final int maxu = 999999999;
        final int random = new Random().nextInt((maxc - minc) + 1) + minc;
        final int random2 = new Random().nextInt((maxu - minu) + 1) + minu;
        this.userName = userName;
        this.userId = userId;
        this.timestamp = timestamp;
        this.profileImg = profileImg;
        this.postImg = postImg;
        this.expireDate = expireDate;
        this.couponCode =Integer.toString(random);
        this.description = description;
        this.title = title;
        this.price = price;
        this.discountPrice=discountPrice;
        this.id=Integer.toString(random2);
    }

    public Coupon() {
        final int min = 90000;
        final int max = 99999;
        final int minu = 900000000;
        final int maxu = 999999999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final int random2 = new Random().nextInt((maxu - minu) + 1) + minu;
        this.couponCode =Integer.toString(random);
        this.id=Integer.toString(random2);
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImg() {
        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img=snapshot.child("imgURL").getValue().toString();
                profileImg=img;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }


    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }
    public String getid() {
        return id;
    }

    public void setid(String ID) {
        this.id = ID;
    }

}
