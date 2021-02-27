package com.viajar.viajar;

// A data object with all information concerning a location

import java.util.List;

public class LocationInfo {

    private String name;
    private List<String> surroundingLocations;
    private Double[] coordinates = new Double[]{0.0, 0.0};
    private int altitude;

    public LocationInfo(String name, List<String> surroundingLocations, double latitude, double longitude, int altitude) {
        setName(name);
        setSurroundingLocations(surroundingLocations);
        setCoordinates(latitude, longitude);
        setAltitude(altitude);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSurroundingLocations() {
        return surroundingLocations;
    }

    public void setSurroundingLocations(List<String> surroundingLocations) {
        this.surroundingLocations = surroundingLocations;
    }

    public double getLatitude() {
        return coordinates[0];
    }

    public void setLatitude(double latitude) {
        this.coordinates[0] = latitude;
    }

    public double getLongitude() {
        return coordinates[1];
    }

    public void setLongitude(double longitude) {
        this.coordinates[1] = longitude;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
}
