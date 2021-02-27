package com.viajar.viajar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TravelActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ZOOM_LEVEL = 10;

    private GoogleMap mMap;
    private LocationInfo currentLocation;
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

    public void createMapMarker() {
        mMap.clear();
        LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(currentLocationName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMinZoomPreference(ZOOM_LEVEL);
    }

    public void populateDatabase() {
        /**AsyncDatabasePopulateTaskRunner taskRunner = new AsyncDatabasePopulateTaskRunner();
        taskRunner.execute(getApplicationContext());
        taskRunner.onPostExecute(getApplicationContext(), currentLocationName, mMap);**/

        new Thread(() -> {
            DBInterface dbInterface = DBInterface.getDBInterface(getApplicationContext());
            dbInterface.populateDatabase(getApplicationContext());
            currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
            runOnUiThread(this::createMapMarker);
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
            DBInterface dbInterface = DBInterface.getDBInterface(getApplicationContext());
            currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
            runOnUiThread(this::createMapMarker);
            runOnUiThread(this::updateUI);
        }).start();
    }

    /**public void getCurrentLocation() {
        AsyncDatabaseUIUpdaterTaskRunner taskRunner = new AsyncDatabaseUIUpdaterTaskRunner();
        taskRunner.onPreExecute(currentLocationName);
        taskRunner.execute(getApplicationContext());
        currentLocation = taskRunner.onPostExecute(getApplicationContext(), mMap);
    }**/
}

// Populates database on activity start, then sets UI with initial location info
/**class AsyncDatabasePopulateTaskRunner extends AsyncTask<Context, String, String> {
    protected String doInBackground(Context... contexts) {
        Context context = contexts[0];
        DBInterface dbInterface = DBInterface.getDBInterface(context);
        dbInterface.populateDatabase(context);
        return null;
    }

    protected void onPostExecute(Context context, String currentLocationName, GoogleMap mMap) {
        AsyncDatabaseUIUpdaterTaskRunner uiUpdater = new AsyncDatabaseUIUpdaterTaskRunner();
        uiUpdater.onPreExecute(currentLocationName);
        uiUpdater.execute(context); // Sets UI with initial location info
        uiUpdater.onPostExecute(context, mMap);
    }
}

// Sets UI with current location info
class AsyncDatabaseUIUpdaterTaskRunner extends AsyncTask<Context, String, String> {
    private LocationInfo location;
    private String locationName;

    protected void onPreExecute(String locationName) {
        this.locationName = locationName;
    }

    protected String doInBackground(Context... contexts) {
        Context context = contexts[0];
        location = DBInterface.getDBInterface(context).generateLocationObject(context, locationName);
        return null;
    }

    protected void onPostExecute(Context context, GoogleMap mMap) {
        // Add a marker in Guerreiros do Rio and move the camera
        LatLng guerreiros = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(guerreiros).title("Marker in " + location.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(guerreiros));
    }
}**/