package com.viajar.viajar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Code related to the toolbar
        Toolbar mToolbar = findViewById(R.id.mainMenuToolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> {
            //What to do on back clicked
            onBackPressed();
        });
    }

    public void moveTravelActivity(View view){
        Intent intent = new Intent(this, TravelActivity.class);
        startActivity(intent);
    }

    public void moveCarActivity(View view) {
        Intent intent = new Intent(this, CarMenuActivity.class);
        startActivity(intent);
    }
}