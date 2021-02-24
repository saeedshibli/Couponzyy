package com.example.couponzy;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.login_Register.Login_form;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView email,name;
    MenuItem Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FireBaseAuth.instance.signOut();
                startActivity(new Intent(getApplicationContext(), Login_form.class));
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_Logout)
                .setOpenableLayout(drawer)
                .build();
        /*Adding Custom Email and Username to HeaderPanel*/
        View headerView = navigationView.getHeaderView(0);
        String CurrentEmail = FireBaseAuth.instance.getCurrentUser().getEmail();
        String CurrentName= FireBaseAuth.instance.getCurrentUser().getDisplayName();
        email=(TextView) headerView.findViewById(R.id.nav_head_main_email);
        email.setText(CurrentEmail);
        name=(TextView)headerView.findViewById(R.id.nav_header_main_name);
        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*String Firstname=snapshot.child("firstname").getValue().toString();
                String Lastname=snapshot.child("lastname").getValue().toString();
                name.setText("Welcome "+ Firstname+" "+Lastname);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //TODO : ADD USER'S IMAGE
        /**/
        /*Adding Logout */
        navigationView.getMenu().findItem(R.id.nav_Logout).setOnMenuItemClickListener(menuItem -> {
            FireBaseAuth.instance.signOut();
            startActivity(new Intent(getApplicationContext(), Login_form.class));
            return true;
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}