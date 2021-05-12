package com.viajar.viajar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ZOOM_LEVEL = 10;

    public static final String EAST = "E";
    public static final String NORTH = "N";
    public static final String NORTHEAST = "NE";
    public static final String NORTHWEST = "NO"; // PT - Noroeste
    public static final String SOUTH = "S";
    public static final String SOUTHEAST = "SE";
    public static final String SOUTHWEST = "SO"; // PT - Sudoeste
    public static final String WEST = "O"; // PT - Oeste

    public static final String CAR = "Carro";
    public static final String BOAT = "Barco";
    public static final String TRAIN = "Comboio";
    public static final String PLANE = "Avião";

    private GoogleMap mMap;
    private LocationInfo currentLocation;
    private ArrayList<LocationInfo> surroudingLocations;
    private String currentLocationName;
    private String currentTransportMeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        currentLocationName = getString(R.string.default_initial_location);
        currentTransportMeans = TravelActivity.CAR;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView locationButtons = findViewById(R.id.bottomNavigationView);
        locationButtons.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.car) {
                currentTransportMeans = CAR;
                runOnUiThread(this::createMapMarkers);
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.boat) {
                currentTransportMeans = BOAT;
                runOnUiThread(this::createMapMarkers);
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.train) {
                currentTransportMeans = TRAIN;
                runOnUiThread(this::createMapMarkers);
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.plane) {
                currentTransportMeans = PLANE;
                runOnUiThread(this::createMapMarkers);
                runOnUiThread(this::updateUI);
                return true;
            }
            return false;
        });

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
            float markerColor;
            if (currentLocation.getSurroundingLocationsByTransportMeans(currentTransportMeans).contains(surroundingLocation.getName()))
                markerColor = BitmapDescriptorFactory.HUE_BLUE;
            else
                markerColor = BitmapDescriptorFactory.HUE_CYAN;
            mMap.addMarker(new MarkerOptions().position(surroundingLocationCoordinates).title(surroundingLocation.getName()).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
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

        int order = 1;
        for(int i=0; i<currentLocation.getSurroundingLocations().keySet().size(); i++)
            for(List<String> connectionID:currentLocation.getSurroundingLocations().keySet()) {
                String surroundingLocation = connectionID.get(0);
                String meansTransport = connectionID.get(1);
                if (order == currentLocation.getSurroundingLocationOrder(surroundingLocation, meansTransport)) {
                    if (currentTransportMeans.equals(meansTransport)) {
                        Button locationButton = new Button(getApplicationContext());
                        locationButton.setText(surroundingLocation);
                        locationButton.setOnClickListener(TravelActivity.this::onClick);
                        buttonLayout.addView(locationButton);
                    }
                    order += 1;
                }
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
        for(List<String> connectionInfo:currentLocation.getSurroundingLocations().keySet()) {
            String surroundingLocationName = connectionInfo.get(0);
            surroudingLocations.add(DBInterface.getDBInterface(getApplicationContext()).generateLocationObject(getApplicationContext(), surroundingLocationName));
        }
    }
}