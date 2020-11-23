package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    //Have static variable for specific business from Favorites
    public static YelpData specFavData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Initially set favorites to null
        ArrayList<Object[]> fav = null;

        //Get the favorite data from the favorite list .ser file
        try {
            FileInputStream fi = new FileInputStream(new File(getFilesDir(), "favList.ser"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            fav = (ArrayList<Object[]>) oi.readObject();
            oi.close();
            fi.close();
        } catch(Exception e) {
        }

        //Get all the business name buttons from their ids
        Button fav0 = findViewById(R.id.fav0);
        Button fav1 = findViewById(R.id.fav1);
        Button fav2 = findViewById(R.id.fav2);
        Button fav3 = findViewById(R.id.fav3);
        Button fav4 = findViewById(R.id.fav4);

        //Initially disable all of the business name buttons
        fav0.setEnabled(false);
        fav1.setEnabled(false);
        fav2.setEnabled(false);
        fav3.setEnabled(false);
        fav4.setEnabled(false);

        //Get all the remove buttons from their ids
        Button remove0 = findViewById(R.id.remove0);
        Button remove1 = findViewById(R.id.remove1);
        Button remove2 = findViewById(R.id.remove2);
        Button remove3 = findViewById(R.id.remove3);
        Button remove4 = findViewById(R.id.remove4);

        //Initially hide all of the remove buttons
        remove0.setVisibility(View.GONE);
        remove1.setVisibility(View.GONE);
        remove2.setVisibility(View.GONE);
        remove3.setVisibility(View.GONE);
        remove4.setVisibility(View.GONE);

        //Displays the favorite business names for the buttons
        //Allows them to be clicked to show more info
        //And sets the specific remove buttons to visible
        int count=0;
        if(fav != null) {
            for (Object[] objArray : fav) {
                String email = (String) objArray[0];
                YelpData yelp = (YelpData) objArray[1];

                //Only takes the data from the signed in user
                if(MainActivity.email.equals(email)) {
                    if(count==0) {
                        fav0.setText(yelp.name);
                        fav0.setEnabled(true);
                        remove0.setVisibility(View.VISIBLE);
                    } else if(count==1) {
                        fav1.setText(yelp.name);
                        fav1.setEnabled(true);
                        remove1.setVisibility(View.VISIBLE);
                    } else if(count==2) {
                        fav2.setText(yelp.name);
                        fav2.setEnabled(true);
                        remove2.setVisibility(View.VISIBLE);
                    } else if(count==3) {
                        fav3.setText(yelp.name);
                        fav3.setEnabled(true);
                        remove3.setVisibility(View.VISIBLE);
                    } else {
                        fav4.setText(yelp.name);
                        fav4.setEnabled(true);
                        remove4.setVisibility(View.VISIBLE);
                    }
                    count++;
                }
            }
        }
        final ArrayList<Object[]> finalFav = fav;

        /*
            fav0, fav1, ... , fav4 are on click listeners
            for up to the 5 business name buttons.
            When the those buttons (they are transparent)
            are clicked it goes to the SpecificFavData activity
        */

        fav0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        specFavData = (YelpData) finalFav.get(i)[1];
                        break;
                    }
                }
                Intent specific = new Intent(getApplicationContext(), SpecificFavData.class);
                startActivity(specific);
            }
        });

        fav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==1) {
                            specFavData = (YelpData) finalFav.get(i)[1];
                            break;
                        }
                        count++;
                    }
                }
                Intent specific = new Intent(getApplicationContext(), SpecificFavData.class);
                startActivity(specific);
            }
        });

        fav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==2) {
                            specFavData = (YelpData) finalFav.get(i)[1];
                            break;
                        }
                        count++;
                    }
                }
                Intent specific = new Intent(getApplicationContext(), SpecificFavData.class);
                startActivity(specific);
            }
        });

        fav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==3) {
                            specFavData = (YelpData) finalFav.get(i)[1];
                            break;
                        }
                        count++;
                    }
                }
                Intent specific = new Intent(getApplicationContext(), SpecificFavData.class);
                startActivity(specific);
            }
        });

        fav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0; i<finalFav.size(); i++) {
                    if(finalFav.get(i)[0].equals(MainActivity.email)) {
                        if(count==4) {
                            specFavData = (YelpData) finalFav.get(i)[1];
                            break;
                        }
                        count++;
                    }
                }
                Intent specific = new Intent(getApplicationContext(), SpecificFavData.class);
                startActivity(specific);
            }
        });

        /*
            remove0, remove1, ... , remove4 are on click listeners
            for up to the 5 remove buttons.
            When a remove button is clicked, that specific business is deleted.
            This activity will refresh with the business removed.
        */

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

        //Logic for the bottom nav bar
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