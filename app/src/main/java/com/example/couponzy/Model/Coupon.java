package com.example.couponzy.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


@Entity
public class Coupon {
    @PrimaryKey
    @NonNull
    public String id;
    public String userName, userId, timestamp, profileImg, postImg, expireDate, couponCode, description, title, distance;
    public double price, discountPrice;
    private Long lastUpdated;

    /*public Coupon(String userName, String userId, String timestamp, String profileImg, String postImg, String expireDate, String couponCode, String description, String title, double price, double discountPrice) {
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
        this.lastUpdated= 0L;
    }*/

    /*public Coupon() {
        final int min = 90000;
        final int max = 99999;
        final int minu = 900000000;
        final int maxu = 999999999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final int random2 = new Random().nextInt((maxu - minu) + 1) + minu;
        this.couponCode =Integer.toString(random);
        this.id=Integer.toString(random2);
    }*/

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("userName", userName);
        result.put("userId", userId);
        result.put("timestamp", timestamp);
        result.put("profileImg", profileImg);
        result.put("postImg", postImg);
        result.put("expireDate", expireDate);
        result.put("couponCode", couponCode);
        result.put("description", description);
        result.put("title", title);
        result.put("distance", distance);
        result.put("price", price);
        result.put("discountPrice", discountPrice);
        result.put("lastUpdated", ServerValue.TIMESTAMP);
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        id = (String) map.get("id");
        userName = (String) map.get("userName");
        userId = (String) map.get("userId");
        timestamp = (String) map.get("timestamp");
        profileImg = (String) map.get("profileImg");
        postImg = (String) map.get("postImg");
        expireDate = (String) map.get("expireDate");
        couponCode = (String) map.get("couponCode");
        description = (String) map.get("description");
        title = (String) map.get("title");
        distance = (String) map.get("distance");
        price = (Long) map.get("price");
        discountPrice = (Long) map.get("discountPrice");
        lastUpdated = (Long) map.get("lastUpdated");
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
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
                String img = snapshot.child("imgURL").getValue().toString();
                profileImg = img;
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
