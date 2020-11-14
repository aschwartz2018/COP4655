package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Results extends FragmentActivity implements OnMapReadyCallback {

    private YelpData[] yelp = Search.yelpData;
    public static int index;
    private TextView nameView;
    private TextView ratingView;
    private TextView addressView;
    private TextView phoneView;
    private TextView openView;
    private Button previous;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nameView = findViewById(R.id.insertName);
        ratingView = findViewById(R.id.insertRating);
        addressView = findViewById(R.id.insertAddress);
        phoneView = findViewById(R.id.insertPhone);
        openView = findViewById(R.id.insertOpen);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        if(index == 0) previous.setVisibility(View.GONE);

        if(yelp != null) {
            nameView.setText(yelp[index].name);
            ratingView.setText(String.valueOf(yelp[index].rating)+"/5.0");
            addressView.setText(yelp[index].address);
            phoneView.setText(yelp[index].phone_num);
            openView.setText(String.valueOf(!yelp[index].closed));

            if(index == yelp.length-1) {
                next.setVisibility(View.GONE);
            }
        } else {
            next.setVisibility(View.GONE);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_results).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        index = 0;
                        break;
                    case R.id.action_search:
                        Intent search = new Intent(getApplicationContext(), Search.class);
                        startActivity(search);
                        index = 0;
                        break;
                    case R.id.action_results:
                        Intent results = new Intent(getApplicationContext(), Results.class);
                        startActivity(results);
                        index = 0;
                        break;
                    case R.id.action_favorites:
                        Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                        startActivity(favorites);
                        index = 0;
                        break;
                }
                return true;
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        double lat=0;
        double lon=0;
        if(yelp != null) {
            lat = yelp[index].lat;
            lon = yelp[index].lon;
        }
        LatLng latLng = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(20.0f);
    }

    public void previousBtn(View view) {
        if(yelp != null && index >= 1) {
            index -= 1;
            Intent results = new Intent(getApplicationContext(), Results.class);
            startActivity(results);
        }
    }

    public void nextBtn(View view) {
        if(yelp != null) {
            if(index < yelp.length) {
                index += 1;
                Intent results = new Intent(getApplicationContext(), Results.class);
                startActivity(results);
            }
        }
    }
}