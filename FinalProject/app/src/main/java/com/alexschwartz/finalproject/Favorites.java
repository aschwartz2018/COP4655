package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ArrayList<Object[]> fav = null;

        try {
            FileInputStream fi = new FileInputStream(new File(getFilesDir(), "favList.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            fav = (ArrayList<Object[]>) oi.readObject();
            oi.close();
            fi.close();
        } catch(Exception e) {
        }

        TextView fav0 = findViewById(R.id.fav0);
        TextView fav1 = findViewById(R.id.fav1);
        TextView fav2 = findViewById(R.id.fav2);
        TextView fav3 = findViewById(R.id.fav3);
        TextView fav4 = findViewById(R.id.fav4);

        Button remove0 = findViewById(R.id.remove0);
        Button remove1 = findViewById(R.id.remove1);
        Button remove2 = findViewById(R.id.remove2);
        Button remove3 = findViewById(R.id.remove3);
        Button remove4 = findViewById(R.id.remove4);

        remove0.setVisibility(View.GONE);
        remove1.setVisibility(View.GONE);
        remove2.setVisibility(View.GONE);
        remove3.setVisibility(View.GONE);
        remove4.setVisibility(View.GONE);

        int count=0;
        if(fav != null) {
            for (Object[] objArray : fav) {
                String email = (String) objArray[0];
                YelpData yelp = (YelpData) objArray[1];

                if(MainActivity.email.equals(email)) {
                    if(count==0) {
                        fav0.setText(yelp.name);
                        remove0.setVisibility(View.VISIBLE);
                    } else if(count==1) {
                        fav1.setText(yelp.name);
                        remove1.setVisibility(View.VISIBLE);
                    } else if(count==2) {
                        fav2.setText(yelp.name);
                        remove2.setVisibility(View.VISIBLE);
                    } else if(count==3) {
                        fav3.setText(yelp.name);
                        remove3.setVisibility(View.VISIBLE);
                    } else {
                        fav4.setText(yelp.name);
                        remove4.setVisibility(View.VISIBLE);
                    }
                    count++;
                }
            }
        }

        final ArrayList<Object[]> finalFav = fav;

        remove0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        finalFav.remove(i);
                        break;
                    }
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalFav);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

        remove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==1)
                            finalFav.remove(i);
                        count++;
                    }
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalFav);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

        remove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==2)
                            finalFav.remove(i);
                        count++;
                    }
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalFav);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

        remove3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==3)
                            finalFav.remove(i);
                        count++;
                    }
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalFav);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

        remove4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==4)
                            finalFav.remove(i);
                        count++;
                    }
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "favList.ser"));
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalFav);
                    oos.close();
                    fos.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

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