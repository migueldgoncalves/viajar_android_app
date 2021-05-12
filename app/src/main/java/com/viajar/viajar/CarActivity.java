package com.viajar.viajar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class CarActivity extends AppCompatActivity {

    private Car car;
    private AtomicBoolean accelerating;
    private AtomicBoolean braking;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        car = new Car(getIntent().getStringExtra("vehicle"));
        accelerating = new AtomicBoolean(false);
        braking = new AtomicBoolean(false);
        mHandler = new Handler();

        findViewById(R.id.accelerateButton).setOnTouchListener(onTouchListener);
        findViewById(R.id.brakeButton).setOnTouchListener(onTouchListener);
        findViewById(R.id.gearUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gearDownButton).setOnClickListener(onClickListener);

        if (car.getVehiclePosition() != 2)
            ((ProgressBar) findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_5TH_GEAR[car.getVehiclePosition()]);
        else
            ((ProgressBar) findViewById(R.id.speedometer)).setMax(Car.SPEED_REDLINE_6TH_GEAR[car.getVehiclePosition()]);
        ((ProgressBar) findViewById(R.id.rpmMeter)).setMax(Car.MAX_ROTATIONS[car.getVehiclePosition()]);

        carController.run();
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

        String text = "";
        if (car.getGear() == -1) {
            text = (String) getText(R.string.reverse);
        } else if (car.getGear() == 0) {
            text = (String) getText(R.string.neutral);
        } else {
            text = String.valueOf(car.getGear());
        }
        ((TextView) findViewById(R.id.gearTextView)).setText(text);

        ((ProgressBar) findViewById(R.id.speedometer)).setProgress(car.getSpeed());
        ProgressBar rpmMeter = findViewById(R.id.rpmMeter);
        rpmMeter.setProgress(car.getRpm());
        if (car.getRpm() < (Car.REDLINE[car.getVehiclePosition()] - Car.SUGGESTION_CHANGE_GEAR[car.getVehiclePosition()])) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        } else if (car.getRpm() < Car.REDLINE[car.getVehiclePosition()]) {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN));
        } else {
            rpmMeter.getProgressDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        }
    }

    Runnable carController = new Runnable() {
        @Override
        public void run() {
            if (((!accelerating.get()) && (!braking.get())) || (car.getRpm() >= Car.MAX_ROTATIONS[car.getVehiclePosition()])) {
                car.decelerateByFriction();
            } if ((accelerating.get()) && (car.getRpm() < Car.MAX_ROTATIONS[car.getVehiclePosition()])) { // Both buttons can be pressed at the same time
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