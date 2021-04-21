package com.example.couponzy.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String email;
    public String firstname;
    public String lastname;
    public String id;
    public String dateOfBirth;
    public String imgURL;
    public String gender;
    public String phone;
    public double lat;
    public double lon;
    public boolean isAdmin;
    public boolean isShop;
    public boolean isUser;
    private Long lastUpdated;


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }



    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdateOfBirth() {
        return dateOfBirth;
    }

    public void setdateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


    public User(String email, String firstname, String lastname, String id, String dateOfBirth, String gender, String phone, String imgURL, double lat, double lon, boolean isAdmin, boolean isShop, boolean isUser) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.imgURL = imgURL;
        this.isAdmin = isAdmin;
        this.isUser = isUser;
        this.isShop = isShop;
        this.lat=lat;
        this.lon=lon;
    }

    public User(){

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("dateOfBirth", dateOfBirth);
        result.put("gender", gender);
        result.put("phone", phone);
        result.put("imgURL", imgURL);
        result.put("isAdmin", isAdmin);
        result.put("isUser", isUser);
        result.put("isShop", isShop);
        result.put("lastUpdated", ServerValue.TIMESTAMP);
        result.put("lat", lat);
        result.put("lon", lon);
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        id = (String) map.get("id");
        email = (String) map.get("email");
        firstname = (String) map.get("firstname");
        lastname = (String) map.get("lastname");
        dateOfBirth = (String) map.get("dateOfBirth");
        gender = (String) map.get("gender");
        phone = (String) map.get("phone");
        imgURL = (String) map.get("imgURL");
        isAdmin = (boolean) map.get("isAdmin");
        isUser = (boolean) map.get("isUser");
        isShop = (boolean) map.get("isShop");
        lastUpdated = (Long) map.get("lastUpdated");
        lat = Double.parseDouble( map.get("lat").toString());
        lon = Double.parseDouble( map.get("lon").toString());
    }


}