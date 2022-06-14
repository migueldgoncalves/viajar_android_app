package com.viajar.viajar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class CarFragment extends Fragment implements OnMapReadyCallback {
    private Car car;
    private AtomicBoolean accelerating;
    private AtomicBoolean braking;
    private Handler mHandler;

    private GoogleMap mMap;
    private MapView mMapView;

    private static final LatLng defaultStartCoordinates = new LatLng(38.707658, -9.136509); // Lisbon

    private LatLng currentLocation;
    double[] journeyLatList; // Ordered latitudes of the locations of the current journey
    double[] journeyLonList;
    LatLng[] currentPairLatLon = new LatLng[2]; // Will contain start and end of current line segment
    int currentSegment = 0; // Current line segment of the journey

    private static final double cameraOffset = 0.005; // Degrees - In Iberian Peninsula, 1 degree aprox. 100 km
    private static final double advance = 0.000002; // Degrees

    Marker mMarker = null;
    Polyline mPolyline = null;
    private static final int lineWidth = 5;
    String markerTitle;
    int color;

    volatile boolean stopThread;
    Thread carController;

    private static final int directionAngleRange = 360 * 2;

    private static final int STANDALONE_MODE = 0;
    private static final int INTEGRATION_MODE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_car, container, false);

        markerTitle = getString(R.string.car_marker_text);

        accelerating = new AtomicBoolean(false);
        braking = new AtomicBoolean(false);
        mHandler = new Handler();

        mMapView = rootView.findViewById(R.id.mapCar);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        requireActivity().findViewById(R.id.accelerateButton).setOnTouchListener(onTouchListener);
        requireActivity().findViewById(R.id.brakeButton).setOnTouchListener(onTouchListener);
        requireActivity().findViewById(R.id.gearUpButton).setOnClickListener(onClickListener);
        requireActivity().findViewById(R.id.gearDownButton).setOnClickListener(onClickListener);

        SeekBar direction = requireActivity().findViewById(R.id.direction);
        direction.setMax(directionAngleRange);
        direction.setProgress(directionAngleRange / 2); // Will start at center
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
        stopThread = true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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

        // Map can be set immediately to default starting location
        if (getMode() == STANDALONE_MODE) {
            currentLocation = defaultStartCoordinates;
            mMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(markerTitle));
            assert mMarker != null;

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            b.include(new LatLng(mMarker.getPosition().latitude + cameraOffset, mMarker.getPosition().longitude - cameraOffset)); // Northwest corner
            b.include(new LatLng(mMarker.getPosition().latitude - cameraOffset, mMarker.getPosition().longitude + cameraOffset)); // Southeast corner
            LatLngBounds bounds = b.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        }
    }

    public void setCar(String carType) {
        if (car == null)
            car = new Car(carType, requireContext());

        if (car.getVehicleType() == Car.CAR) // Car has 6 forward gears, bus and van have 5
            ((ProgressBar) requireActivity().findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_6TH_GEAR[car.getVehicleType()]);
        else
            ((ProgressBar) requireActivity().findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_5TH_GEAR[car.getVehicleType()]);
        ((ProgressBar) requireActivity().findViewById(R.id.rpmMeter)).setMax(Car.MAX_ROTATIONS[car.getVehicleType()]);
    }

    public void startJourney(Bundle args) {
        if (getMode() == INTEGRATION_MODE) { // If in integration mode, a journey to follow and the line color should be provided
            if (args == null)
                return;
            journeyLatList = args.getDoubleArray("latList");
            journeyLonList = args.getDoubleArray("lonList");
            color = args.getInt("color");
        }

        stopThread = false;
        carController = new Thread() {
            @Override
            public void run() {
                if (stopThread) {
                    return;
                }
                if (((!accelerating.get()) && (!braking.get())) || (car.getRpm() >= Car.MAX_ROTATIONS[car.getVehicleType()])) {
                    car.decelerateByFriction();
                } if ((accelerating.get()) && ((car.getRpm() < Car.MAX_ROTATIONS[car.getVehicleType()]) || car.isTopGear())) { // Both buttons can be pressed at the same time
                    car.onClickAccelerateButton();
                } if (braking.get()) {
                    car.onClickBrakeButton();
                }
                car.increaseTravelledDistance();
                if (!stopThread)
                    requireActivity().runOnUiThread(CarFragment.this::updateUI);
                mHandler.postDelayed(carController, (long) (Car.TIME_BETWEEN_INPUTS * 1000));
            }
        };
        carController.start();

        // Initializes map with marker and, if in integration mode, with a polyline
        requireActivity().runOnUiThread(() -> {
            LatLng[] latLngs;
            if (getMode() == INTEGRATION_MODE) {
                latLngs = new LatLng[journeyLatList.length];
                for (int i = 0; i < journeyLatList.length; i++)
                    latLngs[i] = new LatLng(journeyLatList[i], journeyLonList[i]);
                currentPairLatLon[0] = latLngs[0];
                currentPairLatLon[1] = latLngs[1];
                currentSegment = 0;
                currentLocation = latLngs[0];
                mPolyline = mMap.addPolyline(new PolylineOptions().add(latLngs).width(lineWidth).color(color));
                mMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(markerTitle));
            } else {
                latLngs = new LatLng[1];
                latLngs[0] = defaultStartCoordinates;
                currentLocation = latLngs[0];
            }

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            LatLng initialLocation = latLngs[0];
            b.include(new LatLng(initialLocation.latitude + cameraOffset, initialLocation.longitude - cameraOffset));
            b.include(new LatLng(initialLocation.latitude - cameraOffset, initialLocation.longitude + cameraOffset));
            LatLngBounds bounds = b.build();
            if (mMap != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        });
    }

    public void finishJourney() {
        View carFragment = requireActivity().findViewById(R.id.travelCarContainerView);
        requireActivity().runOnUiThread(() -> carFragment.setVisibility(View.GONE));
        View viewPager2 = requireActivity().findViewById(R.id.travelPager);
        requireActivity().runOnUiThread(() -> viewPager2.setVisibility(View.VISIBLE));
        View bottomBar = requireActivity().findViewById(R.id.bottomNavigationViewGPS);
        requireActivity().runOnUiThread(() -> bottomBar.setVisibility(View.VISIBLE));
        stopThread = true;
    }

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            if (view.getId() == R.id.accelerateButton) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    accelerating.set(true);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    accelerating.set(false);
                }
            }
            else if (view.getId() == R.id.brakeButton) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    braking.set(true);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    braking.set(false);
                }
            }
            return true;
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.gearUpButton) {
                car.onClickGearUp();
            }
            else if (view.getId() == R.id.gearDownButton) {
                car.onClickGearDown();
            }
        }
    };

    private void updateUI() {
        ((TextView) requireActivity().findViewById(R.id.speedTextView)).setText(String.valueOf(car.getSpeed()));
        ((TextView) requireActivity().findViewById(R.id.rpmTextView)).setText(String.valueOf(car.getRpm()));
        ((TextView) requireActivity().findViewById(R.id.distanceTextView)).setText(String.valueOf(car.getTravelledDistance()));

        if (car.isGearTooHigh())
            ((TextView) requireActivity().findViewById(R.id.gearTextView)).setTextColor(Color.RED);
        else
            ((TextView) requireActivity().findViewById(R.id.gearTextView)).setTextColor(Color.BLACK);

        // Map-related code
        if (car.getSpeed() != 0) {
            double angle = ((SeekBar) requireActivity().findViewById(R.id.direction)).getProgress();
            if (getMode() == INTEGRATION_MODE) {
                double a = (currentPairLatLon[1].longitude - currentPairLatLon[0].longitude);
                double b = (currentPairLatLon[1].latitude - currentPairLatLon[0].latitude);
                double c = a / b;
                double d = Math.atan(c);
                angle = Math.toDegrees(d);
                if ((a > 0) && (angle < 0))
                    angle += 180;
                else if ((a < 0) && (angle > 0))
                    angle -= 180;
            }

            double newLatitude = currentLocation.latitude + (Math.cos(Math.toRadians(angle)) * advance) * car.getSpeed();
            int maxLatitude = 78;
            if (newLatitude > maxLatitude) // Marker seems to not appear for low zoom levels in very high or very low latitudes
                newLatitude = -maxLatitude;
            else if (newLatitude < -maxLatitude)
                newLatitude = maxLatitude;

            double newLongitude = currentLocation.longitude + (Math.sin(Math.toRadians(angle)) * advance) * car.getSpeed();
            int maxLongitude = 180;
            if (newLongitude > maxLongitude)
                newLongitude = -maxLongitude + 0.01;
            else if (newLongitude < -maxLongitude)
                newLongitude = maxLongitude - 0.01;

            currentLocation = new LatLng(newLatitude, newLongitude);
            if (getMode() == INTEGRATION_MODE) { // If current line segment has ended, switch to next or end journey
                if ((newLatitude > currentPairLatLon[1].latitude) && (currentPairLatLon[1].latitude >= currentPairLatLon[0].latitude))
                    if (currentSegment < journeyLatList.length - 2) {
                        currentPairLatLon[0] = currentPairLatLon[1];
                        currentPairLatLon[1] = new LatLng(journeyLatList[currentSegment + 2], journeyLonList[currentSegment + 2]);
                        currentSegment += 1;
                    } else
                        finishJourney();
                else if ((newLatitude < currentPairLatLon[1].latitude) && (currentPairLatLon[1].latitude <= currentPairLatLon[0].latitude))
                    if (currentSegment < journeyLatList.length - 2) {
                        currentPairLatLon[0] = currentPairLatLon[1];
                        currentPairLatLon[1] = new LatLng(journeyLatList[currentSegment + 2], journeyLonList[currentSegment + 2]);
                        currentSegment += 1;
                    } else
                        finishJourney();
            }

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            if (car.isTopGear()) { // Gear with infinite max speed - Countries and regions will be focused
                int multiplier = 1000;
                b.include(new LatLng(currentLocation.latitude + multiplier * cameraOffset, currentLocation.longitude - multiplier * cameraOffset));
                b.include(new LatLng(currentLocation.latitude - multiplier * cameraOffset, currentLocation.longitude + multiplier * cameraOffset));
            } else { // Standard gear - Roads will be focused
                b.include(new LatLng(currentLocation.latitude + cameraOffset, currentLocation.longitude - cameraOffset));
                b.include(new LatLng(currentLocation.latitude - cameraOffset, currentLocation.longitude + cameraOffset));
            }
            LatLngBounds bounds = b.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));

            if (mMarker != null)
                mMarker.setPosition(currentLocation);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }

        String text;
        if (car.getGear() == -1) {
            text = (String) getText(R.string.reverse);
        } else if (car.getGear() == 0) {
            text = (String) getText(R.string.neutral);
        } else if (car.isTopGear()) {
            text = (String) getText(R.string.infinite);
        } else {
            text = String.valueOf(car.getGear());
        }
        ((TextView) requireActivity().findViewById(R.id.gearTextView)).setText(text);

        ((ProgressBar) requireActivity().findViewById(R.id.speedometer)).setProgress(car.getSpeed());
        ProgressBar rpmMeter = requireActivity().findViewById(R.id.rpmMeter);
        rpmMeter.setProgress(car.getRpm());
        if (car.getRpm() < (Car.REDLINE[car.getVehicleType()] - Car.SUGGESTION_CHANGE_GEAR[car.getVehicleType()])) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        } else if (car.getRpm() < Car.REDLINE[car.getVehicleType()]) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN));
        } else {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        }
    }

    public int getMode() {
        if (getActivity() == null) // Not expected
            return STANDALONE_MODE; // Default

        if (getActivity().getClass() == TravelActivity.class)
            return INTEGRATION_MODE;
        else // Activity is CarActivity
            return STANDALONE_MODE;
    }
}
