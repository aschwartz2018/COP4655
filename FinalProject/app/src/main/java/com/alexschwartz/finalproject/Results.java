package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_results).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        break;
                    case R.id.action_search:
                        Intent results = new Intent(getApplicationContext(), Search.class);
                        startActivity(results);
                        break;
                    case R.id.action_results:
                        Intent map = new Intent(getApplicationContext(), Results.class);
                        startActivity(map);
                        break;
                    case R.id.action_favorites:
                        Intent history = new Intent(getApplicationContext(), Favorites.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });
    }
}