package com.viajar.viajar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        // Code related to the toolbar
        Toolbar mToolbar = findViewById(R.id.carToolbar);
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

    @Override
    protected void onResume() {
        super.onResume();

        View carFragment = findViewById(R.id.carContainerView);
        runOnUiThread(() -> carFragment.setVisibility(View.VISIBLE));

        ((CarFragment) getSupportFragmentManager().getFragments().get(0)).setCar(getIntent().getStringExtra("vehicle"));
        ((CarFragment) getSupportFragmentManager().getFragments().get(0)).startJourney(null);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}