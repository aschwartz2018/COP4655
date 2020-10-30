package com.alexschwartz.compoundnavapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE = 100;
    public String city;
    public EditText inputField;
    public String link="https://api.openweathermap.org/data/2.5/weather?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&";
    private static final int REQUEST_CODE_PERMISSION = 2;
    public String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    public GPSTracker gps;
    public boolean fromGPSBtn = false;
    public boolean gpsGood=false;
    public TextView jsonSuccess;
    public static WeatherData weatherData;

    public static WeatherData getWeatherData() {
        return weatherData;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonSuccess = findViewById(R.id.jsonSuccess);
        jsonSuccess.setText("");

        inputField = findViewById(R.id.userInput);

        ImageView speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        break;
                    case R.id.action_results:
                        Intent results = new Intent(getApplicationContext(), Results.class);
                        startActivity(results);
                        break;
                    case R.id.action_map:
                        Intent map = new Intent(getApplicationContext(), Map.class);
                        startActivity(map);
                        break;
                    case R.id.action_history:
                        Intent history = new Intent(getApplicationContext(), History.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }

    public void cityBtn(View view) {
        fromGPSBtn=false;
        city = inputField.getText().toString();

        getLocation(11111111,0,city);
    }

    public void gpsBtn(View view) {
        fromGPSBtn=true;
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != getPackageManager().PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(MainActivity.this);

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
                gpsBtn(view);
            }
            getLocation(gps.getLatitude(),gps.getLongitude(),"NoCity");
        } else{
            gps.showSettingsAlert();
        }
    }

    public void getLocation(double lat, double lon, String city) {
        String keyword="";
        if(lat==11111111) {
            if(city.length()!=0) {
                if (isInteger(city)) {
                    keyword="zip=";
                } else {
                    keyword="q=";
                }
            }
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="";
        if(city!="NoCity")
            url=link+keyword+city;
        else
            url=link+"lat="+lat+"&lon="+lon;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            weatherData = new WeatherData();
                            weatherData.city = json.getString("name");
                            weatherData.temperature = json.getJSONObject("main").getString("temp");
                            weatherData.tempFeels = json.getJSONObject("main").getString("feels_like");
                            weatherData.minTemp = json.getJSONObject("main").getString("temp_min");
                            weatherData.maxTemp = json.getJSONObject("main").getString("temp_max");
                            weatherData.cloudiness = json.getJSONArray("weather").getJSONObject(0).getString("main");
                            weatherData.pressure = json.getJSONObject("main").getString("pressure");
                            weatherData.humidity = json.getJSONObject("main").getString("humidity");
                            weatherData.windSpeed = json.getJSONObject("wind").getString("speed");
                            weatherData.windDeg = json.getJSONObject("wind").getString("deg");
                            weatherData.lat = json.getJSONObject("coord").getString("lat");
                            weatherData.lon = json.getJSONObject("coord").getString("lon");

                            jsonSuccess.setText("Request successful");
                        } catch (Exception e) {
                            System.out.println("Couldn't parse json");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonSuccess.setText("Request unsuccessful");
            }
        });

        queue.add(stringRequest);
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputField.setText(result.get(0));
                }
                break;
            }
        }
    }
}