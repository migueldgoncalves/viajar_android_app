package com.viajar.viajar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelActivity extends AppCompatActivity {

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

    public static final int TAB_NUMBER = 2;

    private LocationInfo currentLocation;
    private ArrayList<LocationInfo> surroundingLocations;
    private String currentLocationName;
    private String currentTransportMeans;

    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        viewPager2 = findViewById(R.id.travelPager);
        pagerAdapter = new TravelPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.travelTabLayout);
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

        BottomNavigationView locationButtons = findViewById(R.id.bottomNavigationView);
        locationButtons.setOnNavigationItemSelectedListener(item -> {
            if (currentLocation == null)
                return false;
            if (item.getItemId() == R.id.car) {
                currentTransportMeans = CAR;
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.boat) {
                currentTransportMeans = BOAT;
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.train) {
                currentTransportMeans = TRAIN;
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
                return true;
            }
            else if (item.getItemId() == R.id.plane) {
                currentTransportMeans = PLANE;
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
                return true;
            }
            return false;
        });

        populateDatabase(); // Sets UI with initial location info
    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    private class TravelPagerAdapter extends FragmentStateAdapter {
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
            runOnUiThread(() -> ((GPSPageFragment) Objects.requireNonNull(getSupportFragmentManager().getFragments().get(0))).createMapMarkers());
            runOnUiThread(this::updateUI);
        }).start();
    }

    public void updateUI() {
        TextView locationTextView = (TextView) findViewById(R.id.locationTextViewGPS);
        locationTextView.setText(currentLocationName);

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.locationButtonLayout);
        buttonLayout.removeAllViewsInLayout();
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        int order = 1;
        for (int i = 0; i < currentLocation.getSurroundingLocations().keySet().size(); i++)
            for (List<String> connectionID : currentLocation.getSurroundingLocations().keySet()) {
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

        if (currentLocation != null) {
            new Thread(() -> {
                populateCurrentAndSurroundingLocations(false);
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
            }).start();
        }
    }

    public void populateCurrentAndSurroundingLocations(boolean populateDatabase) {
        DBInterface dbInterface = DBInterface.getDBInterface(getApplicationContext());
        if (populateDatabase) {
            dbInterface.populateDatabase(getApplicationContext());
        }
        currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
        surroundingLocations = new ArrayList<>();
        for(List<String> connectionInfo:currentLocation.getSurroundingLocations().keySet()) {
            String surroundingLocationName = connectionInfo.get(0);
            surroundingLocations.add(DBInterface.getDBInterface(getApplicationContext()).generateLocationObject(getApplicationContext(), surroundingLocationName));
        }
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
        }

        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();
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
            LocationInfo currentLocation = ((TravelActivity) getActivity()).currentLocation;
            ArrayList<LocationInfo> surroundingLocations = ((TravelActivity) getActivity()).surroundingLocations;
            String currentLocationName = ((TravelActivity) getActivity()).currentLocationName;
            String currentTransportMeans = ((TravelActivity) getActivity()).currentTransportMeans;

            mMap.clear();
            LatLngBounds.Builder b = new LatLngBounds.Builder();
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            b.include(location);
            mMap.addMarker(new MarkerOptions().position(location).title(currentLocationName));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.setMinZoomPreference(ZOOM_LEVEL);

            for(LocationInfo surroundingLocation: surroundingLocations) {
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
            mMap.animateCamera(c);
            //mMap.moveCamera(c);
        }
    }

    public static class InfoPageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_travel_info, container, false);
            return rootView;
        }

        @Override
        public void onResume() {
            LocationInfo currentLocation = ((TravelActivity) getActivity()).currentLocation;
            String currentLocationName = ((TravelActivity) getActivity()).currentLocationName;

            super.onResume();
            TextView textView = ((TravelActivity) getActivity()).findViewById(R.id.locationTextViewInfo);
            textView.setText(currentLocationName);
            EditText editText = ((TravelActivity) getActivity()).findViewById(R.id.travelInfoText);
            editText.setText("");
            if (currentLocation != null) {
                editText.append(currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + "\n");
                if (currentLocation.getAltitude() == 1)
                    editText.append(currentLocation.getAltitude() + " metro\n");
                else
                    editText.append(currentLocation.getAltitude() + " metros\n");
                if (currentLocation.getProtectedArea() != null)
                    editText.append(currentLocation.getProtectedArea() + "\n");
                editText.append("\n");
                if (currentLocation.getCountry().equals("Portugal")) {
                    editText.append("Freguesia: " + ((LocationInfoPortugal) currentLocation).getParish() + "\n");
                    editText.append("Concelho: " + ((LocationInfoPortugal) currentLocation).getMunicipality() + "\n");
                    editText.append("Distrito: " + ((LocationInfoPortugal) currentLocation).getDistrict() + "\n");
                    editText.append("Entidade Intermunicipal: " + ((LocationInfoPortugal) currentLocation).getIntermunicipalEntity() + "\n");
                    editText.append("Região: " + ((LocationInfoPortugal) currentLocation).getRegion());
                } else if (currentLocation.getCountry().equals("Spain")) {
                    if (((LocationInfoSpain) currentLocation).getDistrict() != null)
                        editText.append("Distrito: " + ((LocationInfoSpain) currentLocation).getDistrict() + "\n");
                    editText.append("Município: " + ((LocationInfoSpain) currentLocation).getMunicipality() + "\n");
                    if (((LocationInfoSpain) currentLocation).getAutonomousCommunity().equals("Extremadura")) {
                        if (((LocationInfoSpain) currentLocation).getComarcas().size() == 1)
                            editText.append("Mancomunidade integral: " + ((LocationInfoSpain) currentLocation).getComarcas().get(0) + "\n");
                        else if (((LocationInfoSpain) currentLocation).getComarcas().size() == 0)
                            editText.append("Mancomunidade integral: Nenhuma\n");
                    } else {
                        if (((LocationInfoSpain) currentLocation).getComarcas().size() == 2)
                            editText.append("Comarcas: " + ((LocationInfoSpain) currentLocation).getComarcas().get(0) +
                                    ", " + ((LocationInfoSpain) currentLocation).getComarcas().get(1) + "\n");
                        else if (((LocationInfoSpain) currentLocation).getComarcas().size() == 1)
                            editText.append("Comarca: " + ((LocationInfoSpain) currentLocation).getComarcas().get(0) + "\n");
                        else
                            editText.append("Comarca: Nenhuma\n");
                    }
                    editText.append("Província: " + ((LocationInfoSpain) currentLocation).getProvince() + "\n");
                    editText.append("Comunidade Autónoma: " + ((LocationInfoSpain) currentLocation).getAutonomousCommunity());
                }
            }
        }
    }

}