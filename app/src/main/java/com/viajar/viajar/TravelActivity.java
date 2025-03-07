package com.viajar.viajar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.viajar.viajar.utils.RegionBoundsManager;
import com.viajar.viajar.utils.RouteColorGetter;
import com.viajar.viajar.utils.Utils;
import com.viajar.viajar.views.DestinationButtonView;
import com.viajar.viajar.views.DestinationsCustomView;
import com.viajar.viajar.activities.InfoPageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TravelActivity extends AppCompatActivity implements OnMapsSdkInitializedCallback {

    public static final String EAST = "E";
    public static final String NORTH = "N";
    public static final String NORTHEAST = "NE";
    public static final String NORTHWEST = "NW";
    public static final String SOUTH = "S";
    public static final String SOUTHEAST = "SE";
    public static final String SOUTHWEST = "SW";
    public static final String WEST = "W";

    public static final String CAR = "Car";
    public static final String BOAT = "Boat";
    public static final String SHIP = "Ship";
    public static final String TRAIN = "Train";
    public static final String HIGH_SPEED_TRAIN = "High-Speed Train";
    public static final String PLANE = "Plane";
    public static final String TRANSFER = "Transfer";
    public static final String SUBWAY = "Subway";
    public static final String HIKING = "Hiking";

    public static final int TAB_NUMBER = 3;

    // HISTORY MODE SETTINGS - Change here
    private static final boolean historyMode = false; // Set to true to show location subset in map tab
    private static final int desiredBatch = 1900; // Locations to show in history mode, 100 = First 100 locations to be added
    private static final boolean useMarkers = false; // True - Markers are placed on the map; False - Connections are drawn instead

    private LocationInfo currentLocation;
    private LocationInfo formerLocation;
    private HashMap<String, LocationInfo> surroundingLocations;
    private String currentLocationName;
    private String currentTransportMeans;
    private String currentMapArea;
    private ArrayList<String[]> allCoordinatesNamesBatches;
    private ArrayList<String[]> allConnectionsCoordinates;
    private Double northernmostLatitude;
    private Double southernmostLatitude;
    private Double westernmostLongitude;
    private Double easternmostLongitude;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);

        setContentView(R.layout.activity_travel);

        viewPager2 = findViewById(R.id.travelPager);
        FragmentStateAdapter pagerAdapter = new TravelPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

        // Code related to the toolbar
        Toolbar mToolbar = findViewById(R.id.travelToolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> {
            //What to do on back clicked
            onBackPressed();
        });

        tabLayout = findViewById(R.id.travelTabLayout);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentLocationName = getString(R.string.default_initial_location);
        currentTransportMeans = TravelActivity.CAR;

        BottomNavigationView locationButtons = findViewById(R.id.bottomNavigationViewGPS);
        locationButtons.setOnItemSelectedListener(item -> {
            if (currentLocation == null)
                return false;
            if (item.getItemId() == R.id.car) {
                currentTransportMeans = CAR;
            }
            else if (item.getItemId() == R.id.boat) {
                currentTransportMeans = BOAT;
            }
            else if (item.getItemId() == R.id.ship) {
                currentTransportMeans = SHIP;
            }
            else if (item.getItemId() == R.id.train) {
                currentTransportMeans = TRAIN;
            }
            else if (item.getItemId() == R.id.plane) {
                currentTransportMeans = PLANE;
            }
            else if (item.getItemId() == R.id.highSpeedTrain) {
                currentTransportMeans = HIGH_SPEED_TRAIN;
            }
            else if (item.getItemId() == R.id.transfer) {
                currentTransportMeans = TRANSFER;
            }
            else if (item.getItemId() == R.id.subway) {
                currentTransportMeans = SUBWAY;
            }
            else if (item.getItemId() == R.id.hiking) {
                currentTransportMeans = HIKING;
            }
            else {
                return false;
            }
            // It seems fragment 0 is CarFragment and 1 is GPSFragment
            runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(1)).createMapMarkers());
            runOnUiThread(this::updateUI);
            return true;
        });
        DBInterface.deleteDatabase(getApplicationContext());
        populateDatabase(); // Sets UI with initial location info
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Add to any activity that requires options in toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("Travel Activity", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("Travel Activity", "The legacy version of the renderer is used.");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.travelCarContainerView).getVisibility() == View.VISIBLE)
            ((CarFragment) getSupportFragmentManager().getFragments().get(0)).finishJourney();
        else if (viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    private static class TravelPagerAdapter extends FragmentStateAdapter {
        public TravelPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case (0):
                    return new GPSPageFragment();
                case (1):
                    return new InfoPageFragment();
                case (2):
                    return new MapPageFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return TAB_NUMBER;
        }
    }

    public void populateDatabase() {
        new Thread(() -> {
            populateCurrentAndSurroundingLocations(true);
            runOnUiThread(this::updateUI);
        }).start();
    }

    public void updateUI() {
        if (currentLocation == null) {
            return;
        }

        TextView locationTextViewGPS = findViewById(R.id.locationTextViewGPS);
        TextView briefInfoTextView = findViewById(R.id.briefInfoTextView);
        if (locationTextViewGPS != null)
            locationTextViewGPS.setText(currentLocationName);
        if ((briefInfoTextView != null) && (currentLocation != null))
            if (currentLocation.getCountry().equals(getString(R.string.portugal)))
                if (Arrays.asList("Açores", "Madeira").contains(((LocationInfoPortugal) currentLocation).getDistrict()))
                    briefInfoTextView.setText(getString(R.string.brief_info_pt_acores_madeira, ((LocationInfoPortugal) currentLocation).getMunicipality(), ((LocationInfoPortugal) currentLocation).getDistrict()));
                else
                    briefInfoTextView.setText(getString(R.string.brief_info_pt_continental_portugal, ((LocationInfoPortugal) currentLocation).getMunicipality(), ((LocationInfoPortugal) currentLocation).getDistrict()));
            else if (currentLocation.getCountry().equals(getString(R.string.spain)))
                if (Utils.isAutonomousCommunityWithSingleProvince(((LocationInfoSpain) currentLocation).getAutonomousCommunity(), ((LocationInfoSpain) currentLocation).getProvince()))
                    briefInfoTextView.setText(getString(R.string.brief_info_es_uniprovince, ((LocationInfoSpain) currentLocation).getMunicipality(), ((LocationInfoSpain) currentLocation).getProvince()));
                else
                    briefInfoTextView.setText(getString(R.string.brief_info_es_multiprovince, ((LocationInfoSpain) currentLocation).getMunicipality(), ((LocationInfoSpain) currentLocation).getProvince(), ((LocationInfoSpain) currentLocation).getAutonomousCommunity()));
            else if (currentLocation.getCountry().equals(getString(R.string.gibraltar_short_name)))
                briefInfoTextView.setText(getString(R.string.brief_info_gi));
            else if (currentLocation.getCountry().equals(getString(R.string.andorra)))
                briefInfoTextView.setText(getString(R.string.brief_info_ad, ((LocationInfoAndorra) currentLocation).getParish()));
            else { // Location beyond Iberian Peninsula
                briefInfoTextView.setText(((LocationInfoBeyondIberianPeninsula) currentLocation).getOsmAdminLevelString());
            }

        LinearLayout buttonLayoutGPS = findViewById(R.id.locationButtonLayoutGPS);
        LinearLayout destinationLayoutGPS = findViewById(R.id.destinationLayoutGPS);
        if (buttonLayoutGPS != null) {
            buttonLayoutGPS.removeAllViewsInLayout();
            destinationLayoutGPS.removeAllViewsInLayout();
            buttonLayoutGPS.setOrientation(LinearLayout.VERTICAL);
            destinationLayoutGPS.setOrientation(LinearLayout.VERTICAL);
        }

        int order = 1;
        for (int i = 0; i < currentLocation.getSurroundingLocations().keySet().size(); i++) {
            for (List<String> connectionID : currentLocation.getSurroundingLocations().keySet()) {
                String surroundingLocation = connectionID.get(0);
                String meansTransport = connectionID.get(1);
                if (order == currentLocation.getSurroundingLocationOrder(surroundingLocation, meansTransport)) {
                    if (currentTransportMeans.equals(meansTransport)) {
                        if (buttonLayoutGPS != null) {
                            LocationInfo surroundingLocationInfo = surroundingLocations.get(surroundingLocation);
                            if (surroundingLocationInfo == null) // Error - Not expected
                                continue;

                            DestinationButtonView destinationButtonView = new DestinationButtonView(getApplicationContext(), null);
                            destinationButtonView.setView(currentLocation, currentTransportMeans, surroundingLocationInfo);
                            destinationButtonView.getButton().setOnClickListener(TravelActivity.this::onClickGPS);
                            buttonLayoutGPS.addView(destinationButtonView);
                        }
                    }
                    order += 1;
                }
            }
        }

        if (currentLocation.hasDestinationsInMeansTransport(currentTransportMeans)) {
            DestinationsCustomView algo = new DestinationsCustomView(getApplicationContext(), null);
            algo.setView(currentLocation, currentTransportMeans);
            if (destinationLayoutGPS != null)
                destinationLayoutGPS.addView(algo);
        }

        // Hides all transport means buttons, then shows only buttons of transport means accessible from current location
        // ADD NEW TRANSPORT MEANS HERE

        BottomNavigationView locationButtons = findViewById(R.id.bottomNavigationViewGPS);

        locationButtons.getMenu().findItem(R.id.car).setVisible(false);
        locationButtons.getMenu().findItem(R.id.train).setVisible(false);
        locationButtons.getMenu().findItem(R.id.boat).setVisible(false);
        locationButtons.getMenu().findItem(R.id.ship).setVisible(false);
        locationButtons.getMenu().findItem(R.id.plane).setVisible(false);
        locationButtons.getMenu().findItem(R.id.highSpeedTrain).setVisible(false);
        locationButtons.getMenu().findItem(R.id.transfer).setVisible(false);
        locationButtons.getMenu().findItem(R.id.subway).setVisible(false);
        locationButtons.getMenu().findItem(R.id.hiking).setVisible(false);

        if (currentLocation.getSurroundingLocationsByTransportMeans(CAR).size() > 0)
            locationButtons.getMenu().findItem(R.id.car).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(TRAIN).size() > 0)
            locationButtons.getMenu().findItem(R.id.train).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(BOAT).size() > 0)
            locationButtons.getMenu().findItem(R.id.boat).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(SHIP).size() > 0)
            locationButtons.getMenu().findItem(R.id.ship).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(PLANE).size() > 0)
            locationButtons.getMenu().findItem(R.id.plane).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(HIGH_SPEED_TRAIN).size() > 0)
            locationButtons.getMenu().findItem(R.id.highSpeedTrain).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(TRANSFER).size() > 0)
            locationButtons.getMenu().findItem(R.id.transfer).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(SUBWAY).size() > 0)
            locationButtons.getMenu().findItem(R.id.subway).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(HIKING).size() > 0)
            locationButtons.getMenu().findItem(R.id.hiking).setVisible(true);
    }

    public void onClickGPS(View view) {
        currentLocationName = (String) ((Button) view).getText();

        if (currentLocation != null) {
            new Thread(() -> {
                populateCurrentAndSurroundingLocations(false);

                boolean car_mode_setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.settings_key_car_mode), false);
                if (car_mode_setting)
                    startCarJourney();

                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(1)).createMapMarkers());
                runOnUiThread(this::updateUI);
            }).start();
        }
    }

    public void populateCurrentAndSurroundingLocations(boolean populateFromDatabase) {
        DBInterface dbInterface = DBInterface.getDBInterface(getApplicationContext());
        formerLocation = currentLocation;
        currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
        surroundingLocations = new HashMap<>();
        for(List<String> connectionInfo:currentLocation.getSurroundingLocations().keySet()) {
            String surroundingLocationName = connectionInfo.get(0);
            LocationInfo newSurroundingLocation = DBInterface.getDBInterface(getApplicationContext()).generateLocationObject(getApplicationContext(), surroundingLocationName);
            surroundingLocations.put(surroundingLocationName, newSurroundingLocation);
        }
        if (populateFromDatabase) {
            allCoordinatesNamesBatches = dbInterface.getAllCoordinatesNamesBatches(getApplicationContext());
            allConnectionsCoordinates = dbInterface.getAllConnectionCoordinates(getApplicationContext());
            northernmostLatitude = dbInterface.getMapNorthernmostLatitude(getApplicationContext());
            southernmostLatitude = dbInterface.getMapSouthernmostLatitude(getApplicationContext());
            westernmostLongitude = dbInterface.getMapWesternmostLongitude(getApplicationContext());
            easternmostLongitude = dbInterface.getMapEasternmostLongitude(getApplicationContext());
        }
    }

    public void startCarJourney() {
        String routeName = formerLocation.getRouteName(currentLocationName, currentTransportMeans);
        int routeColor = RouteColorGetter.getRouteLineColor(routeName, currentTransportMeans);

        Bundle args = new Bundle();
        double[] latList = new double[]{formerLocation.getLatitude(), currentLocation.getLatitude()};
        double[] lonList = new double[]{formerLocation.getLongitude(), currentLocation.getLongitude()};
        args.putDoubleArray("latList", latList);
        args.putDoubleArray("lonList", lonList);
        args.putInt("color", routeColor);

        // Visually, replaces GPS fragment with Car fragment
        View carFragment = findViewById(R.id.travelCarContainerView);
        runOnUiThread(() -> carFragment.setVisibility(View.VISIBLE));
        viewPager2 = findViewById(R.id.travelPager);
        runOnUiThread(() -> viewPager2.setVisibility(View.GONE));
        View bottomNavigationView = findViewById(R.id.bottomNavigationViewGPS);
        runOnUiThread(() -> bottomNavigationView.setVisibility(View.GONE));

        String vehicle = getString(R.string.vehicle_car);
        ((CarFragment) getSupportFragmentManager().getFragments().get(0)).setCar(vehicle);
        ((CarFragment) getSupportFragmentManager().getFragments().get(0)).startJourney(args);
    }

    public LocationInfo getCurrentLocation() {
        return currentLocation;
    }

    public String getCurrentLocationName() {
        return currentLocationName;
    }

    // Fragment classes

    public static class GPSPageFragment extends Fragment implements OnMapReadyCallback {

        MapView mMapView;
        private GoogleMap mMap;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_travel_gps, container, false);

            mMapView = rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            mMapView.getMapAsync(this);

            return rootView;
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
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            createMapMarkers();
        }

        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();

            // Makes only the GPS bottom navigation bar visible
            View bottomNavigationViewGPS = requireActivity().findViewById(R.id.bottomNavigationViewGPS);
            requireActivity().runOnUiThread(() -> bottomNavigationViewGPS.setVisibility(View.VISIBLE));
            View bottomNavigationViewMap = requireActivity().findViewById(R.id.bottomNavigationViewMap);
            requireActivity().runOnUiThread(() -> bottomNavigationViewMap.setVisibility(View.INVISIBLE));

            requireActivity().runOnUiThread(() -> ((TravelActivity) requireActivity()).updateUI());
        }


        @Override
        public void onPause() {
            super.onPause();
            mMapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mMapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mMapView.onLowMemory();
        }

        public void createMapMarkers() {
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            HashMap<String, LocationInfo> surroundingLocations = ((TravelActivity) requireActivity()).surroundingLocations;
            String currentLocationName = ((TravelActivity) requireActivity()).currentLocationName;
            String currentTransportMeans = ((TravelActivity) requireActivity()).currentTransportMeans;

            mMap.clear();
            LatLngBounds.Builder b = new LatLngBounds.Builder();
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            b.include(location);
            mMap.addMarker(new MarkerOptions().position(location).title(currentLocationName));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.setMinZoomPreference(ZOOM_LEVEL);

            for(LocationInfo surroundingLocation: surroundingLocations.values()) {
                LatLng surroundingLocationCoordinates = new LatLng(surroundingLocation.getLatitude(), surroundingLocation.getLongitude());
                float markerColor;
                if (currentLocation.getSurroundingLocationsByTransportMeans(currentTransportMeans).contains(surroundingLocation.getName())) {
                    markerColor = BitmapDescriptorFactory.HUE_BLUE;
                    b.include(surroundingLocationCoordinates);
                }
                else
                    markerColor = BitmapDescriptorFactory.HUE_CYAN;
                mMap.addMarker(new MarkerOptions().position(surroundingLocationCoordinates).title(surroundingLocation.getName()).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            }
            LatLngBounds bounds = b.build();
            CameraUpdate c = CameraUpdateFactory.newLatLngBounds(bounds,150);
            mMap.animateCamera(c);
            //mMap.moveCamera(c);
        }
    }

    public static class MapPageFragment extends Fragment implements OnMapReadyCallback {

        private static final int largeIconSize = 350;
        private static final int lineWidth = 5;
        private static int mapPadding = 20;
        private static final double aroundMapSize = 0.15 / 2; // Degrees - In Iberian Peninsula 1 degree = ~100 km
        private static final int defaultMapAreaID = R.id.around;

        MapView mMapView;
        private GoogleMap mMap;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_travel_map, container, false);

            mMapView = rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            mMapView.getMapAsync(this);

            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            BottomNavigationView mapAreaButtons = requireActivity().findViewById(R.id.bottomNavigationViewMap);
            mapAreaButtons.setOnNavigationItemSelectedListener(item -> {
                if (currentLocation == null)
                    return false;
                if (item.getItemId() == R.id.global) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.global);
                    requireActivity().runOnUiThread(this::frameMap);
                    return true;
                }
                else if (item.getItemId() == R.id.region) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.region);
                    requireActivity().runOnUiThread(this::frameMap);
                    return true;
                }
                else if (item.getItemId() == R.id.subregion) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.subregion);
                    requireActivity().runOnUiThread(this::frameMap);
                    return true;
                }
                else if (item.getItemId() == R.id.around) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.around);
                    requireActivity().runOnUiThread(this::frameMap);
                    return true;
                }
                return false;
            });

            return rootView;
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
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            drawMap();
        }

        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();

            // Makes only the map bottom navigation bar visible
            BottomNavigationView bottomNavigationViewGPS = requireActivity().findViewById(R.id.bottomNavigationViewGPS);
            requireActivity().runOnUiThread(() -> bottomNavigationViewGPS.setVisibility(View.INVISIBLE));
            BottomNavigationView bottomNavigationViewMap = requireActivity().findViewById(R.id.bottomNavigationViewMap);
            requireActivity().runOnUiThread(() -> bottomNavigationViewMap.setVisibility(View.VISIBLE));

            bottomNavigationViewMap.setSelectedItemId(defaultMapAreaID);

            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            TextView textView = requireActivity().findViewById(R.id.locationTextViewMap);
            textView.setText(currentLocation.getName());

            requireActivity().runOnUiThread(this::drawMap);
        }


        @Override
        public void onPause() {
            super.onPause();
            mMapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mMapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mMapView.onLowMemory();
        }

        public void drawMap() {
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            String currentTransportMeans = ((TravelActivity) requireActivity()).currentTransportMeans;
            ArrayList<String[]> allCoordinatesNamesBatches = ((TravelActivity) requireActivity()).allCoordinatesNamesBatches;
            ArrayList<String[]> allConnectionsCoordinates = ((TravelActivity) requireActivity()).allConnectionsCoordinates;

            if (mMap == null)
                return;

            mMap.clear();
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(currentLocation.getName()).icon(BitmapDescriptorFactory.defaultMarker()));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.setMinZoomPreference(ZOOM_LEVEL);

            frameMap(); // Frames the map according to the selected region

            if (TravelActivity.historyMode) { // Show some connections or locations, and only if inside desired batch
                if (TravelActivity.useMarkers) { // Draws markers
                    mapPadding = 150;
                    for (String[] coordinateNameBatch : allCoordinatesNamesBatches) {
                        double latitude = Double.parseDouble(coordinateNameBatch[0]);
                        double longitude = Double.parseDouble(coordinateNameBatch[1]);
                        String name = coordinateNameBatch[2];
                        int batch = Integer.parseInt(coordinateNameBatch[3]);
                        if ((batch > 0) && (batch <= TravelActivity.desiredBatch))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name));
                    }
                } else { // Draws connections - Note: Some connections may not appear for smaller batches
                    mapPadding = 50;
                    for (String[] connectionCoordinates : allConnectionsCoordinates) {
                        Double latitude1 = Double.valueOf(connectionCoordinates[0]);
                        Double longitude1 = Double.valueOf(connectionCoordinates[1]);
                        Double latitude2 = Double.valueOf(connectionCoordinates[2]);
                        Double longitude2 = Double.valueOf(connectionCoordinates[3]);
                        String routeName = connectionCoordinates[4];
                        String meansTransport = connectionCoordinates[5];

                        // Transport means not to draw on the global map
                        if (meansTransport.equals(PLANE))
                            continue;

                        int lineColor = RouteColorGetter.getRouteLineColor(routeName, meansTransport);
                        boolean locationOneInBatch = false;
                        boolean locationTwoInBatch = false;
                        for (String[] coordinateNameBatch : allCoordinatesNamesBatches) {
                            double latitude = Double.parseDouble(coordinateNameBatch[0]);
                            double longitude = Double.parseDouble(coordinateNameBatch[1]);
                            int batch = Integer.parseInt(coordinateNameBatch[3]);
                            if ((batch > 0) && (batch <= TravelActivity.desiredBatch)) {
                                if ((latitude == latitude1) && (longitude == longitude1))
                                    locationOneInBatch = true;
                                else if ((latitude == latitude2) && (longitude == longitude2))
                                    locationTwoInBatch = true;
                            }
                        }
                        if (locationOneInBatch && locationTwoInBatch) {
                            mMap.addPolyline(new PolylineOptions().add(
                                    new LatLng(latitude1, longitude1),
                                    new LatLng(latitude2, longitude2))
                                    .width(lineWidth).color(lineColor));
                        }
                    }
                }
            } else { // Show current map. To reduce system load, only the connections to surrounding locations + the current routes if any, will be drawn
                for (String[] connectionCoordinates : allConnectionsCoordinates) {
                    Double latitude1 = Double.valueOf(connectionCoordinates[0]);
                    Double longitude1 = Double.valueOf(connectionCoordinates[1]);
                    Double latitude2 = Double.valueOf(connectionCoordinates[2]);
                    Double longitude2 = Double.valueOf(connectionCoordinates[3]);
                    String routeName = connectionCoordinates[4];
                    String meansTransport = connectionCoordinates[5];

                    // Draw only the connections available at this location OR the connections belonging to the routes where the current location is
                    if (!currentLocation.isSameLocation(latitude1, longitude1) && !currentLocation.isSameLocation(latitude2, longitude2)) {
                        if (!currentLocation.getRouteNames().contains(routeName)) {
                            continue;
                        }
                    }

                    int lineColor = RouteColorGetter.getRouteLineColor(routeName, meansTransport);
                    mMap.addPolyline(new PolylineOptions().add(
                            new LatLng(latitude1, longitude1),
                            new LatLng(latitude2, longitude2))
                            .width(lineWidth).color(lineColor));
                }
            }
        }

        public void frameMap() {
            String currentMapArea = ((TravelActivity) requireActivity()).currentMapArea;
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            if (currentMapArea == null)
                return;
            else if (currentMapArea.equals(getString(R.string.global))) {
                if (TravelActivity.historyMode) { // Include only locations inside desired batch
                    ArrayList<String[]> allCoordinatesNamesBatches = ((TravelActivity) requireActivity()).allCoordinatesNamesBatches;
                    for (String[] coordinateNameBatch : allCoordinatesNamesBatches) {
                        double latitude = Double.parseDouble(coordinateNameBatch[0]);
                        double longitude = Double.parseDouble(coordinateNameBatch[1]);
                        int batch = Integer.parseInt(coordinateNameBatch[3]);
                        if ((batch > 0) && (batch <= TravelActivity.desiredBatch))
                            b.include(new LatLng(latitude, longitude));
                    }
                } else { // Include all locations in the same macro-region (i.e. Azores Islands, Madeira Islands, Canary Islands, or Iberian Peninsula + Balearic Islands)
                    LatLng[] bounds = new RegionBoundsManager(getContext()).getGlobalBoundsByLocation(currentLocation);
                    for (LatLng bound : bounds)
                        b.include(bound);
                }
            } else if (currentMapArea.equals(getString(R.string.subregion))) {
                LatLng[] bounds = new RegionBoundsManager(getContext()).getSubregionBoundsByLocation(currentLocation);
                for (LatLng bound : bounds)
                    b.include(bound);
            } else if (currentMapArea.equals(getString(R.string.region))) {
                LatLng[] bounds = new RegionBoundsManager(getContext()).getRegionBoundsByLocation(currentLocation);
                for (LatLng bound : bounds)
                    b.include(bound);
            } else if (currentMapArea.equals(getString(R.string.around))) {
                b.include(new LatLng(currentLocation.getLatitude() + aroundMapSize, currentLocation.getLongitude())); // North
                b.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude() - aroundMapSize)); // West
                b.include(new LatLng(currentLocation.getLatitude() - aroundMapSize, currentLocation.getLongitude())); // South
                b.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude() + aroundMapSize)); // East
            } else { // Invalid string
                return;
            }

            LatLngBounds bounds = b.build();
            CameraUpdate c = CameraUpdateFactory.newLatLngBounds(bounds, mapPadding);
            if (mMap != null)
                mMap.animateCamera(c);
                //mMap.moveCamera(c);
        }
    }
}
