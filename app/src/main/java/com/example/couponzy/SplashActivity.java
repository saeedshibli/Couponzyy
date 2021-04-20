package com.example.couponzy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.login_Register.Login_form;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    int progress=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splash_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(2400, 20) {

            public void onTick(long millisUntilFinished) {
                progress=progress+1;
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                startApp();
            }

        }.start();
    }

    private void startApp() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {
                if(FireBaseAuth.instance.getCurrentUser()==null){
                    startActivity(new Intent(SplashActivity.this, Login_form.class));
                    //overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                progressBar.setVisibility(View.INVISIBLE);
                //overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                finish();
            }
        });
    }
}