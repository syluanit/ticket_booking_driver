package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashScreen.this, Home.class);
        SplashScreen.this.startActivity(intent);
        SplashScreen.this.finish();
    }


}
