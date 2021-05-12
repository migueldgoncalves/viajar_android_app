package com.viajar.viajar;

public class Car {

    public static final int INPUTS_PER_SECOND = 20; // Input will be read this number of times per second
    public static final double TIME_BETWEEN_INPUTS = 1 / (double) INPUTS_PER_SECOND;

    // {Bus, Van, Car}

    public static final int[] REDLINE = new int[]{2500, 4500, 7000};
    public static final int[] MAX_ROTATIONS = new int[]{3000, 6000, 9000};
    public static final int[] SUGGESTION_CHANGE_GEAR = new int[]{500, 500, 1000}; // How much before redline app will warn user to gear up
    public static final int[] MAX_SPEED = new int[]{125, 160, 250}; // km/h
    public static final int[] DECELERATION_PER_SECOND = new int[]{10, 10, 10}; // km/h lost per second when not accelerating nor braking
    public static final int[] BRAKE_PER_SECOND = new int[]{40, 40, 40}; // km/h braked per second
    public static final int[] GEAR_NUMBER = new int[]{5, 5, 6}; // Forward gears

    // Time for going from 0 RPM to redline in each gear in max acceleration, in seconds
    public static final int[] ACCELERATION_1ST_GEAR = new int[]{4, 4, 2};
    public static final int[] ACCELERATION_2ND_GEAR = new int[]{8, 8, 6};
    public static final int[] ACCELERATION_3RD_GEAR = new int[]{16, 14, 12};
    public static final int[] ACCELERATION_4TH_GEAR = new int[]{30, 28, 25};
    public static final int[] ACCELERATION_5TH_GEAR = new int[]{50, 45, 40};
    public static final int[] ACCELERATION_6TH_GEAR = new int[]{70, 60, 50};
    public static final int[] ACCELERATION_7TH_GEAR = new int[]{90, 80, 70};
    public static final int[] ACCELERATION_8TH_GEAR = new int[]{120, 105, 90};
    public static final int[] ACCELERATION_REVERSE_GEAR = new int[]{4, 4, 2};

    // Speed at readline in km/h for each gear
    public static final int[] SPEED_REDLINE_1ST_GEAR = new int[]{25, 35, MAX_SPEED[2] / 5};
    public static final int[] SPEED_REDLINE_2ND_GEAR = new int[]{40, 60, 2 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_3RD_GEAR = new int[]{55, 90, 3 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_4TH_GEAR = new int[]{80, 120, 4 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_5TH_GEAR = new int[]{120, 160, 5 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_6TH_GEAR = new int[]{6 * (MAX_SPEED[2] / 5), 6 * (MAX_SPEED[2] / 5), 6 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_7TH_GEAR = new int[]{7 * (MAX_SPEED[2] / 5),7 * (MAX_SPEED[2] / 5), 7 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_8TH_GEAR = new int[]{8 * (MAX_SPEED[2] / 5),8 * (MAX_SPEED[2] / 5), 8 * (MAX_SPEED[2] / 5)};
    public static final int[] SPEED_REDLINE_REVERSE_GEAR = new int[]{25, 35, MAX_SPEED[2] / 5};

    // Deceleration types
    public static final int DECELERATION_BRAKING = 1;
    public static final int DECELERATION_FRICTION = 2;

    private double speed = 0.0; // km/h
    private int rpm = 0;
    private int gear = 0;
    private double travelledDistance = 0.0; // km
    private int vehiclePosition = -1;
    
    public Car(String vehicle) {
        switch (vehicle) {
            case ("Bus"):
            case ("Autocarro"):
            case ("Autobús"):
                vehiclePosition = 0;
                break;
            case ("Van"):
            case ("Carrinha"):
            case ("Camioneta"):
                vehiclePosition = 1;
                break;
            case ("Car"):
            case ("Carro"):
            case ("Coche"):
                vehiclePosition = 2;
                break;
        }
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
        if (rpm < MAX_ROTATIONS[vehiclePosition]) {
            if (gear == -1) {
                speed -= ((double) SPEED_REDLINE_REVERSE_GEAR[vehiclePosition] / ACCELERATION_REVERSE_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 1) {
                speed += ((double) SPEED_REDLINE_1ST_GEAR[vehiclePosition] / ACCELERATION_1ST_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 2) {
                speed += ((double) SPEED_REDLINE_2ND_GEAR[vehiclePosition] / ACCELERATION_2ND_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 3) {
                speed += ((double) SPEED_REDLINE_3RD_GEAR[vehiclePosition] / ACCELERATION_3RD_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 4) {
                speed += ((double) SPEED_REDLINE_4TH_GEAR[vehiclePosition] / ACCELERATION_4TH_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 5) {
                speed += ((double) SPEED_REDLINE_5TH_GEAR[vehiclePosition] / ACCELERATION_5TH_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 6) {
                speed += ((double) SPEED_REDLINE_6TH_GEAR[vehiclePosition] / ACCELERATION_6TH_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 7) {
                speed += ((double) SPEED_REDLINE_7TH_GEAR[vehiclePosition] / ACCELERATION_7TH_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
            else if (gear == 8) {
                speed += ((double) SPEED_REDLINE_8TH_GEAR[vehiclePosition] / ACCELERATION_8TH_GEAR[vehiclePosition] / INPUTS_PER_SECOND);
            }
        }
        upgrade_rpm();
    }

    private void decelerate(int deceleration_type) {
        double speed_loss = 0;
        if (deceleration_type == DECELERATION_BRAKING) {
            speed_loss = (double) BRAKE_PER_SECOND[vehiclePosition] / INPUTS_PER_SECOND;
        }
        else if (deceleration_type == DECELERATION_FRICTION) {
            speed_loss = (double) DECELERATION_PER_SECOND[vehiclePosition] / INPUTS_PER_SECOND;
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
            rpm = (int) Math.abs(speed * REDLINE[vehiclePosition] / SPEED_REDLINE_REVERSE_GEAR[vehiclePosition]);
        }
        else if (gear == 0) {
            rpm = 0;
        }
        else if (gear == 1) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_1ST_GEAR[vehiclePosition]);
        }
        else if (gear == 2) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_2ND_GEAR[vehiclePosition]);
        }
        else if (gear == 3) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_3RD_GEAR[vehiclePosition]);
        }
        else if (gear == 4) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_4TH_GEAR[vehiclePosition]);
        }
        else if (gear == 5) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_5TH_GEAR[vehiclePosition]);
        }
        else if (gear == 6) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_6TH_GEAR[vehiclePosition]);
        }
        else if (gear == 7) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_7TH_GEAR[vehiclePosition]);
        }
        else if (gear == 8) {
            rpm = (int) (speed * REDLINE[vehiclePosition] / SPEED_REDLINE_8TH_GEAR[vehiclePosition]);
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
        if (gear < GEAR_NUMBER[vehiclePosition]) {
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

    public int getVehiclePosition() {
        return vehiclePosition;
    }

    public void setVehiclePosition(int vehiclePosition) {
        this.vehiclePosition = vehiclePosition;
    }
}
