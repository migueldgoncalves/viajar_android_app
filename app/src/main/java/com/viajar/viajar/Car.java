package com.viajar.viajar;

public class Car {

    public static final int INPUTS_PER_SECOND = 20; // Input will be read this number of times per second
    public static final double TIME_BETWEEN_INPUTS = 1 / (double) INPUTS_PER_SECOND;

    public static final int REDLINE = 7000;
    public static final int MAX_ROTATIONS = 9000;
    public static final int SUGGESTION_CHANGE_GEAR = 1000; // How much before redline app will warn user to gear up
    public static final int MAX_SPEED = 250; // km/h
    public static final int DECELERATION_PER_SECOND = 10; // km/h lost per second when not accelerating nor braking
    public static final int BRAKE_PER_SECOND = 40; // km/h braked per second
    public static final int GEAR_NUMBER = 6; // Forward gears

    // Time for going from 0 RPM to redline in each gear in max acceleration, in seconds
    public static final int ACCELERATION_1ST_GEAR = 2;
    public static final int ACCELERATION_2ND_GEAR = 6;
    public static final int ACCELERATION_3RD_GEAR = 12;
    public static final int ACCELERATION_4TH_GEAR = 25;
    public static final int ACCELERATION_5TH_GEAR = 40;
    public static final int ACCELERATION_6TH_GEAR = 50;
    public static final int ACCELERATION_7TH_GEAR = 70;
    public static final int ACCELERATION_8TH_GEAR = 90;
    public static final int ACCELERATION_REVERSE_GEAR = 2;

    // Speed at readline in km/h for each gear
    public static final int SPEED_REDLINE_1ST_GEAR = MAX_SPEED / 5;
    public static final int SPEED_REDLINE_2ND_GEAR = 2 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_3RD_GEAR = 3 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_4TH_GEAR = 4 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_5TH_GEAR = 5 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_6TH_GEAR = 6 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_7TH_GEAR = 7 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_8TH_GEAR = 8 * (MAX_SPEED / 5);
    public static final int SPEED_REDLINE_REVERSE_GEAR = MAX_SPEED / 5;

    // Deceleration types
    public static final int DECELERATION_BRAKING = 1;
    public static final int DECELERATION_FRICTION = 2;

    private double speed = 0.0; // km/h
    private int rpm = 0;
    private int gear = 0;
    private double travelledDistance = 0.0; // km

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
        if (rpm < MAX_ROTATIONS) {
            if (gear == -1) {
                speed -= ((double) SPEED_REDLINE_REVERSE_GEAR / ACCELERATION_REVERSE_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 1) {
                speed += ((double) SPEED_REDLINE_1ST_GEAR / ACCELERATION_1ST_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 2) {
                speed += ((double) SPEED_REDLINE_2ND_GEAR / ACCELERATION_2ND_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 3) {
                speed += ((double) SPEED_REDLINE_3RD_GEAR / ACCELERATION_3RD_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 4) {
                speed += ((double) SPEED_REDLINE_4TH_GEAR / ACCELERATION_4TH_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 5) {
                speed += ((double) SPEED_REDLINE_5TH_GEAR / ACCELERATION_5TH_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 6) {
                speed += ((double) SPEED_REDLINE_6TH_GEAR / ACCELERATION_6TH_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 7) {
                speed += ((double) SPEED_REDLINE_7TH_GEAR / ACCELERATION_7TH_GEAR / INPUTS_PER_SECOND);
            }
            else if (gear == 8) {
                speed += ((double) SPEED_REDLINE_8TH_GEAR / ACCELERATION_8TH_GEAR / INPUTS_PER_SECOND);
            }
        }
        upgrade_rpm();
    }

    private void decelerate(int deceleration_type) {
        double speed_loss = 0;
        if (deceleration_type == DECELERATION_BRAKING) {
            speed_loss = (double) BRAKE_PER_SECOND / INPUTS_PER_SECOND;
        }
        else if (deceleration_type == DECELERATION_FRICTION) {
            speed_loss = (double) DECELERATION_PER_SECOND / INPUTS_PER_SECOND;
        }

        if (Math.abs(speed) > speed_loss) {
            if (speed < 0) {
                speed += speed_loss;
            }
            else {
                speed -= speed_loss;
            }
        }
        upgrade_rpm();
    }

    private void upgrade_rpm() {
        if (gear == -1) {
            rpm = (int) Math.abs(speed * REDLINE / SPEED_REDLINE_REVERSE_GEAR);
        }
        else if (gear == 0) {
            rpm = 0;
        }
        else if (gear == 1) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_1ST_GEAR);
        }
        else if (gear == 2) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_2ND_GEAR);
        }
        else if (gear == 3) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_3RD_GEAR);
        }
        else if (gear == 4) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_4TH_GEAR);
        }
        else if (gear == 5) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_5TH_GEAR);
        }
        else if (gear == 6) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_6TH_GEAR);
        }
        else if (gear == 7) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_7TH_GEAR);
        }
        else if (gear == 8) {
            rpm = (int) (speed * REDLINE / SPEED_REDLINE_8TH_GEAR);
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
        if (gear < GEAR_NUMBER) {
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
}
