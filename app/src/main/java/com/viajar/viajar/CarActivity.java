package com.viajar.viajar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class CarActivity extends FragmentActivity implements OnMapReadyCallback {

    private Car car;
    private AtomicBoolean accelerating;
    private AtomicBoolean braking;
    private Handler mHandler;

    private GoogleMap mMap;
    private LatLng currentLocation;
    double initialLat = 38.707658; // Lisbon
    double initialLon = -9.136509;
    double cameraOffset = 0.005; // Degrees - In Iberian Peninsula, 1 degree aprox. 100 km
    double advance = 0.000005; // Degrees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        car = new Car(getIntent().getStringExtra("vehicle"), getApplicationContext());
        accelerating = new AtomicBoolean(false);
        braking = new AtomicBoolean(false);
        mHandler = new Handler();

        findViewById(R.id.accelerateButton).setOnTouchListener(onTouchListener);
        findViewById(R.id.brakeButton).setOnTouchListener(onTouchListener);
        findViewById(R.id.gearUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gearDownButton).setOnClickListener(onClickListener);

        if (car.getVehicleType() != Car.CAR)
            ((ProgressBar) findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_5TH_GEAR[car.getVehicleType()]);
        else
            ((ProgressBar) findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_6TH_GEAR[car.getVehicleType()]);
        ((ProgressBar) findViewById(R.id.rpmMeter)).setMax(Car.MAX_ROTATIONS[car.getVehicleType()]);

        SeekBar direction = findViewById(R.id.direction);
        int fullCircle = 360;
        direction.setMax(fullCircle * 2);
        direction.setProgress(fullCircle);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCar);
        mapFragment.getMapAsync(this);

        carController.run();
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

        currentLocation = new LatLng(initialLat, initialLon);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(new LatLng(initialLat + cameraOffset, initialLon - cameraOffset));
        b.include(new LatLng(initialLat - cameraOffset, initialLon + cameraOffset));
        LatLngBounds bounds = b.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
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
        ((TextView) findViewById(R.id.speedTextView)).setText(String.valueOf(car.getSpeed()));
        ((TextView) findViewById(R.id.rpmTextView)).setText(String.valueOf(car.getRpm()));
        ((TextView) findViewById(R.id.distanceTextView)).setText(String.valueOf(car.getTravelledDistance()));

        if (car.isGearTooHigh())
            ((TextView) findViewById(R.id.gearTextView)).setTextColor(Color.RED);
        else
            ((TextView) findViewById(R.id.gearTextView)).setTextColor(Color.BLACK);

        // Map-related code
        if (car.getSpeed() != 0) {
            mMap.clear();
            int quarterCircle = 90;
            int angle = ((SeekBar) findViewById(R.id.direction)).getProgress() - quarterCircle; // 0 - 360 -> -180 - 180

            int halfCircle = 180; // Ensures starting angle points to North
            double newLatitude = currentLocation.latitude + (Math.sin(Math.toRadians(angle + halfCircle)) * advance) * car.getSpeed();
            int maxLatitude = 78;
            if (newLatitude > maxLatitude) // Marker seems to not appear for low zoom levels in very high or very low latitudes
                newLatitude = -maxLatitude;
            else if (newLatitude < -maxLatitude)
                newLatitude = maxLatitude;

            double newLongitude = currentLocation.longitude + (Math.cos(Math.toRadians(angle + halfCircle)) * advance) * car.getSpeed();
            int maxLongitude = 180;
            if (newLongitude > maxLongitude)
                newLongitude = -maxLongitude + 0.01;
            else if (newLongitude < -maxLongitude)
                newLongitude = maxLongitude - 0.01;

            currentLocation = new LatLng(newLatitude, newLongitude);

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

            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }

        String text = "";
        if (car.getGear() == -1) {
            text = (String) getText(R.string.reverse);
        } else if (car.getGear() == 0) {
            text = (String) getText(R.string.neutral);
        } else if (car.isTopGear()) {
            text = (String) getText(R.string.infinite);
        } else {
            text = String.valueOf(car.getGear());
        }
        ((TextView) findViewById(R.id.gearTextView)).setText(text);

        ((ProgressBar) findViewById(R.id.speedometer)).setProgress(car.getSpeed());
        ProgressBar rpmMeter = findViewById(R.id.rpmMeter);
        rpmMeter.setProgress(car.getRpm());
        if (car.getRpm() < (Car.REDLINE[car.getVehicleType()] - Car.SUGGESTION_CHANGE_GEAR[car.getVehicleType()])) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        } else if (car.getRpm() < Car.REDLINE[car.getVehicleType()]) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN));
        } else {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        }
    }

    Runnable carController = new Runnable() {
        @Override
        public void run() {
            if (((!accelerating.get()) && (!braking.get())) || (car.getRpm() >= Car.MAX_ROTATIONS[car.getVehicleType()])) {
                car.decelerateByFriction();
            } if ((accelerating.get()) && ((car.getRpm() < Car.MAX_ROTATIONS[car.getVehicleType()]) || car.isTopGear())) { // Both buttons can be pressed at the same time
                car.onClickAccelerateButton();
            } if (braking.get()) {
                car.onClickBrakeButton();
            }
            car.increaseTravelledDistance();
            runOnUiThread(CarActivity.this::updateUI);
            mHandler.postDelayed(carController, (long) (Car.TIME_BETWEEN_INPUTS * 1000));
        }
    };
}