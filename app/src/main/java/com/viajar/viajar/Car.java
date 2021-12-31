package com.viajar.viajar;

import android.content.Context;

public class Car {

    public static final int INPUTS_PER_SECOND = 20; // Input will be read this number of times per second
    public static final double TIME_BETWEEN_INPUTS = 1 / (double) INPUTS_PER_SECOND;

    public static final int BUS = 0;
    public static final int VAN = 1;
    public static final int CAR = 2;

    // {Bus, Van, Car}

    public static final int[] REDLINE = new int[]{2500, 4500, 7000};
    public static final int[] MAX_ROTATIONS = new int[]{3000, 6000, 9000};
    public static final int[] SUGGESTION_CHANGE_GEAR = new int[]{500, 500, 1000}; // How much before redline app will warn user to gear up
    public static final int[] MAX_SPEED = new int[]{125, 160, 250}; // km/h
    public static final int[] DECELERATION_PER_SECOND = new int[]{10, 10, 10}; // km/h lost per second when not accelerating nor braking
    public static final int[] BRAKE_PER_SECOND = new int[]{40, 40, 40}; // km/h braked per second
    public static final int[] TOTAL_GEARS = new int[]{5, 5, 6}; // Forward gears
    public static final int[] TOP_GEAR = new int[]{11, 11, 11}; // Supported forward gears - Last will have infinite max speed

    // Time for going from 0 RPM to redline in each gear in max acceleration, in seconds
    public static final int[] ACCELERATION_1ST_GEAR = new int[]{4, 4, 2};
    public static final int[] ACCELERATION_2ND_GEAR = new int[]{8, 8, 6};
    public static final int[] ACCELERATION_3RD_GEAR = new int[]{16, 14, 12};
    public static final int[] ACCELERATION_4TH_GEAR = new int[]{30, 28, 25};
    public static final int[] ACCELERATION_5TH_GEAR = new int[]{50, 45, 40};
    public static final int[] ACCELERATION_6TH_GEAR = new int[]{70, 60, 50};
    public static final int[] ACCELERATION_7TH_GEAR = new int[]{90, 80, 70};
    public static final int[] ACCELERATION_8TH_GEAR = new int[]{120, 105, 90};
    public static final int[] ACCELERATION_9TH_GEAR = new int[]{150, 125, 120};
    public static final int[] ACCELERATION_10TH_GEAR = new int[]{180, 160, 150};
    public static final int[] ACCELERATION_11TH_GEAR = new int[]{1, 1, 1}; // Special gear
    public static final int[] ACCELERATION_REVERSE_GEAR = new int[]{4, 4, 2};

