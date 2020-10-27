package com.alexschwartz.nativeweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public LinearLayout resultsPage;
    public LinearLayout searchPage;
    public ListView headingsList;
    public ListView dataList;
    public EditText inputField;
    public ArrayAdapter<String> arrayAdapter1;
    public ArrayAdapter<String> arrayAdapter2;
    public ArrayList<String> list1 = new ArrayList<>();
    public ArrayList<String> list2;
    public String city;
    public String link="https://api.openweathermap.org/data/2.5/weather?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&";
    private static final int REQUEST_CODE_PERMISSION = 2;
    public String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    public GPSTracker gps;
    public boolean fromGPSBtn = false;
    public boolean gpsGood=false;
    public JSONObject myJson;

    public MainActivity() {
        list1.add("City");
        list1.add("Temperature");
        list1.add("Feels Like");
        list1.add("Min Temperature");
        list1.add("Max Temperature");
        list1.add("Cloudiness");
        list1.add("Pressure");
        list1.add("Humidity");
        list1.add("Wind Speed");
        list1.add("Wind Direction");
        list1.add("Geolocation");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list1);
        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        searchPage = findViewById(R.id.searchPage);
        resultsPage = findViewById(R.id.resultsPage);
    }

    public void cityBtn(View view) {
        fromGPSBtn=false;
        inputField = findViewById(R.id.userInput);
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
        final JSONObject[] json = {null};
        if(city!="NoCity")
            url=link+keyword+city;
        else
            url=link+"lat="+lat+"&lon="+lon;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            json[0] = new JSONObject(response);
                            myJson = json[0];
                            String cityFromJSON = json[0].getString("name");
                            String temperature = json[0].getJSONObject("main").getString("temp");
                            String tempFeels = json[0].getJSONObject("main").getString("feels_like");
                            String minTemp = json[0].getJSONObject("main").getString("temp_min");
                            String maxTemp = json[0].getJSONObject("main").getString("temp_max");
                            String cloudiness = json[0].getJSONArray("weather").getJSONObject(0).getString("main");
                            String pressure = json[0].getJSONObject("main").getString("pressure");
                            String humidity = json[0].getJSONObject("main").getString("humidity");
                            String windSpeed = json[0].getJSONObject("wind").getString("speed");
                            String windDeg = json[0].getJSONObject("wind").getString("deg");
                            String latFromJSON = json[0].getJSONObject("coord").getString("lat");
                            String lonFromJSON = json[0].getJSONObject("coord").getString("lon");

                            searchPage.setVisibility(View.GONE);
                            resultsPage.setVisibility(View.VISIBLE);

                            headingsList = findViewById(R.id.weatherHeadings);
                            headingsList.setAdapter(arrayAdapter1);
                            headingsList.setEnabled(false);

                            dataList = findViewById(R.id.weatherData);
                            list2 = new ArrayList<>();
                            list2.add(cityFromJSON);
                            list2.add(temperature+" degF");
                            list2.add(tempFeels+" degF");
                            list2.add(minTemp+" degF");
                            list2.add(maxTemp+" degF");
                            list2.add(cloudiness);
                            list2.add(pressure+" hpa");
                            list2.add(humidity+"%");
                            list2.add(windSpeed+" mph");
                            list2.add(windDeg+" degrees");
                            list2.add(latFromJSON+" lat, "+lonFromJSON+" lon");
                            arrayAdapter2.clear();
                            arrayAdapter2.addAll(list2);
                            dataList.setAdapter(arrayAdapter2);
                            dataList.setEnabled(false);

                            String embedMap = "<iframe frameborder='0' style='width:100%; height:100%; border:0'"
                                    +" src='https://www.google.com/maps/embed/v1/view?key=AIzaSyC0FDwy4JSx7rRULalVcSaz7K_9pOGJ8Yc&center="
                                    + latFromJSON + ","+ lonFromJSON + "&zoom=15'></iframe>";

                            WebView webview = (WebView) findViewById(R.id.myMap);
                            webview.setWebViewClient(new WebViewClient());
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.loadData(embedMap, "text/html", null);
                        } catch (Exception e) {
                            searchPage.setVisibility(View.VISIBLE);
                            resultsPage.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("JSON request didn't work");
            }
        });

        queue.add(stringRequest);
    }

    public void goBack(View view) {
        searchPage.setVisibility(View.VISIBLE);
        resultsPage.setVisibility(View.GONE);
        if(!fromGPSBtn)
            inputField.setText("");
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
}