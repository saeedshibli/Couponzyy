package com.example.couponzy;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.couponzy.Model.FireBaseDB;
import com.example.couponzy.Model.User;
import com.example.couponzy.Model.model;
import com.example.couponzy.ui.CouponsLine.CouponsLineViewModel;
import com.example.couponzy.ui.Map.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel map;

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
            LatLng pos=new LatLng(seller.lat,seller.lon);
            Marker mkr = mMap.addMarker(new MarkerOptions().position(pos).title(seller.firstname + " "+ seller.lastname + " " + seller.email + " "+ seller.phone));
        }
        // Add a marker in Sydney and move the camera
        LatLng middle = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((middle),8.0f));
    }
}