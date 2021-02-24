package com.example.couponzy.Model;

import androidx.room.Entity;

import java.util.ArrayList;

@Entity
public class User {
    public String email;
    public String firstname;
    public String lastname;
    public String id;
    public String dateOfBirth;
    public String imgURL;
    public String gender;
    public String phone;



    public ArrayList<String>Posts=null;

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


    public User(String email, String firstname, String lastname, String id, String dateOfBirth, String gender, String phone,String imgURL) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.imgURL=imgURL;
        Posts=null;
    }
}