    // Speed at readline in km/h for each gear
    public static final int[] SPEED_REDLINE_1ST_GEAR = new int[]{25, 35, MAX_SPEED[CAR] / 5};
    public static final int[] SPEED_REDLINE_2ND_GEAR = new int[]{40, 60, 2 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_3RD_GEAR = new int[]{55, 90, 3 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_4TH_GEAR = new int[]{80, 120, 4 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_5TH_GEAR = new int[]{5 * (MAX_SPEED[BUS] / 5), 5 * (MAX_SPEED[VAN] / 5), 5 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_6TH_GEAR = new int[]{6 * (MAX_SPEED[BUS] / 5), 6 * (MAX_SPEED[VAN] / 5), 6 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_7TH_GEAR = new int[]{7 * (MAX_SPEED[BUS] / 5), 7 * (MAX_SPEED[VAN] / 5), 7 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_8TH_GEAR = new int[]{8 * (MAX_SPEED[BUS] / 5), 8 * (MAX_SPEED[VAN] / 5), 8 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_9TH_GEAR = new int[]{9 * (MAX_SPEED[BUS] / 5), 9 * (MAX_SPEED[VAN] / 5), 9 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_10TH_GEAR = new int[]{10 * (MAX_SPEED[BUS] / 5), 10 * (MAX_SPEED[VAN] / 5), 10 * (MAX_SPEED[CAR] / 5)};
    public static final int[] SPEED_REDLINE_11TH_GEAR = new int[]{100 * (MAX_SPEED[BUS] / 5), 100 * (MAX_SPEED[VAN] / 5), 100 * (MAX_SPEED[CAR] / 5)}; // Special gear
    public static final int[] SPEED_REDLINE_REVERSE_GEAR = new int[]{25, 35, MAX_SPEED[CAR] / 5};

    // Deceleration types
    public static final int DECELERATION_BRAKING = 1;
    public static final int DECELERATION_FRICTION = 2;

    private double speed = 0.0; // km/h
    private int rpm = 0;
    private int gear = 0;
    private double travelledDistance = 0.0; // km
    private int vehicleType = -1;
    
    public Car(String vehicle, Context context) {
        if (vehicle.equals(context.getString(R.string.vehicle_bus)))
            vehicleType = BUS;
        else if (vehicle.equals(context.getString(R.string.vehicle_van)))
            vehicleType = VAN;
        else if (vehicle.equals(context.getString(R.string.vehicle_car)))
            vehicleType = CAR;
    }

    public void onClickAccelerateButton() {
        accelerate();
    }

    public void onClickBrakeButton() {
        decelerate(DECELERATION_BRAKING);
    }

    public void onClickGearUp() {
        gearUp();
    }

    public void onClickGearDown() {
        gearDown();
    }

    public void decelerateByFriction() {
        decelerate(DECELERATION_FRICTION);
    }

    private void accelerate() {
        if ((rpm < MAX_ROTATIONS[vehicleType]) || isTopGear()) { // Top gear has no speed limit
            if (gear == -1) {
                speed -= ((double) SPEED_REDLINE_REVERSE_GEAR[vehicleType] / ACCELERATION_REVERSE_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 1) {
                speed += ((double) SPEED_REDLINE_1ST_GEAR[vehicleType] / ACCELERATION_1ST_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 2) {
                speed += ((double) SPEED_REDLINE_2ND_GEAR[vehicleType] / ACCELERATION_2ND_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 3) {
                speed += ((double) SPEED_REDLINE_3RD_GEAR[vehicleType] / ACCELERATION_3RD_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 4) {
                speed += ((double) SPEED_REDLINE_4TH_GEAR[vehicleType] / ACCELERATION_4TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 5) {
                speed += ((double) SPEED_REDLINE_5TH_GEAR[vehicleType] / ACCELERATION_5TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 6) {
                speed += ((double) SPEED_REDLINE_6TH_GEAR[vehicleType] / ACCELERATION_6TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 7) {
                speed += ((double) SPEED_REDLINE_7TH_GEAR[vehicleType] / ACCELERATION_7TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 8) {
                speed += ((double) SPEED_REDLINE_8TH_GEAR[vehicleType] / ACCELERATION_8TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 9) {
                speed += ((double) SPEED_REDLINE_9TH_GEAR[vehicleType] / ACCELERATION_9TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 10) {
                speed += ((double) SPEED_REDLINE_10TH_GEAR[vehicleType] / ACCELERATION_10TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
            else if (gear == 11) { // Set top gear acceleration here - Did not work when changing other gear values with very high speeds
                speed += 10 * ((double) SPEED_REDLINE_11TH_GEAR[vehicleType] / ACCELERATION_11TH_GEAR[vehicleType] / INPUTS_PER_SECOND);
            }
        }
        upgrade_rpm();
    }

    private void decelerate(int deceleration_type) {
        double speed_loss = 0;
        if (deceleration_type == DECELERATION_BRAKING) {
            if (isTopGear()) // Set top gear deceleration rate here
                speed_loss = 10000 * (double) BRAKE_PER_SECOND[vehicleType] / INPUTS_PER_SECOND;
            else
                speed_loss = (double) BRAKE_PER_SECOND[vehicleType] / INPUTS_PER_SECOND;
        }
        else if (deceleration_type == DECELERATION_FRICTION) {
            speed_loss = (double) DECELERATION_PER_SECOND[vehicleType] / INPUTS_PER_SECOND;
        }

        if (Math.abs(speed) > speed_loss) {
            if (speed < 0) {
                speed += speed_loss;
            }
            else {
                speed -= speed_loss;
            }
        } else {
            speed = 0;
        }
        upgrade_rpm();
    }

    private void upgrade_rpm() {
        if (gear == -1) {
            rpm = (int) Math.abs(speed * REDLINE[vehicleType] / SPEED_REDLINE_REVERSE_GEAR[vehicleType]);
        }
        else if (gear == 0) {
            rpm = 0;
        }
        else if (gear == 1) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_1ST_GEAR[vehicleType]);
        }
        else if (gear == 2) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_2ND_GEAR[vehicleType]);
        }
        else if (gear == 3) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_3RD_GEAR[vehicleType]);
        }
        else if (gear == 4) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_4TH_GEAR[vehicleType]);
        }
        else if (gear == 5) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_5TH_GEAR[vehicleType]);
        }
        else if (gear == 6) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_6TH_GEAR[vehicleType]);
        }
        else if (gear == 7) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_7TH_GEAR[vehicleType]);
        }
        else if (gear == 8) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_8TH_GEAR[vehicleType]);
        }
        else if (gear == 9) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_9TH_GEAR[vehicleType]);
        }
        else if (gear == 10) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_10TH_GEAR[vehicleType]);
        }
        else if (gear == 11) {
            rpm = (int) (speed * REDLINE[vehicleType] / SPEED_REDLINE_11TH_GEAR[vehicleType]);
        }
    }

    private void upgrade_distance() {
        travelledDistance += (1 / (double) INPUTS_PER_SECOND) * speed / 3600;
    }

    private void reset_car() {
        speed = 0;
        rpm = 0;
        gear = 1;
        travelledDistance = 0;
    }

    public void gearUp() {
        if (gear < TOP_GEAR[vehicleType]) {
            this.gear += 1;
            upgrade_rpm();
        }
    }

    public void gearDown() {
        if (gear > -1) {
            this.gear -= 1;
            upgrade_rpm();
        }
    }

    public void increaseTravelledDistance() {
        this.travelledDistance += (this.speed / 3600 * TIME_BETWEEN_INPUTS);
    }

    public int getSpeed() {
        return (int) speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public double getTravelledDistance() {
        return (double) Math.round(travelledDistance * 1000) / 1000;  // Returns double with 3 decimal places
    }

    public void setTravelledDistance(int travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isGearTooHigh() { // Example: 8th gear
        return (gear > TOTAL_GEARS[vehicleType]);
    }

    public boolean isTopGear() {
        return (gear == TOP_GEAR[vehicleType]);
    }
}
