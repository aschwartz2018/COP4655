package com.alexschwartz.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private RelativeLayout signedInView;
    private BottomNavigationView bottomNavigationView;
    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 1;
    private boolean loggedIn;
    private String name;
    private String email;
    private String photoURL;
    private TextView nameView;
    private TextView emailView;
    private ImageView profilePicture;
    private static String SHARED_PREFS = "sharedPrefs";
    private static String LOGGED_IN = "loggedIn";
    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String PHOTO_URL = "photoURL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences loadData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        loggedIn = loadData.getBoolean(LOGGED_IN,false);
        System.out.println(loadData.getString(NAME,""));

        Button signOutButton = findViewById(R.id.sign_out_button);
        signInButton = findViewById(R.id.sign_in_button);
        signedInView = findViewById(R.id.signed_in_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        profilePicture = findViewById(R.id.account_photo);

        if(!loggedIn) {
            signInButton.setVisibility(View.VISIBLE);
            signedInView.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            name = loadData.getString(NAME,"");
            email = loadData.getString(EMAIL,"");
            photoURL = loadData.getString(PHOTO_URL,"");

            signInButton.setVisibility(View.GONE);
            signedInView.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.setSelectedItemId(R.id.action_home);

            nameView.setText(name);
            emailView.setText(email);
            Glide.with(this).load(photoURL).into(profilePicture);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut();
                signedInView.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.GONE);
                loggedIn = false;

                SharedPreferences saveData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = saveData.edit();
                editor.putBoolean(LOGGED_IN,loggedIn);
                editor.putString(NAME,"");
                editor.putString(EMAIL,"");
                editor.putString(PHOTO_URL,"");
                editor.apply();
            }
        });

        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
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

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            signedInView.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            loggedIn = true;

            name = account.getDisplayName();
            email = account.getEmail();
            photoURL = account.getPhotoUrl().toString();

            nameView.setText(name);
            emailView.setText(email);
            Glide.with(this).load(photoURL).into(profilePicture);

            SharedPreferences saveData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = saveData.edit();
            editor.putBoolean(LOGGED_IN,loggedIn);
            editor.putString(NAME,name);
            editor.putString(EMAIL,email);
            editor.putString(PHOTO_URL,photoURL);
            editor.apply();

        } catch (Exception e) {
        }
    }

}