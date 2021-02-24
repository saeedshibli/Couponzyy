package com.example.couponzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.R;
import com.example.couponzy.login_Register.Login_form;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FireBaseAuth.instance.getCurrentUser()==null){
                    startActivity(new Intent(SplashActivity.this, Login_form.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        },2500);
    }
}