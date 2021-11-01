package com.viajar.viajar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelActivity extends FragmentActivity {

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
    public static final String HIGH_SPEED_TRAIN = "Comboio de Alta Velocidade";
    public static final String PLANE = "Avião";

    public static final int TAB_NUMBER = 3;

    private LocationInfo currentLocation;
    private ArrayList<LocationInfo> surroundingLocations;
    private String currentLocationName;
    private String currentTransportMeans;
    private String currentMapArea;
    private ArrayList<String[]> allCoordinatesAndNames;
    private ArrayList<String[]> allConnectionsCoordinates;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        viewPager2 = findViewById(R.id.travelPager);
        FragmentStateAdapter pagerAdapter = new TravelPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

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
            else if (item.getItemId() == R.id.highSpeedTrain) {
                currentTransportMeans = HIGH_SPEED_TRAIN;
                runOnUiThread(() -> ((GPSPageFragment) getSupportFragmentManager().getFragments().get(0)).createMapMarkers());
                runOnUiThread(this::updateUI);
                return true;
            }
            return false;
        });
        DBInterface.deleteDatabase(getApplicationContext());
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
            if (currentLocation.getCountry().equals("Portugal"))
                briefInfoTextView.setText(getString(R.string.brief_info_pt, ((LocationInfoPortugal) currentLocation).getMunicipality(), ((LocationInfoPortugal) currentLocation).getDistrict()));
            else if (currentLocation.getCountry().equals("Spain"))
                if (isComunidadeUniprovincial(((LocationInfoSpain) currentLocation).getAutonomousCommunity()))
                    briefInfoTextView.setText(getString(R.string.brief_info_es_uniprovince, ((LocationInfoSpain) currentLocation).getMunicipality(), ((LocationInfoSpain) currentLocation).getProvince()));
                else
                    briefInfoTextView.setText(getString(R.string.brief_info_es_multiprovince, ((LocationInfoSpain) currentLocation).getMunicipality(), ((LocationInfoSpain) currentLocation).getProvince(), ((LocationInfoSpain) currentLocation).getAutonomousCommunity()));
            else if (currentLocation.getCountry().equals("Gibraltar"))
                briefInfoTextView.setText(getString(R.string.brief_info_gi));

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
                        Button locationButton = new Button(getApplicationContext());
                        locationButton.setText(surroundingLocation);
                        if (buttonLayoutGPS != null) {
                            locationButton.setOnClickListener(TravelActivity.this::onClickGPS);
                            buttonLayoutGPS.addView(locationButton);
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
        locationButtons.getMenu().findItem(R.id.plane).setVisible(false);
        locationButtons.getMenu().findItem(R.id.highSpeedTrain).setVisible(false);

        if (currentLocation.getSurroundingLocationsByTransportMeans(CAR).size() > 0)
            locationButtons.getMenu().findItem(R.id.car).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(TRAIN).size() > 0)
            locationButtons.getMenu().findItem(R.id.train).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(BOAT).size() > 0)
            locationButtons.getMenu().findItem(R.id.boat).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(PLANE).size() > 0)
            locationButtons.getMenu().findItem(R.id.plane).setVisible(true);
        if (currentLocation.getSurroundingLocationsByTransportMeans(HIGH_SPEED_TRAIN).size() > 0)
            locationButtons.getMenu().findItem(R.id.highSpeedTrain).setVisible(true);
    }

    public void onClickGPS(View view) {
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
            allCoordinatesAndNames = dbInterface.getAllCoordinatesAndNames(getApplicationContext());
            allConnectionsCoordinates = dbInterface.getAllConnectionCoordinates(getApplicationContext());
        }
        currentLocation = dbInterface.generateLocationObject(getApplicationContext(), currentLocationName);
        surroundingLocations = new ArrayList<>();
        for(List<String> connectionInfo:currentLocation.getSurroundingLocations().keySet()) {
            String surroundingLocationName = connectionInfo.get(0);
            surroundingLocations.add(DBInterface.getDBInterface(getApplicationContext()).generateLocationObject(getApplicationContext(), surroundingLocationName));
        }
    }

    private boolean isComunidadeUniprovincial(String autonomousCommunity) {
        String[] comunidadesUniprovinciales = new String[]{"Comunidade de Madrid"};
        return new ArrayList<>(Arrays.asList(comunidadesUniprovinciales)).contains(autonomousCommunity);
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
            if (!DBInterface.getDeveloperMode())
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
            ArrayList<LocationInfo> surroundingLocations = ((TravelActivity) requireActivity()).surroundingLocations;
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

            for(LocationInfo surroundingLocation: surroundingLocations) {
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

    public static class InfoPageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_travel_info, container, false);
        }

        @Override
        public void onResume() {
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            String currentLocationName = ((TravelActivity) requireActivity()).currentLocationName;

            super.onResume();

            // Hides both bottom navigation bars
            View bottomNavigationViewGPS = requireActivity().findViewById(R.id.bottomNavigationViewGPS);
            requireActivity().runOnUiThread(() -> bottomNavigationViewGPS.setVisibility(View.INVISIBLE));
            View bottomNavigationViewMap = requireActivity().findViewById(R.id.bottomNavigationViewMap);
            requireActivity().runOnUiThread(() -> bottomNavigationViewMap.setVisibility(View.INVISIBLE));

            TextView textView = requireActivity().findViewById(R.id.locationTextViewInfo);
            textView.setText(currentLocationName);
            EditText editText = requireActivity().findViewById(R.id.travelInfoText);
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
                    editText.append("Região: " + ((LocationInfoPortugal) currentLocation).getRegion() + "\n");
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
                    if (!((LocationInfoSpain) currentLocation).getAutonomousCommunity().equals("Comunidade de Madrid"))
                        editText.append("Província: " + ((LocationInfoSpain) currentLocation).getProvince() + "\n");
                    editText.append("Comunidade Autónoma: " + ((LocationInfoSpain) currentLocation).getAutonomousCommunity() + "\n");
                } else if (currentLocation.getCountry().equals("Gibraltar")) {
                    if (((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().size() == 2)
                        editText.append("Major Residential Areas: " + ((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().get(0) +
                                ", " + ((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().get(1) + "\n");
                    else if (((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().size() == 1)
                        editText.append("Major Residential Area: " + ((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().get(0) + "\n");
                }
                if (currentLocation.getCountry().equals("Spain"))
                    editText.append("País: Espanha");
                else if (currentLocation.getCountry().equals("Gibraltar"))
                    editText.append("País: Reino Unido - Gibraltar");
                else if (currentLocation.getCountry().equals("Portugal"))
                    editText.append("País: Portugal");
            }
        }
    }

    public static class MapPageFragment extends Fragment implements OnMapReadyCallback {

        private static final int largeIconSize = 350;
        private static final int lineWidth = 5;
        private static final int defaultLineColor = Color.BLACK;
        private static final int mapPadding = 50;
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
                if (item.getItemId() == R.id.iberianPeninsula) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.iberian_peninsula);
                    requireActivity().runOnUiThread(this::enquadrarMapa);
                    return true;
                }
                else if (item.getItemId() == R.id.region) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.region);
                    requireActivity().runOnUiThread(this::enquadrarMapa);
                    return true;
                }
                else if (item.getItemId() == R.id.around) {
                    ((TravelActivity) requireActivity()).currentMapArea = getString(R.string.around);
                    requireActivity().runOnUiThread(this::enquadrarMapa);
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
            if (!DBInterface.getDeveloperMode())
                desenharMapa();
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

            requireActivity().runOnUiThread(this::desenharMapa);
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

        public void desenharMapa() {
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            ArrayList<String[]> allCoordinatesAndNames = ((TravelActivity) requireActivity()).allCoordinatesAndNames;
            ArrayList<String[]> allConnectionsCoordinates = ((TravelActivity) requireActivity()).allConnectionsCoordinates;

            if (mMap == null)
                return;

            mMap.clear();
            //LatLngBounds.Builder b = new LatLngBounds.Builder();
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            //b.include(location);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.marker_icon);
            //Bitmap largeMarker = Bitmap.createScaledBitmap(bm, largeIconSize, largeIconSize, false);
            //mMap.addMarker(new MarkerOptions().position(location).title(currentLocation.getName()).icon(BitmapDescriptorFactory.fromBitmap(largeMarker)));
            mMap.addMarker(new MarkerOptions().position(location).title(currentLocation.getName()).icon(BitmapDescriptorFactory.defaultMarker()));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            //mMap.setMinZoomPreference(ZOOM_LEVEL);

            for(String[] coordinatesAndName: allCoordinatesAndNames) {
                Double latitude = Double.valueOf(coordinatesAndName[0]);
                Double longitude = Double.valueOf(coordinatesAndName[1]);
                String name = coordinatesAndName[2];

                if (name.equals(currentLocation.getName()))
                    continue; // Marker already added

                LatLng surroundingLocationCoordinates = new LatLng(latitude, longitude);
                float markerColor = BitmapDescriptorFactory.HUE_BLUE;
                //mMap.addMarker(new MarkerOptions().position(surroundingLocationCoordinates).title(name).icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                //b.include(surroundingLocationCoordinates);
            }
            //LatLngBounds bounds = b.build();
            enquadrarMapa(); // Enquadra o mapa de acordo com a região escolhida

            for(String[] connectionCoordinates: allConnectionsCoordinates) {
                Double latitude1 = Double.valueOf(connectionCoordinates[0]);
                Double longitude1 = Double.valueOf(connectionCoordinates[1]);
                Double latitude2 = Double.valueOf(connectionCoordinates[2]);
                Double longitude2 = Double.valueOf(connectionCoordinates[3]);
                String routeName = connectionCoordinates[4];
                String meansTransport = connectionCoordinates[5];

                if (meansTransport.equals(PLANE))
                    continue;

                int lineColor = DestinationsCustomView.getColorByRouteName(routeName, meansTransport);
                if (lineColor == 0)
                    lineColor = defaultLineColor;
                mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(latitude1, longitude1),
                        new LatLng(latitude2, longitude2))
                        .width(lineWidth).color(lineColor));
            }
        }

        public void enquadrarMapa() {
            String currentMapArea = ((TravelActivity) requireActivity()).currentMapArea;
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            if (currentMapArea == null)
                return;
            else if (currentMapArea.equals(getString(R.string.iberian_peninsula))) {
                b.include(new LatLng(43.791278, -7.689167)); // North
                b.include(new LatLng(38.780907, -9.500550)); // West
                b.include(new LatLng(36.000141, -5.610575)); // South
                b.include(new LatLng(42.319428, 3.322223)); // East
            } else if (currentMapArea.equals(getString(R.string.region))) { // PT - Approximately matches NUTS II, ES - Autonomous Communities or parts of them, GI - Gibraltar
                switch (currentLocation.getCountry()) {
                    case "Portugal":
                        String district = ((LocationInfoPortugal) currentLocation).getDistrict();
                        String intermunicipalEntity = ((LocationInfoPortugal) currentLocation).getIntermunicipalEntity();

                        if (district.equals("Faro")) { // Algarve
                            b.include(new LatLng(37.528930, -7.574430)); // North
                            b.include(new LatLng(37.023060, -8.996989)); // West
                            b.include(new LatLng(36.960175, -7.888063)); // South
                            b.include(new LatLng(37.163375, -7.399764)); // East
                        } else if (Arrays.asList("Faro", "Beja", "Évora", "Portalegre").contains(district) || // Alentejo
                                intermunicipalEntity.equals("Alentejo Litoral")) {
                            b.include(new LatLng(39.664015, -7.539616)); // North
                            b.include(new LatLng(38.490404, -8.912161)); // West
                            b.include(new LatLng(37.318961, -8.065657)); // South
                            b.include(new LatLng(38.207785, -6.932024)); // East
                        } else if (Arrays.asList("Área Metropolitana de Lisboa", "Lezíria do Tejo", "Médio Tejo", "Oeste").contains(
                                intermunicipalEntity)) { // Lisboa e Vale do Tejo
                            b.include(new LatLng(39.838531, -8.477847)); // North
                            b.include(new LatLng(38.780907, -9.500550)); // West
                            b.include(new LatLng(38.409289, -9.198756)); // South
                            b.include(new LatLng(39.565853, -7.811094)); // East
                        } else { // Error
                            return;
                        }
                        break;
                    case "Spain":
                        String autonomousCommunity = ((LocationInfoSpain) currentLocation).getAutonomousCommunity();
                        String province = ((LocationInfoSpain) currentLocation).getProvince();

                        if (Arrays.asList("Huelva", "Sevilha", "Córdoba", "Cádiz").contains(province)) { // Western Andalucía
                            b.include(new LatLng(38.729087, -5.046943)); // North
                            b.include(new LatLng(37.555508, -7.522651)); // West
                            b.include(new LatLng(36.000141, -5.610575)); // South
                            b.include(new LatLng(37.401959, -4.001265)); // East
                        } else if (Arrays.asList("Málaga", "Jaén", "Granada", "Almería").contains(province)) { // Eastern Andalucía
                            b.include(new LatLng(38.533100, -2.767157)); // North
                            b.include(new LatLng(36.539470, -5.609127)); // West
                            b.include(new LatLng(36.310385, -5.249473)); // South
                            b.include(new LatLng(37.375263, -1.630213)); // East
                        } else if (autonomousCommunity.equals("Extremadura")) { // Extremadura
                            b.include(new LatLng(40.486650, -6.234850)); // North
                            b.include(new LatLng(39.663752, -7.539327)); // West
                            b.include(new LatLng(37.941153, -6.180359)); // South
                            b.include(new LatLng(39.168453, -4.647907)); // East
                        } else if (Arrays.asList("Ciudad Real", "Toledo").contains(province)) { // Western Castilla-La Mancha
                            b.include(new LatLng(40.318335, -4.380068)); // North
                            b.include(new LatLng(39.877307, -5.406371)); // West
                            b.include(new LatLng(38.342696, -4.287579)); // South
                            b.include(new LatLng(38.734878, -2.638243)); // East
                        } else if (autonomousCommunity.equals("Comunidade de Madrid")) { // Madrid
                            b.include(new LatLng(41.165731, -3.543958)); // North
                            b.include(new LatLng(40.217163, -4.579124)); // West
                            b.include(new LatLng(39.884752, -3.804706)); // South
                            b.include(new LatLng(40.099441, -3.053298)); // East
                        } else { // Error
                            return;
                        }
                        break;
                    case "Gibraltar":
                        b.include(new LatLng(36.155101, -5.345433)); // North
                        b.include(new LatLng(36.142543, -5.367428)); // West
                        b.include(new LatLng(36.108838, -5.346123)); // South
                        b.include(new LatLng(36.145086, -5.337617)); // East
                        break;
                    default: // Error
                        return;
                }

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
            mMap.animateCamera(c);
            //mMap.moveCamera(c);
        }
    }
}

class DestinationsCustomView extends LinearLayout {
    private final int textSizeUnit = TypedValue.COMPLEX_UNIT_SP;
    private final int routeNameTextSize = 20;

    private static final int autoEstradaColor = Color.BLUE;
    private static final int itinerarioPrincipalColor = Color.parseColor("#008000"); // Dark green
    private static final int itinerarioComplementarColor = Color.parseColor("#808080"); // Grey
    private static final int viaMaritimaColor = Color.CYAN;
    private static final int viaFerroviariaColor = Color.parseColor("#800000"); // Dark brown
    private static final int viaAltaVelocidadeFerroviariaColor = Color.parseColor("#660066"); // Purple
    private static final int defaultBackgroundColor = Color.parseColor("#F0F0F0"); // Light gray - Ex: itinerários complementares
    private static final int redRouteHighlight = Color.RED; // Ex: itinerários principais

    public DestinationsCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
    }

    public void setView(LocationInfo currentLocation, String currentTransportMeans) {
        int order = 1;
        boolean first = true;
        for (int i = 0; i < currentLocation.getSurroundingLocations().keySet().size(); i++) {
            for (List<String> connectionID : currentLocation.getSurroundingLocations().keySet()) {
                String surroundingLocation = connectionID.get(0);
                String meansTransport = connectionID.get(1);
                if ((order == currentLocation.getSurroundingLocationOrder(surroundingLocation, meansTransport)) && (
                        currentTransportMeans.equals(meansTransport)) && (
                        currentLocation.hasDestinationsFromSurroundingLocation(surroundingLocation, currentTransportMeans))) {
                    addDestinationsInfo(currentLocation, currentTransportMeans, surroundingLocation, first);
                    first = false;
                }
            }
            order += 1;
        }
    }

    private void addDestinationsInfo(LocationInfo currentLocation, String currentTransportMeans, String surroundingLocation, boolean first) {
        String routeName = currentLocation.getRouteName(surroundingLocation, currentTransportMeans);

        // Separator

        if (!first) {
            this.addView(new TextView(getContext()));
        }

        // Surrounding location

        TextView surroundingLocationTextView = new TextView(getContext());
        this.addView(surroundingLocationTextView);
        surroundingLocationTextView.setText(surroundingLocation);

        // Route name

        LinearLayout routeNameLinearLayout = new LinearLayout(getContext()); // Will allow to set different color just under route name and not entire screen width
        this.addView(routeNameLinearLayout);
        routeNameLinearLayout.setGravity(Gravity.CENTER);

        TextView routeTextView = new TextView(getContext());
        routeNameLinearLayout.addView(routeTextView);
        routeTextView.setText(routeName);
        routeTextView.setTextSize(textSizeUnit, routeNameTextSize);
        routeTextView.setTypeface(routeTextView.getTypeface(), Typeface.BOLD);
        routeTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        // Destinations and text color

        LinearLayout destinationsLinearLayout = new LinearLayout(getContext());
        this.addView(destinationsLinearLayout);
        destinationsLinearLayout.setOrientation(VERTICAL);

        for (String destinationText : currentLocation.getDestinationsFromSurroundingLocation(surroundingLocation, currentTransportMeans)) {
            TextView destinationTextView = new TextView(getContext());
            destinationTextView.setText(destinationText);
            destinationsLinearLayout.addView(destinationTextView);
            if (isAutoEstrada(routeName) || isItinerarioPrincipal(routeName) ||
                    isViaMaritima(currentTransportMeans) ||
                    isViaFerroviaria(currentTransportMeans) || isViaAltaVelocidadeFerroviaria(currentTransportMeans)) {
                destinationTextView.setTextColor(Color.WHITE);
                routeTextView.setTextColor(Color.WHITE);
            } else if (currentLocation.getCountry().equals("Spain") && isCarreteraDelEstado(routeName)) {
                routeTextView.setTextColor(Color.WHITE);
                destinationTextView.setTextColor(Color.BLACK);
            } else {
                destinationTextView.setTextColor(Color.BLACK);
                routeTextView.setTextColor(Color.BLACK);
            }
        }

        // Background color
        int backgroundColor = getColorByRouteName(routeName, currentTransportMeans);
        if (backgroundColor == 0)
            backgroundColor = defaultBackgroundColor;
        routeNameLinearLayout.setBackgroundColor(backgroundColor);
        destinationsLinearLayout.setBackgroundColor(backgroundColor);

        // When route name background color does not match general background color
        if (isCarreteraDelEstado(routeName) || isItinerarioPrincipal(routeName))
            routeTextView.setBackgroundColor(redRouteHighlight);

        // Required - https://developer.android.com/training/custom-views/create-view#addprop

        invalidate();
        requestLayout();
    }

    static int getColorByRouteName(String routeName, String currentTransportMeans) {
        if (isAutoEstrada(routeName)) {
            return autoEstradaColor;
        } else if (isItinerarioPrincipal(routeName)) {
            return itinerarioPrincipalColor;
        } else if (isItinerarioComplementar(routeName)) {
            return itinerarioComplementarColor;
        } else if (isViaMaritima(currentTransportMeans)) {
            return viaMaritimaColor;
        } else if (isViaFerroviaria(currentTransportMeans)) {
            return viaFerroviariaColor;
        } else if (isViaAltaVelocidadeFerroviaria(currentTransportMeans)) {
            return viaAltaVelocidadeFerroviariaColor;
        } else { // Ex: Itinerários Complementares, Portuguese Estradas Nacionais
            return 0;
        }
    }

    private static boolean isAutoEstrada(String routeName) {
        if ((routeName == null) || (routeName.length() == 0))
            return false;

        return (
                // General auto-estradas
                routeName.startsWith("A-") || // Autovía (also Andaluzia)
                routeName.startsWith("AP-") || // Autopista
                routeName.startsWith("R-") || // Radial
                (routeName.charAt(0) == 'A' && ((routeName.length() == 2) || (routeName.length() == 3))) || // Ex: A2, A22
                routeName.equals("A9 CREL") ||
                routeName.equals("A13-1") ||
                routeName.equals("A26-1") ||

                // Spanish autonomous community auto-estradas
                routeName.startsWith("CM-") || // Castilla-La Mancha
                routeName.startsWith("EX-A") || // Extremadura
                routeName.startsWith("M-") || // Comunidad de Madrid

                // Spanish provincial auto-estradas
                routeName.startsWith("CA-") || // Cádiz
                routeName.startsWith("CO-") || // Córdoba
                routeName.startsWith("GR-") || // Granada
                routeName.startsWith("H-") || // Huelva
                routeName.startsWith("MA-") || // Málaga
                routeName.startsWith("SE-") || // Seville
                routeName.startsWith("TO-") // Toledo
        );
    }

    private static boolean isItinerarioPrincipal(String routeName) {
        if ((routeName == null) || (routeName.length() == 0))
            return false;
        return (routeName.startsWith("IP"));
    }

    private static boolean isItinerarioComplementar(String routeName) {
        if ((routeName == null) || (routeName.length() == 0))
            return false;
        return (routeName.startsWith("IC"));
    }

    private static boolean isViaMaritima(String meansTransport) {
        return (meansTransport.equals(TravelActivity.BOAT));
    }

    private static boolean isViaFerroviaria(String meansTransport) {
        return meansTransport.equals(TravelActivity.TRAIN);
    }

    private static boolean isViaAltaVelocidadeFerroviaria(String meansTransport) {
        return meansTransport.equals(TravelActivity.HIGH_SPEED_TRAIN);
    }

    private static boolean isCarreteraDelEstado(String routeName) {
        return (routeName.startsWith("N-"));
    }

}