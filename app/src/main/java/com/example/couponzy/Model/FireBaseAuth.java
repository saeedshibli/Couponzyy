package com.example.couponzy.Model;

import com.google.firebase.auth.FirebaseAuth;

public class FireBaseAuth {

    public final static FirebaseAuth instance =FirebaseAuth.getInstance();
    private FireBaseAuth(){}
}
