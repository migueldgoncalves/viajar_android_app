package com.viajar.viajar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TravelActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ZOOM_LEVEL = 10;

    private GoogleMap mMap;
    private LocationInfo currentLocation;
    private ArrayList<LocationInfo> surroudingLocations;
    private String currentLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        currentLocationName = getString(R.string.default_initial_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        populateDatabase(); // Sets UI with initial location info
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void createMapMarkers() {
        mMap.clear();
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        b.include(location);
        mMap.addMarker(new MarkerOptions().position(location).title(currentLocationName));
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMinZoomPreference(ZOOM_LEVEL);

        for(LocationInfo surroundingLocation:surroudingLocations) {
            LatLng surroundingLocationCoordinates = new LatLng(surroundingLocation.getLatitude(), surroundingLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(surroundingLocationCoordinates).title(surroundingLocation.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            b.include(surroundingLocationCoordinates);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate c = CameraUpdateFactory.newLatLngBounds(bounds,150);
        //mMap.animateCamera(c);
        //mMap.moveCamera(c);
    }

    public void populateDatabase() {
        new Thread(() -> {
            populateCurrentAndSurroundingLocations(true);
            runOnUiThread(this::createMapMarkers);
            runOnUiThread(this::updateUI);
        }).start();
    }

    public void updateUI() {
        TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
        locationTextView.setText(currentLocationName);

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.locationButtonLayout);
        buttonLayout.removeAllViewsInLayout();
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        for(String surroundingLocation:currentLocation.getSurroundingLocations()) {
            Button locationButton = new Button(getApplicationContext());
            locationButton.setText(surroundingLocation);
            locationButton.setOnClickListener(TravelActivity.this::onClick);
            buttonLayout.addView(locationButton);
        }
    }

    public void onClick(View view) {
        currentLocationName = (String) ((Button) view).getText();

        new Thread(() -> {
            populateCurrentAndSurroundingLocations(false);
            runOnUiThread(this::createMapMarkers);
            runOnUiThread(this::updateUI);
        }).start();
    }

    public void populateCurrentAndSurroundingLocations(boolean populateDatabase) {
        DBInterface dbInterface = DBInterface.getDBInterface(getApplicationContext());
        if (populateDatabase) {
            dbInterface.populateDatabase(getApplicationContext());
        }
        currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
        surroudingLocations = new ArrayList<>();
        for(String surroundingLocationName:currentLocation.getSurroundingLocations()) {
            surroudingLocations.add(DBInterface.getDBInterface(getApplicationContext()).generateLocationObject(getApplicationContext(), surroundingLocationName));
        }
    }
}