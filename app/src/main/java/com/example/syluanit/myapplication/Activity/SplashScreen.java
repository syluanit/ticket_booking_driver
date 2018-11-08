package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.syluanit.myapplication.Service.Database;

public class SplashScreen extends AppCompatActivity {

    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new Database(this, "driver.sqlite", null, 1);
//        database.queryData("Drop table IF exists Ticket");

        Cursor data = database.getDaTa("SELECT * FROM sqlite_master WHERE name ='User' and type='table'");

        if (data.getCount() > 0) {
            Intent intent = new Intent(SplashScreen.this, Home.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SplashScreen.this, SignIn.class);
            startActivity(intent);
            finish();
        }
    }
}
