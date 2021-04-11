package com.viajar.viajar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void moveTravelActivity(View view){
        Intent intent = new Intent(this, TravelActivity.class);
        startActivity(intent);
    }

    public void moveCarActivity(View view) {
        Intent intent = new Intent(this, CarActivity.class);
        startActivity(intent);
    }
}