package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_favorites).setChecked(true);
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
}