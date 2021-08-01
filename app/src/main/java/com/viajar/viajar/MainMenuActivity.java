package com.viajar.viajar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private boolean populatingDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void moveTravelActivity(View view){
        Intent intent = new Intent(this, TravelActivity.class);
        if (!DBInterface.getDeveloperMode())
            startActivity(intent);
        else { // Does not enter Travel Activity in developer mode
            if (!populatingDB) {
                populatingDB = true;
                Toast.makeText(getApplicationContext(), "Starting DB population...", Toast.LENGTH_SHORT).show();
                new Thread(() -> {
                    DBInterface.getDBInterface(getApplicationContext()).populateDatabase(getApplicationContext());
                    populatingDB = false;
                }).start();
            }
        }
    }

    public void moveCarActivity(View view) {
        Intent intent = new Intent(this, CarMenuActivity.class);
        startActivity(intent);
    }
}