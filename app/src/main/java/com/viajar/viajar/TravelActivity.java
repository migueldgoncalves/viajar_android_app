package com.viajar.viajar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
                    return new DestinationPageFragment();
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
            //runOnUiThread(() -> ((GPSPageFragment) Objects.requireNonNull(getSupportFragmentManager().getFragments().get(0))).createMapMarkers());
            runOnUiThread(this::updateUI);
        }).start();
    }

    public void updateUI() {
        if (currentLocation == null) {
            return;
        }

        TextView locationTextViewGPS = findViewById(R.id.locationTextViewGPS);
        TextView briefInfoTextView = findViewById(R.id.briefInfoTextView);
        TextView locationTextViewDestinations = findViewById(R.id.locationTextViewDestination);
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
        if (locationTextViewDestinations != null)
            locationTextViewDestinations.setText(currentLocationName);

        LinearLayout buttonLayoutGPS = findViewById(R.id.locationButtonLayoutGPS);
        LinearLayout buttonLayoutDestinations = findViewById(R.id.locationButtonLayoutDestinations);
        if (buttonLayoutGPS != null) {
            buttonLayoutGPS.removeAllViewsInLayout();
            buttonLayoutGPS.setOrientation(LinearLayout.VERTICAL);
        }
        if (buttonLayoutDestinations != null) {
            buttonLayoutDestinations.removeAllViewsInLayout();
            buttonLayoutDestinations.setOrientation(LinearLayout.VERTICAL);
        }

        int order = 1;
        for (int i = 0; i < currentLocation.getSurroundingLocations().keySet().size(); i++)
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
                        if (buttonLayoutDestinations != null) {
                            locationButton.setOnClickListener(TravelActivity.this::onClickDestinations);
                            buttonLayoutDestinations.addView(locationButton);
                        }
                    }
                    order += 1;
                }
            }
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

    public void onClickDestinations(View view) {
        if (currentLocation != null) {
            new Thread(() -> runOnUiThread(() -> ((DestinationPageFragment) getSupportFragmentManager().getFragments().get(2)).onClick(view))).start();
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
            return inflater.inflate(R.layout.fragment_travel_info, container, false);
        }

        @Override
        public void onResume() {
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            String currentLocationName = ((TravelActivity) requireActivity()).currentLocationName;

            super.onResume();
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

    public static class DestinationPageFragment extends Fragment {

        ViewGroup rootView;
        View destinationsView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_travel_destination, container, false);
            return rootView;
        }

        @Override
        public void onResume() {
            requireActivity().runOnUiThread(() -> ((TravelActivity) requireActivity()).updateUI());

            String currentLocationName = ((TravelActivity) requireActivity()).currentLocationName;

            super.onResume();
            TextView textView = requireActivity().findViewById(R.id.locationTextViewDestination);
            textView.setText(currentLocationName);
            ((TextView) requireActivity().findViewById(R.id.wayName)).setText("");
            ((LinearLayout) requireActivity().findViewById(R.id.destinations)).removeAllViews();
            ((ConstraintLayout) requireActivity().findViewById(R.id.view_destinations)).setBackgroundColor(Color.WHITE);
        }

        private void onClick(View view) {
            String selectedSurroundingLocation = (String) ((Button) view).getText();
            LocationInfo currentLocation = ((TravelActivity) requireActivity()).currentLocation;
            String currentTransportMeans = ((TravelActivity) requireActivity()).currentTransportMeans;

            String routeName = currentLocation.getRouteName(selectedSurroundingLocation, currentTransportMeans);
            View destinationsLayout = ((ConstraintLayout) requireActivity().findViewById(R.id.view_destinations));
            TextView routeTextView = ((TextView) requireActivity().findViewById(R.id.wayName));
            LinearLayout destinations = requireActivity().findViewById(R.id.destinations);
            ((LinearLayout) requireActivity().findViewById(R.id.destinations)).removeAllViews();
            if (routeName == null) {
                routeTextView.setText(R.string.no_destination);
                routeTextView.setTextColor(Color.BLACK);
                routeTextView.setBackgroundColor(Color.WHITE);
                destinationsLayout.setBackgroundColor(Color.WHITE);
            } else {
                routeTextView.setText(routeName);
                for (String destinationText : currentLocation.getDestinationsFromSurroundingLocation(selectedSurroundingLocation, currentTransportMeans)) {
                    TextView destination = new TextView(getContext());
                    destination.setText(destinationText);
                    destinations.addView(destination);
                    if (isAutoEstrada(routeName) || isItinerarioPrincipal(routeName) || isViaMaritima(currentTransportMeans) || isViaFerroviaria(currentTransportMeans)) {
                        destination.setTextColor(Color.WHITE);
                        routeTextView.setTextColor(Color.WHITE);
                    } else if (currentLocation.getCountry().equals("Spain") && isCarreteraDelEstado(routeName)) {
                        routeTextView.setTextColor(Color.WHITE);
                        destination.setTextColor(Color.BLACK);
                    } else {
                        destination.setTextColor(Color.BLACK);
                        routeTextView.setTextColor(Color.BLACK);
                    }
                }
                if (isAutoEstrada(routeName)) {
                    destinationsLayout.setBackgroundColor(Color.BLUE);
                    routeTextView.setBackgroundColor(Color.BLUE);
                } else if (isItinerarioPrincipal(routeName)) {
                    destinationsLayout.setBackgroundColor(Color.parseColor("#008000")); // Dark green
                    routeTextView.setBackgroundColor(Color.parseColor("#008000"));
                } else if (isViaMaritima(currentTransportMeans)) {
                    destinationsLayout.setBackgroundColor(Color.CYAN);
                    routeTextView.setBackgroundColor(Color.CYAN);
                } else if (isViaFerroviaria(currentTransportMeans)) {
                    destinationsLayout.setBackgroundColor(Color.parseColor("#800000")); // Dark brown
                    routeTextView.setBackgroundColor(Color.parseColor("#800000"));
                } else {
                    routeTextView.setBackgroundColor(Color.WHITE);
                    destinationsLayout.setBackgroundColor(Color.WHITE);
                }

                if (isCarreteraDelEstado(routeName) || isItinerarioPrincipal(routeName))
                    routeTextView.setBackgroundColor(Color.RED);
            }
        }

        private boolean isAutoEstrada(String routeName) {
            return (routeName.startsWith("A-") || // Autovía
                    (routeName.charAt(0) == 'A' && ((routeName.length() == 2) || (routeName.length() == 3))) || // Ex: A2, A22
                    routeName.startsWith("AP-") || // Autopista
                    routeName.startsWith("SE-") || // Seville
                    routeName.startsWith("H-") || // Huelva
                    routeName.startsWith("CA-") || // Cádiz
                    routeName.startsWith("EX-A") || // Extremadura
                    routeName.startsWith("CO-") || // Córdoba
                    routeName.startsWith("CM-") || // Castilla-La Mancha
                    routeName.startsWith("M-") || // Madrid
                    routeName.startsWith("MA-") || // Málaga
                    routeName.startsWith("TO-") || // Toledo
                    routeName.startsWith("R-") || // Radial
                    routeName.equals("A9 CREL"));
        }

        private boolean isItinerarioPrincipal(String routeName) {
            return (routeName.startsWith("IP"));
        }

        private boolean isViaMaritima(String meansTransport) {
            return (meansTransport.equals(BOAT));
        }

        private boolean isViaFerroviaria(String meansTransport) {
            return (meansTransport.equals(TRAIN) || meansTransport.equals(HIGH_SPEED_TRAIN));
        }

        private boolean isCarreteraDelEstado(String routeName) {
            return (routeName.startsWith("N-"));
        }

    }

}