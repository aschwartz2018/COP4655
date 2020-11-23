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

    //Stores the data for a particular favorite business
    private YelpData favoriteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_fav_data);

        //Set up the map fragment
        favoriteData = Favorites.specFavData;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get the text views from their ids
        TextView nameView = findViewById(R.id.insertName);
        TextView ratingView = findViewById(R.id.insertRating);
        TextView addressView = findViewById(R.id.insertAddress);
        TextView phoneView = findViewById(R.id.insertPhone);
        TextView openView = findViewById(R.id.insertOpen);

        //I don't need to check if favoriteData == null
        //since this activity requires that the data exists
        //to be launched in the first place

        //Sets the text of the corresponding text views from the favoriteData
        nameView.setText(favoriteData.name);
        ratingView.setText(favoriteData.rating +"/5.0");
        addressView.setText(favoriteData.address);
        phoneView.setText(favoriteData.phone_num);
        openView.setText(String.valueOf(!favoriteData.closed));
    }

    //Onclick method for the back button
    public void backBtn(View view) {
        //Launches the favorite activity
        Intent favs = new Intent(getApplicationContext(), Favorites.class);
        startActivity(favs);
    }

    public void onMapReady(GoogleMap googleMap) {
        //Gets the coordinates from the favorite data
        double lat=favoriteData.lat;
        double lon=favoriteData.lon;

        //Map logic
        LatLng latLng = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(20.0f);
    }
}