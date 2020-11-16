package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Results extends FragmentActivity implements OnMapReadyCallback {

    private YelpData[] yelp = Search.yelpData;
    public static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView nameView = findViewById(R.id.insertName);
        TextView ratingView = findViewById(R.id.insertRating);
        TextView addressView = findViewById(R.id.insertAddress);
        TextView phoneView = findViewById(R.id.insertPhone);
        TextView openView = findViewById(R.id.insertOpen);
        Button previous = findViewById(R.id.previous);
        Button next = findViewById(R.id.next);
        Button favoriteBtn = findViewById(R.id.favoriteBtn);

        if(index == 0) previous.setVisibility(View.GONE);

        if(yelp != null) {
            nameView.setText(yelp[index].name);
            ratingView.setText(yelp[index].rating +"/5.0");
            addressView.setText(yelp[index].address);
            phoneView.setText(yelp[index].phone_num);
            openView.setText(String.valueOf(!yelp[index].closed));

            if(index == yelp.length-1) {
                next.setVisibility(View.GONE);
            }
            favoriteBtn.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.GONE);
            favoriteBtn.setVisibility(View.GONE);
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
        index -= 1;
        Intent results = new Intent(getApplicationContext(), Results.class);
        startActivity(results);
    }

    public void nextBtn(View view) {
        index += 1;
        Intent results = new Intent(getApplicationContext(), Results.class);
        startActivity(results);
    }

    public void favoriteBtn(View view) {
        int favCount = 1;
        ArrayList<Object[]> favoriteData = null;

        try {
            FileInputStream fi = new FileInputStream(new File(getFilesDir(), "favList.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            favoriteData = (ArrayList<Object[]>) oi.readObject();
            oi.close();
            fi.close();
        } catch(Exception e) {
        }

        if(favoriteData == null) {
            favoriteData = new ArrayList<>();
        }

        for(int i=0; i<favoriteData.size(); i++) {
            if(favoriteData.get(i)[0].equals(MainActivity.email)) {
                favCount++;
            }
        }

        if(favCount == 6) {
            Toast.makeText(getApplicationContext(),"Can't add to favorites", Toast.LENGTH_SHORT).show();
        } else {
            boolean alreadyExists = false;

            Object[] eachData = new Object[2];
            eachData[0] = MainActivity.email;
            eachData[1] = yelp[index];

            for(int i=0; i<favoriteData.size(); i++) {
                YelpData prevYelp = (YelpData) favoriteData.get(i)[1];
                YelpData newYelp = (YelpData) eachData[1];
                if(favoriteData.get(i)[0].equals(eachData[0]) && prevYelp.equals(newYelp)) {
                    alreadyExists = true;
                    break;
                }
            }

            if(alreadyExists) {
                Toast.makeText(getApplicationContext(),"Business already added", Toast.LENGTH_SHORT).show();
            } else {
                favoriteData.add(eachData);
                Toast.makeText(getApplicationContext(),"Added to favorites", Toast.LENGTH_SHORT).show();
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(favoriteData);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}