package com.viajar.viajar;

// A data object with all information concerning a location

import android.content.Context;

import com.viajar.viajar.utils.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LocationInfo {

    private String name;
    private HashMap<List<String>, List<String>> surroundingLocations;
    private HashMap<List<String>, List<String>> destinations; // Possible destinations along a certain way
    private HashMap<List<String>, String> routeNames; // Ex: road, railway, and river names
    private Double[] coordinates = new Double[]{0.0, 0.0};
    private int altitude;
    private String country;
    private String protectedArea;
    private String island;
    private int batch;

    public LocationInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<List<String>, List<String>> getSurroundingLocations() {
        return surroundingLocations;
    }

    public void setSurroundingLocations(HashMap<List<String>, List<String>> surroundingLocations) {
        this.surroundingLocations = surroundingLocations;
    }

    public List<String> getSurroundingLocationInfo(String locationName, String meansTransport) {
        return this.surroundingLocations.get(Arrays.asList(locationName, meansTransport));
    }

    public String getSurroundingLocationCardinalPoint(String locationName, String meansTransport) {
        return surroundingLocations.get(Arrays.asList(locationName, meansTransport)).get(0);
    }

    public Double getSurroundingLocationDistance(String locationName, String meansTransport) {
        return Double.valueOf(surroundingLocations.get(Arrays.asList(locationName, meansTransport)).get(1));
    }

    public int getSurroundingLocationOrder(String locationName, String meansTransport) {
        return Integer.parseInt(surroundingLocations.get(Arrays.asList(locationName, meansTransport)).get(2));
    }

    public List<String> getSurroundingLocationsByTransportMeans(String meansTransport) {
        List<String> surroundingLocations = new ArrayList<>();
        for (List<String> connectionInfo : this.surroundingLocations.keySet()) {
            String surroundingLocation = connectionInfo.get(0);
            String surroundingLocationTransportMeans = connectionInfo.get(1);
            if (surroundingLocationTransportMeans.equals(meansTransport))
                surroundingLocations.add(surroundingLocation);
        }
        return surroundingLocations;
    }

    public void setSurroundingLocationInfo(String locationName, String meansTransport, List<String> surroundingLocationInfo) {
        this.surroundingLocations.put(Arrays.asList(locationName, meansTransport), surroundingLocationInfo);
    }

    public void setSurroundingLocationCardinalPoint(String locationName, String meansTransport, String cardinalPoint) {
        if (getSurroundingLocationInfo(locationName, meansTransport) == null)
            setSurroundingLocationInfo(locationName, meansTransport, new ArrayList<>());
        surroundingLocations.get(Arrays.asList(locationName, meansTransport)).set(0, cardinalPoint);
    }

    public void setSurroundingLocationDistance(String locationName, String meansTransport, Double distance) {
        if (getSurroundingLocationInfo(locationName, meansTransport) == null)
            setSurroundingLocationInfo(locationName, meansTransport, new ArrayList<>());
        if (getSurroundingLocationInfo(locationName, meansTransport).size() == 0)
            setSurroundingLocationCardinalPoint(locationName, meansTransport, "");
        surroundingLocations.get(Arrays.asList(locationName, meansTransport)).set(1, String.valueOf(distance));
    }

    public void setSurroundingLocationOrder(String locationName, String meansTransport, int order) {
        if (getSurroundingLocationInfo(locationName, meansTransport) == null)
            setSurroundingLocationInfo(locationName, meansTransport, new ArrayList<>());
        if (getSurroundingLocationInfo(locationName, meansTransport).size() == 0)
            setSurroundingLocationCardinalPoint(locationName, meansTransport, "");
        if (getSurroundingLocationInfo(locationName, meansTransport).size() == 1)
            setSurroundingLocationDistance(locationName, meansTransport, 0.0);
        surroundingLocations.get(Arrays.asList(locationName, meansTransport)).set(2, String.valueOf(order));
    }

    public HashMap<List<String>, List<String>> getAllDestinations() {
        return destinations;
    }

    public void setAllDestinations(HashMap<List<String>, List<String>> destinations) {
        this.destinations = destinations;
    }

    public List<String> getDestinationsFromSurroundingLocation(String locationName, String meansTransport) {
        return this.destinations.get(Arrays.asList(locationName, meansTransport));
    }

    public boolean hasDestinationsInMeansTransport(String meansTransport) {
        for (List<String> key : this.getAllDestinations().keySet()) {
            String locationName = key.get(0);
            String keyMeansTransport = key.get(1);
            if (hasDestinationsFromSurroundingLocation(locationName, keyMeansTransport) && keyMeansTransport.equals(meansTransport))
                return true;
        }
        return false;
    }

    public boolean hasDestinationsFromSurroundingLocation(String locationName, String meansTransport) {
        return this.getDestinationsFromSurroundingLocation(locationName, meansTransport).size() > 0;
    }

    public void setDestinationsFromSurroundingLocation(String locationName, String meansTransport, List<String> destinations) {
        this.destinations.put(Arrays.asList(locationName, meansTransport), destinations);
    }

    public Collection<String> getRouteNames() {
        return routeNames.values();
    }

    public void setRouteNames(HashMap<List<String>, String> routeNames) {
        this.routeNames = routeNames;
    }

    public String getRouteName(String locationName, String meansTransport) {
        return this.routeNames.get(Arrays.asList(locationName, meansTransport));
    }

    public boolean hasRouteName(String locationName, String meansTransport) {
        return this.routeNames.containsKey(Arrays.asList(locationName, meansTransport));
    }

    public void setRouteName(String locationName, String meansTransport, String routeName) {
        this.routeNames.put(Arrays.asList(locationName, meansTransport), routeName);
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

    public boolean isSameLocation(double latitude, double longitude) {
        return (latitude == this.getLatitude()) && (longitude == this.getLongitude());
    }

    public boolean isInIberianPeninsula(Context context) {
        List<String> allIberianPeninsulaCountries = Arrays.asList(context.getString(R.string.portugal), context.getString(R.string.spain), context.getString(R.string.gibraltar_short_name), context.getString(R.string.andorra));
        return allIberianPeninsulaCountries.contains(this.getCountry());
    }

    public Coordinate getCoordinatesAsObject() {
        return new Coordinate(getLatitude(), getLongitude());
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProtectedArea() {
        return protectedArea;
    }

    public boolean hasProtectedArea() {
        return protectedArea != null;
    }

    public void setProtectedArea(String protectedArea) {
        this.protectedArea = protectedArea;
    }

    public String getIsland() {
        return island;
    }

    public boolean hasIsland() {
        return island != null;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }
}
