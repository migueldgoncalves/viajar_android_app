package com.viajar.viajar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView version_text = findViewById(R.id.version_text);
        String major_version = getString(R.string.version_major);
        String minor_version = getString(R.string.version_minor);
        String patch_version = getString(R.string.version_patch);
        version_text.setText(getString(R.string.version_text, major_version, minor_version, patch_version));
    }

    public void moveTravelActivity(View view){
        Intent intent = new Intent(this, TravelActivity.class);
        startActivity(intent);
    }
}