package com.viajar.viajar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CarMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_menu);
    }

    public void moveCarActivity(View view) {
        Intent intent = new Intent(this, CarActivity.class);
        intent.putExtra("vehicle", ((Button)view).getText().toString());
        startActivity(intent);
    }
}