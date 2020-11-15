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

    public static YelpData[] yelpData;
    private boolean gpsEnabled;
    private double lat;
    private double lon;
    private EditText businessField;
    private EditText locationField;
    private GPSTracker gps;
    private boolean gpsGood=false;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        businessField = findViewById(R.id.businessSearch);
        locationField = findViewById(R.id.location);

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

    public void searchBusinesses(View view) {
        String business = businessField.getText().toString();
        String location = locationField.getText().toString();

        String term;
        if(gpsEnabled) {
            term = "&latitude="+lat+"&longitude="+lon;
        } else {
            term = "&location="+ location;
        }

        String YourUrl = "https://api.yelp.com/v3/businesses/search?term="+ business + term;
        final String ACCESS_TOKEN = "c8n4u3NbIBrrr2ZSZprJA2pKA0HiVAcTmRz1h0JWY2lujHBq0FLaPaXYSuREA0Opa8uD1a_8qC4lTUEJWL-0HtqGNX3MY-3467o5LGThii7gshimyozwRfzBt2CoX3Yx";

        StringRequest request;
        request = new StringRequest(Request.Method.GET, YourUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int total = 5;
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("businesses");
                    int countJSON = jsonArray.length();
                    if(countJSON < total)
                        total = countJSON;
                    if(total == 0) {
                        return;
                    }
                    yelpData = new YelpData[total];
                    for(int i=0; i<total; i++) {
                        JSONObject jsonBusiness = jsonArray.getJSONObject(i);
                        yelpData[i] = new YelpData();
                        yelpData[i].name = jsonBusiness.getString("name");
                        yelpData[i].rating = jsonBusiness.getDouble("rating");
                        yelpData[i].address = jsonBusiness.getJSONObject("location").getString("address1");
                        yelpData[i].phone_num = jsonBusiness.getString("display_phone");
                        yelpData[i].closed = jsonBusiness.getBoolean("is_closed");
                        yelpData[i].lat = jsonBusiness.getJSONObject("coordinates").getDouble("latitude");
                        yelpData[i].lon = jsonBusiness.getJSONObject("coordinates").getDouble("longitude");
                    }
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    public void gpsButton(View view) {
        if(gpsEnabled) {
            gpsEnabled = false;
            Toast.makeText(getApplicationContext(),"GPS Mode Off", Toast.LENGTH_SHORT).show();
            locationField.setEnabled(true);
        } else {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission) != getPackageManager().PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{mPermission},
                            REQUEST_CODE_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            gps = new GPSTracker(Search.this);

            // check if GPS enabled
            if(gps.canGetLocation()){
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
                    gpsGood = true;
                    gpsButton(view);
                }
                lat = gps.getLatitude();
                lon = gps.getLongitude();

                gpsEnabled = true;
                Toast.makeText(getApplicationContext(),"GPS Mode On", Toast.LENGTH_SHORT).show();
                locationField.setEnabled(false);
            } else{
                gps.showSettingsAlert();
            }
        }
    }
}