package com.example.couponzy.login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.couponzy.Model.FireBaseAuth;

import com.example.couponzy.MainActivity;
import com.example.couponzy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Login_form extends AppCompatActivity {
    Button Login,Register;
    EditText txtemail,txtpassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

         Login=findViewById(R.id.Login_form_login_btn);
         Register=findViewById(R.id.Login_form_regieter);
         progressBar=findViewById(R.id.activity_login_form_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
         txtemail=findViewById(R.id.Login_form_email);
         txtpassword=findViewById(R.id.Login_form_password);

         Register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getApplicationContext(), Register_form.class));
             }
         });
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email= txtemail.getText().toString().trim();
                    String password=txtpassword.getText().toString().trim();

                    if(TextUtils.isEmpty((email))){
                        Toast.makeText(Login_form.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    }
                    if(TextUtils.isEmpty((password))){
                        Toast.makeText(Login_form.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    }

                    if(!TextUtils.isEmpty((email)) &&!TextUtils.isEmpty((password))) {
                        progressBar.setVisibility(View.VISIBLE);
                        FireBaseAuth.instance.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login_form.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Login_form.this, "Login Failed or User was not Found!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

    }
}