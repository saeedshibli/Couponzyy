package com.example.couponzy.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
        price = Double.parseDouble(map.get("price").toString());
        discountPrice = Double.parseDouble(map.get("discountPrice").toString());
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
                if(snapshot.child("imgURL").getValue()!=null) {
                    String img = snapshot.child("imgURL").getValue().toString();
                    profileImg = img;
                }
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
