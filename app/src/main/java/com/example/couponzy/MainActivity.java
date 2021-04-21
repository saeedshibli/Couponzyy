package com.example.couponzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.couponzy.Model.FireBaseAuth;
import com.example.couponzy.Model.FireDataBase;
import com.example.couponzy.login_Register.Login_form;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.google.android.gms.tasks.Tasks.await;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView email,name;
    ImageView userImage;
    MenuItem Logout;
    MenuItem menuMyCoupons;
    Boolean Type;
    int Content,nav;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this changes the side bar to left side
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow, R.id.nav_Logout,R.id.nav_map)
                    .setOpenableLayout(drawer)
                    .build();
        /*Adding Custom Email and Username to HeaderPanel*/
        View headerView = navigationView.getHeaderView(0);
        String CurrentEmail = FireBaseAuth.instance.getCurrentUser().getEmail();
        String CurrentName= FireBaseAuth.instance.getCurrentUser().getDisplayName();
        email=(TextView) headerView.findViewById(R.id.nav_head_main_email);
        email.setText(CurrentEmail);
        name=(TextView)headerView.findViewById(R.id.nav_header_main_name);
        userImage=(ImageView)headerView.findViewById(R.id.imageView_post_userImg);
        FireDataBase.instance.getReference("User").child(FireBaseAuth.instance.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Firstname=snapshot.child("firstname").getValue().toString();
                String Lastname=snapshot.child("lastname").getValue().toString();
                name.setText("ברוך הבא "+ Firstname+" "+Lastname);
                userImage.setImageResource(R.drawable.ic_baseline_person_24);
                if(snapshot.child("imgURL").getValue()!=null) {
                    if (snapshot.child("imgURL").getValue().toString() != null && snapshot.child("imgURL").getValue().toString() != "") {
                        Picasso.get().load(snapshot.child("imgURL").getValue().toString()).placeholder(R.drawable.ic_baseline_person_24).into(userImage);
                    }
                }
                Type=Boolean.parseBoolean(snapshot.child("isUser").getValue().toString());
                if(Type==true){
                    NavigationMenuItemView navigationMenuItemView= findViewById(R.id.nav_home);
                    navigationMenuItemView.setVisibility(headerView.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*Adding Logout */
        navigationView.getMenu().findItem(R.id.nav_Logout).setOnMenuItemClickListener(menuItem -> {
            FireBaseAuth.instance.signOut();
            startActivity(new Intent(getApplicationContext(), Login_form.class));
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            return true;
        });

        /*adding map*/
        navigationView.getMenu().findItem(R.id.nav_map).setOnMenuItemClickListener(menuItem->{
            //Navigation.findNavController(navigationView.getRootView()).navigate(R.id.action_nav_home_to_mapsActivity);
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            return true;
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //setFragment(GalleryFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        //CHECKING WHICH KIND OF USER IS CONNECTED
        String  currentUserId= FireBaseAuth.instance.getCurrentUser().getUid();
        FireDataBase.instance.getReference("User").child(currentUserId).child("isUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    Type = Boolean.parseBoolean(snapshot.getValue().toString());
                    if (Type == true) {
                        navigationView.getMenu().removeItem(R.id.nav_home);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(Type==null){
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}