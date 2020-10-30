package com.alexschwartz.compoundnavapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Locale;

public class Results extends AppCompatActivity {

    public static WeatherData wd;
    public ArrayList<String> list1 = new ArrayList<>();
    public ArrayList<String> list2;

    public TextToSpeech t1;
    public TextToSpeech t2;
    public Button b1;
    public Button b2;

    public Results() {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list1);;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);;

        b1=findViewById(R.id.button1);
        b2=findViewById(R.id.button2);
        b2.setVisibility(View.GONE);

        ListView headingsList = findViewById(R.id.weatherHeadings);
        ListView dataList;
        headingsList.setAdapter(arrayAdapter1);
        headingsList.setEnabled(false);

        WeatherData wdtemp = MainActivity.getWeatherData();
        if(wdtemp != null) {
            wd = wdtemp;
        }
        if(wd != null) {
            dataList = findViewById(R.id.weatherData);
            list2 = new ArrayList<>();
            list2.add(wd.city);
            list2.add(wd.temperature+" degrees F");
            list2.add(wd.tempFeels+" degrees F");
            list2.add(wd.minTemp+" degrees F");
            list2.add(wd.maxTemp+" degrees F");
            list2.add(wd.cloudiness);
            list2.add(wd.pressure+" hpa");
            list2.add(wd.humidity+"%");
            list2.add(wd.windSpeed+" mph");
            list2.add(wd.windDeg+" degrees");
            list2.add(wd.lat+" lat, "+wd.lon+" lon");
            arrayAdapter2.clear();
            arrayAdapter2.addAll(list2);
            dataList.setAdapter(arrayAdapter2);
            dataList.setEnabled(false);
            b2.setVisibility(View.VISIBLE);
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = list1.toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.US);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = list2.toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
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
}