package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {

    //Static variable to hold all the yelp data
    public static YelpData[] yelpData;

    //Boolean to determine which location is used
    private boolean gpsEnabled;

    //Coordinate variables
    private double lat;
    private double lon;

    //Variables for user input
    private EditText businessField;
    private EditText locationField;

    //Required so GPS works without glitches
    private boolean gpsGood=false;

    //Permission variables
    private static final int REQUEST_CODE_PERMISSION = 2;
    public String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Get the text field variables by their ids
        businessField = findViewById(R.id.businessSearch);
        locationField = findViewById(R.id.location);

        //Logic for the bottom nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_search).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        break;
                    case R.id.action_search:
                        Intent search = new Intent(getApplicationContext(), Search.class);
                        startActivity(search);
                        break;
                    case R.id.action_results:
                        Intent results = new Intent(getApplicationContext(), Results.class);
                        startActivity(results);
                        break;
                    case R.id.action_favorites:
                        Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                        startActivity(favorites);
                        break;
                }
                return true;
            }
        });
    }

    //Called when user pressed "Search" button (onclick)
    public void searchBusinesses(View view) {
        //Get the actual strings from the text fields
        String business = businessField.getText().toString();
        String location = locationField.getText().toString();

        //Will depend on whether GPS is on or not
        String term;

        if(gpsEnabled) {
            //Set "term" to the lat and lon configuration
            term = "&latitude="+lat+"&longitude="+lon;
        } else {
            //Set "term" to the location (city, state, zip) configuration
            term = "&location="+ location;
        }

        //The complete built URL for the Yelp API
        String YourUrl = "https://api.yelp.com/v3/businesses/search?term="+ business + term;

        //My Yelp API key
        final String ACCESS_TOKEN = "c8n4u3NbIBrrr2ZSZprJA2pKA0HiVAcTmRz1h0JWY2lujHBq0FLaPaXYSuREA0Opa8uD1a_8qC4lTUEJWL-0HtqGNX3MY-3467o5LGThii7gshimyozwRfzBt2CoX3Yx";

        //Makes the volley JSON request
        //I am storing all the YelpData (5 entries) in an array
        StringRequest request;
        request = new StringRequest(Request.Method.GET, YourUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //I decided on only showing the first 5 entries from the API
                    int total = 5;
                    JSONObject json = new JSONObject(response);

                    //"businesses" array is the important part from JSON
                    JSONArray jsonArray = json.getJSONArray("businesses");

                    //Gets the count of the array
                    int countJSON = jsonArray.length();

                    //I am storing a max of 5 entries
                    //so if the count is less, we have to use the smaller number
                    if(countJSON < total)
                        total = countJSON;

                    //If there are no entries, return out of the function
                    //to prevent crashing (treat it as if the request didn't happen)
                    if(total == 0) {
                        return;
                    }

                    //Make a new YelpData array
                    yelpData = new YelpData[total];

                    //Traversing all the json data (up to 5 entries)
                    for(int i=0; i<total; i++) {
                        //We are indexing it by each business in the jsonArray
                        JSONObject jsonBusiness = jsonArray.getJSONObject(i);

                        //Make a new YelpData object
                        yelpData[i] = new YelpData();

                        //Store all the data we need for each index
                        yelpData[i].name = jsonBusiness.getString("name");
                        yelpData[i].rating = jsonBusiness.getDouble("rating");
                        yelpData[i].address = jsonBusiness.getJSONObject("location").getString("address1");
                        yelpData[i].phone_num = jsonBusiness.getString("display_phone");
                        yelpData[i].closed = jsonBusiness.getBoolean("is_closed");
                        yelpData[i].lat = jsonBusiness.getJSONObject("coordinates").getDouble("latitude");
                        yelpData[i].lon = jsonBusiness.getJSONObject("coordinates").getDouble("longitude");
                    }

                    //When the JSON request is done, go to the Results tab automatically
                    Intent results = new Intent(getApplicationContext(), Results.class);
                    startActivity(results);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {

            //This is for Authorization Header with Yelp API Key
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","bearer " + ACCESS_TOKEN);
                return params;
            }
        };

        //Add request to the queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    //Called when the user pressed "GPS" button (onclick)
    public void gpsButton(View view) {

        //Toggles GPS logic every time it is clicked
        if(gpsEnabled) {
            gpsEnabled = false;
            Toast.makeText(getApplicationContext(),"GPS Mode Off", Toast.LENGTH_SHORT).show();

            //Enables the location field again
            locationField.setEnabled(true);
        } else {
            //Tries to request required permissions from the user
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission) != getPackageManager().PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{mPermission},
                            REQUEST_CODE_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Make a new GPSTracker object
            GPSTracker gps = new GPSTracker(Search.this);

            // check if GPS is able to be used
            if(gps.canGetLocation()){
                //Fixes weirdness with GPS permissions
                boolean permissionAsking=true;
                while(permissionAsking) {
                    if(ActivityCompat.checkSelfPermission(getApplication(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissionAsking=true;
                    } else {
                        permissionAsking=false;
                    }
                }
                if(!gpsGood) {
                    //Fixes weird glitches with the app on modern android versions
                    gpsGood = true;
                    gpsButton(view);
                }

                //Stores the coordinates in their respective variables
                lat = gps.getLatitude();
                lon = gps.getLongitude();

                gpsEnabled = true;
                Toast.makeText(getApplicationContext(),"GPS Mode On", Toast.LENGTH_SHORT).show();

                //Disables the location field when GPS is on
                locationField.setEnabled(false);
            } else{
                //Shows Android settings popup to enable GPS
                gps.showSettingsAlert();
            }
        }
    }
}