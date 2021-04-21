package com.example.couponzy;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.couponzy.Model.FireBaseDB;
import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;
import com.example.couponzy.ui.CouponsLine.CouponsLineViewModel;
import com.example.couponzy.ui.Map.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel map;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(()->{
            map=new ViewModelProvider(this).get(MapViewModel.class);
        });

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lat=31.666,lon=34.952;
        List<User> sellers=map.getSellers();
        int len=sellers.size();
        for(User seller:sellers){
            Picasso.get().load(seller.getImgURL()).into(target);
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
            LatLng pos=new LatLng(seller.lat,seller.lon);
            Marker mkr = mMap.addMarker(new MarkerOptions().position(pos).title(seller.firstname + " "+ seller.lastname));
            mkr.setSnippet(seller.email + " \n "+ seller.phone + " \n ");
            mkr.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        }
        // Add a marker in Sydney and move the camera
        LatLng middle = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((middle),8.0f));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MapsActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(MapsActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(MapsActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this,MainActivity.class);
                intent .putExtra("tag",true);
                finish();
                startActivity(intent);
            }
        });
    }
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom from) {
           bitmap=bitmap1;
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };



}