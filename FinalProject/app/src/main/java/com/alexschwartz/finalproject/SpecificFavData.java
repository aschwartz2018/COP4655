package com.alexschwartz.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SpecificFavData extends AppCompatActivity implements OnMapReadyCallback {

    private YelpData favoriteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_fav_data);

        favoriteData = Favorites.specFavData;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView nameView = findViewById(R.id.insertName);
        TextView ratingView = findViewById(R.id.insertRating);
        TextView addressView = findViewById(R.id.insertAddress);
        TextView phoneView = findViewById(R.id.insertPhone);
        TextView openView = findViewById(R.id.insertOpen);

        nameView.setText(favoriteData.name);
        ratingView.setText(favoriteData.rating +"/5.0");
        addressView.setText(favoriteData.address);
        phoneView.setText(favoriteData.phone_num);
        openView.setText(String.valueOf(!favoriteData.closed));
    }

    public void backBtn(View view) {
        Intent favs = new Intent(getApplicationContext(), Favorites.class);
        startActivity(favs);
    }

    public void onMapReady(GoogleMap googleMap) {
        double lat=favoriteData.lat;
        double lon=favoriteData.lon;
        LatLng latLng = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(20.0f);
    }
}