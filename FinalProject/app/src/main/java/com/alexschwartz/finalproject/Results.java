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

    //Set yelp variable to the static yelpData in Search class
    private YelpData[] yelp = Search.yelpData;

    //Store index for each business
    public static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Have variables for all the xml elements
        TextView nameView = findViewById(R.id.insertName);
        TextView ratingView = findViewById(R.id.insertRating);
        TextView addressView = findViewById(R.id.insertAddress);
        TextView phoneView = findViewById(R.id.insertPhone);
        TextView openView = findViewById(R.id.insertOpen);
        Button previous = findViewById(R.id.previous);
        Button next = findViewById(R.id.next);
        Button favoriteBtn = findViewById(R.id.favoriteBtn);

        //If it is the first business, hide previous button
        if(index == 0) previous.setVisibility(View.GONE);

        if(yelp != null) {
            //If yelp data exists, set the text views to the respective data
            nameView.setText(yelp[index].name);
            ratingView.setText(yelp[index].rating +"/5.0");
            addressView.setText(yelp[index].address);
            phoneView.setText(yelp[index].phone_num);
            openView.setText(String.valueOf(!yelp[index].closed));

            //If it is the last business
            if(index == yelp.length-1) {
                //Hide the next button
                next.setVisibility(View.GONE);
            }
            favoriteBtn.setVisibility(View.VISIBLE);
        } else {
            //If the yelp data doesn't exist, hide next and favorite buttons
            next.setVisibility(View.GONE);
            favoriteBtn.setVisibility(View.GONE);
        }

        //Logic for the bottom nav bar
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
        //If the yelp data is null use (0,0) as (lat,lon)
        double lat=0;
        double lon=0;
        if(yelp != null) {
            //If the yelp data exists, set lat and lon to the
            //yelp API provided latitude and longitude data
            lat = yelp[index].lat;
            lon = yelp[index].lon;
        }

        //Map logic
        LatLng latLng = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(20.0f);
    }

    //Onclick for the previous button
    public void previousBtn(View view) {
        //Decrement the index variable
        index -= 1;

        //Start the same activity with index changed
        Intent results = new Intent(getApplicationContext(), Results.class);
        startActivity(results);
    }

    //Onclick for the next button
    public void nextBtn(View view) {
        //Increment the index variable
        index += 1;

        //Start the same activity with index changed
        Intent results = new Intent(getApplicationContext(), Results.class);
        startActivity(results);
    }

    public void favoriteBtn(View view) {
        int favCount = 1;

        //Initially the favorite data is null
        ArrayList<Object[]> favoriteData = null;

        //Get any favorite data from the favorite list .ser file
        try {
            FileInputStream fi = new FileInputStream(new File(getFilesDir(), "favList.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            favoriteData = (ArrayList<Object[]>) oi.readObject();
            oi.close();
            fi.close();
        } catch(Exception e) {
        }

        //If it is the first item stored, then make a new list
        if(favoriteData == null) {
            favoriteData = new ArrayList<>();
        }

        //Gets the count of how many favorite businesses
        for(int i=0; i<favoriteData.size(); i++) {
            if(favoriteData.get(i)[0].equals(MainActivity.email)) {
                favCount++;
            }
        }

        if(favCount == 6) { //If the count gets to 6 businesses, display error toast
            Toast.makeText(getApplicationContext(),"Can't add to favorites", Toast.LENGTH_SHORT).show();
        } else {
            boolean alreadyExists = false;

            //Stores new data with email identifier and yelp data from business
            Object[] eachData = new Object[2];
            eachData[0] = MainActivity.email;
            eachData[1] = yelp[index];

            //Checks if the selected business already exists in favorites list
            for(int i=0; i<favoriteData.size(); i++) {
                YelpData prevYelp = (YelpData) favoriteData.get(i)[1];
                YelpData newYelp = (YelpData) eachData[1];
                if(favoriteData.get(i)[0].equals(eachData[0]) && prevYelp.equals(newYelp)) {
                    alreadyExists = true;
                    break;
                }
            }

            if(alreadyExists) { //Displays toast if the business already added to favorites
                Toast.makeText(getApplicationContext(),"Business already added", Toast.LENGTH_SHORT).show();
            } else {
                //Append the new data to the favorite data list
                favoriteData.add(eachData);
                Toast.makeText(getApplicationContext(),"Added to favorites", Toast.LENGTH_SHORT).show();

                //Store the favorite data list to the favorite list .ser file
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