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

    //Variables for the different views
    private RelativeLayout signedInView;
    private RelativeLayout signedOutView;
    private BottomNavigationView bottomNavigationView;
    private TextView nameView;
    private TextView emailView;
    private ImageView profilePicture;

    //Variables for signing into google account
    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 1;

    //Data being stored in shared preferences
    private boolean loggedIn;
    private String name;
    public static String email;
    private String photoURL;

    //Constant variables for shared preferences
    private static String SHARED_PREFS = "sharedPrefs";
    private static String LOGGED_IN = "loggedIn";
    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String PHOTO_URL = "photoURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load any existing data from shared preferences
        SharedPreferences loadData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        loggedIn = loadData.getBoolean(LOGGED_IN,false);

        //Getting xml elements from findViewById
        Button signOutButton = findViewById(R.id.sign_out_button);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signedInView = findViewById(R.id.signed_in_view);
        signedOutView = findViewById(R.id.signed_out_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        profilePicture = findViewById(R.id.account_photo);

        if(!loggedIn) { //If the user is not logged in
            //Enable the signed out view and disable the nav bar
            signedOutView.setVisibility(View.VISIBLE);
            signedInView.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        } else { //If the user is logged in
            //Get name, email, and photoURL variables from the shared preferences
            name = loadData.getString(NAME,"");
            email = loadData.getString(EMAIL,"");
            photoURL = loadData.getString(PHOTO_URL,"");

            //Enable the signed in view and enable the nav bar
            signedOutView.setVisibility(View.GONE);
            signedInView.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.setSelectedItemId(R.id.action_home);

            //Displays the name, email, and profile photo of the user logged in
            nameView.setText(name);
            emailView.setText(email);
            Glide.with(this).load(photoURL).into(profilePicture);
        }

        //Sets the default sign in options for the gso variable
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Make the sign in client based on the above options
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        //Go to the signIn() method when the sign in button is clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Signs the user out of the app
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sign out the client
                googleSignInClient.signOut();

                //Enable the signed in view and enable the nav bar
                signedInView.setVisibility(View.GONE);
                signedOutView.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.GONE);

                //Set logged in variable to false
                loggedIn = false;

                //Saves the loggedIn variable to saved prefs
                SharedPreferences saveData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = saveData.edit();
                editor.putBoolean(LOGGED_IN,loggedIn);

                //Set the name, email, and photoURL sharedPrefs to empty strings
                editor.putString(NAME,"");
                editor.putString(EMAIL,"");
                editor.putString(PHOTO_URL,"");

                //Need to apply() the editor at the end
                editor.apply();

                //Set any search data to null
                Search.yelpData = null;
            }
        });

        //Logic for the bottom nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    //The method for initializing a sign in
    private void signIn() {
        //Brings up the sign in intent to display
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Calls the method to handle sign in with google sign in task
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Only go on to handleSignInRequest if the request code equals to RC_SIGN_IN
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //Method that gets specific account data of the user
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        //If the account doesn't exist, an exception will occur
        try {
            //Get account variable from sign in
            GoogleSignInAccount account = task.getResult(ApiException.class);

            //If above doesn't fail, enable signed in view
            signedInView.setVisibility(View.VISIBLE);
            signedOutView.setVisibility(View.GONE);

            //Enable nav bar and set boolean loggedIn to true
            bottomNavigationView.setVisibility(View.VISIBLE);
            loggedIn = true;

            //Get specific account info
            name = account.getDisplayName();
            email = account.getEmail();
            photoURL = account.getPhotoUrl().toString();

            //Set text fields and image view of the above variables
            nameView.setText(name);
            emailView.setText(email);
            Glide.with(this).load(photoURL).into(profilePicture);

            //Save loggedIn, name, email, and photoURL variables to shared prefs
            SharedPreferences saveData = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = saveData.edit();
            editor.putBoolean(LOGGED_IN,loggedIn);
            editor.putString(NAME,name);
            editor.putString(EMAIL,email);
            editor.putString(PHOTO_URL,photoURL);

            //Need to apply() the editor at the end
            editor.apply();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}