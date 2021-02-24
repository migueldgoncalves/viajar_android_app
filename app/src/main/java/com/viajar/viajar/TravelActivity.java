package com.viajar.viajar;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viajar.viajar.database.Location;

import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SQLDatabase sqlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        populateDatabase();
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

        // Add a marker in Guerreiros do Rio and move the camera
        LatLng guerreiros = new LatLng(37.396353, -7.446837);
        mMap.addMarker(new MarkerOptions().position(guerreiros).title("Marker in Guerreiros do Rio"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(guerreiros));
    }

    public void populateDatabase() {
        getDatabase();
        List<Location> locationArray = new ArrayList<>();
        System.out.println(this.sqlDatabase.locationDAO().getLocationNumber());
    }

    public SQLDatabase getDatabase() {
        if (sqlDatabase == null) {
            this.sqlDatabase = Room.databaseBuilder(getApplicationContext(), SQLDatabase.class, getString(R.string.app_name)).build();
        }
        return this.sqlDatabase;
    }
}