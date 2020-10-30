package com.alexschwartz.compoundnavapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONObject;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    public static WeatherData wd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final ArrayList<String> list1 = new ArrayList<>();
        final ArrayList<String> list2 = new ArrayList<>();
        final ArrayList<String> list3 = new ArrayList<>();
        final ArrayList<String> list4 = new ArrayList<>();
        final ArrayList<String> list5 = new ArrayList<>();

        long aDay = 24*60*60;
        long day5 = System.currentTimeMillis() / 1000L;
        long day4 = day5 - aDay;
        long day3 = day5 - 2*aDay;
        long day2 = day5 - 3*aDay;
        long day1 = day5 - 4*aDay;

        WeatherData wdtemp = MainActivity.weatherData;
        if(wdtemp != null) {
            wd = wdtemp;
        }
        if(wd != null) {
            double lat = Double.parseDouble(wd.lat);
            double lon = Double.parseDouble(wd.lon);

            String base="https://api.openweathermap.org/data/2.5/onecall/timemachine?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&";
            String mainLink=base+"lat="+lat+"&lon="+lon;
            String link1 = mainLink + "&dt="+day1;
            String link2 = mainLink + "&dt="+day2;
            String link3 = mainLink + "&dt="+day3;
            String link4 = mainLink + "&dt="+day4;
            String link5 = mainLink + "&dt="+day5;

            RequestQueue queue = Volley.newRequestQueue(this);
            final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            final ListView day1List = findViewById(R.id.day1);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, link1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                list1.add("Day 1");
                                list1.add("Temperature: "+json.getJSONObject("current").getString("temp")+" degrees F");
                                list1.add("Weather: "+json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
                                list1.add("Humidity: "+json.getJSONObject("current").getString("humidity")+"%");
                                arrayAdapter1.clear();
                                arrayAdapter1.addAll(list1);
                                day1List.setAdapter(arrayAdapter1);
                                day1List.setEnabled(false);
                            } catch (Exception e) {
                                System.out.println("JSON couldn't parse");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JSON request failed");
                }
            });
            queue.add(stringRequest);

            queue = Volley.newRequestQueue(this);
            final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            final ListView day2List = findViewById(R.id.day2);
            stringRequest = new StringRequest(Request.Method.GET, link2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                list2.add("Day 2");
                                list2.add("Temperature: "+json.getJSONObject("current").getString("temp")+" degrees F");
                                list2.add("Weather: "+json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
                                list2.add("Humidity: "+json.getJSONObject("current").getString("humidity")+"%");
                                arrayAdapter2.clear();
                                arrayAdapter2.addAll(list2);
                                day2List.setAdapter(arrayAdapter2);
                                day2List.setEnabled(false);
                            } catch (Exception e) {
                                System.out.println("JSON couldn't parse");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JSON request failed");
                }
            });
            queue.add(stringRequest);

            queue = Volley.newRequestQueue(this);
            final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            final ListView day3List = findViewById(R.id.day3);
            stringRequest = new StringRequest(Request.Method.GET, link3,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                list3.add("Day 3");
                                list3.add("Temperature: "+json.getJSONObject("current").getString("temp")+" degrees F");
                                list3.add("Weather: "+json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
                                list3.add("Humidity: "+json.getJSONObject("current").getString("humidity")+"%");
                                arrayAdapter3.clear();
                                arrayAdapter3.addAll(list3);
                                day3List.setAdapter(arrayAdapter3);
                                day3List.setEnabled(false);
                            } catch (Exception e) {
                                System.out.println("JSON couldn't parse");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JSON request failed");
                }
            });
            queue.add(stringRequest);

            queue = Volley.newRequestQueue(this);
            final ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            final ListView day4List = findViewById(R.id.day4);
            stringRequest = new StringRequest(Request.Method.GET, link4,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                list4.add("Day 4");
                                list4.add("Temperature: "+json.getJSONObject("current").getString("temp")+" degrees F");
                                list4.add("Weather: "+json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
                                list4.add("Humidity: "+json.getJSONObject("current").getString("humidity")+"%");
                                arrayAdapter4.clear();
                                arrayAdapter4.addAll(list4);
                                day4List.setAdapter(arrayAdapter4);
                                day4List.setEnabled(false);
                            } catch (Exception e) {
                                System.out.println("JSON couldn't parse");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JSON request failed");
                }
            });
            queue.add(stringRequest);

            queue = Volley.newRequestQueue(this);
            final ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            final ListView day5List = findViewById(R.id.day5);
            stringRequest = new StringRequest(Request.Method.GET, link5,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                list5.add("Day 5");
                                list5.add("Temperature: "+json.getJSONObject("current").getString("temp")+" degrees F");
                                list5.add("Weather: "+json.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
                                list5.add("Humidity: "+json.getJSONObject("current").getString("humidity")+"%");
                                arrayAdapter5.clear();
                                arrayAdapter5.addAll(list5);
                                day5List.setAdapter(arrayAdapter5);
                                day5List.setEnabled(false);
                            } catch (Exception e) {
                                System.out.println("JSON couldn't parse");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JSON request failed");
                }
            });
            queue.add(stringRequest);
        }

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
}