package com.alexschwartz.toasttest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Displaying Toast with Hello World message
        Toast.makeText(getApplicationContext(),"Hello World",Toast.LENGTH_SHORT).show();
    }
}