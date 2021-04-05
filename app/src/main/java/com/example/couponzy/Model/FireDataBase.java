package com.example.couponzy.Model;

import com.google.firebase.database.FirebaseDatabase;

public class FireDataBase {


    public final static FirebaseDatabase instance = FirebaseDatabase.getInstance();

    private FireDataBase() {
    }
}
