package com.viajar.viajar;

import android.content.Context;

import androidx.room.Room;

import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Destination;
import com.viajar.viajar.database.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBInterface {

    private static DBInterface dbInterface;

    private SQLDatabase sqlDatabase;

    private static final String dbName = "Travel";

    private DBInterface() {

    }

    public static DBInterface getDBInterface(Context context) {
        if (DBInterface.dbInterface == null) {
            DBInterface.dbInterface = new DBInterface();
            DBInterface.dbInterface.sqlDatabase = DBInterface.dbInterface.getDatabase(context);
        }
        return DBInterface.dbInterface;
    }

    public LocationInfo generateLocationObject(Context context, String locationName) {
        // Determine the country - Update when adding more countries
        String country;
        if (getDatabase(context).dao().isLocationInPortugal(locationName) == 1) {
            country = "Portugal";
        } else if (getDatabase(context).dao().isLocationInSpain(locationName) == 1) {
            country = "Spain";
        } else if (getDatabase(context).dao().isLocationInGibraltar(locationName) == 1) {
            country = "Gibraltar";
        } else {
            return null; // Invalid location
        }

        // Determine the surrounding locations and respective connection info
        HashMap<List<String>, List<String>> surroundingLocations = new HashMap<>();
        HashMap<List<String>, String> routeNames = new HashMap<>();
        Connection[] locationConnections = getDatabase(context).dao().getLocationConnections(locationName);
        for (Connection connection : locationConnections) {
            String surroundingLocation;
            String cardinalPoint;
            int order;
            if (connection.locationA.equals(locationName)) { // Surrounding location is the location B
                surroundingLocation = connection.locationB;
                cardinalPoint = connection.cardinalPoint;
                order = connection.orderA;
            } else {
                surroundingLocation = connection.locationA;
                cardinalPoint = getOppositeCardinalPoint(connection.cardinalPoint);
                order = connection.orderB;
            }
            String meansTransport = connection.meansTransport;
            Double distance = connection.distance;
            surroundingLocations.put(Arrays.asList(surroundingLocation, meansTransport), Arrays.asList(cardinalPoint, String.valueOf(distance), String.valueOf(order)));
            if (!connection.extraInfo.equals(""))
                routeNames.put(Arrays.asList(surroundingLocation, meansTransport), connection.extraInfo);
            else
                routeNames.put(Arrays.asList(surroundingLocation, meansTransport), null);
        }

        // Determine the available destinations
        HashMap<List<String>, List<String>> locationDestinations = new HashMap<>();
        Destination[] destinationsDBObjects = getDatabase(context).dao().getLocationDestinations(locationName);
        for (List<String> connectionInfo : surroundingLocations.keySet()) {
            String surroundingLocation = connectionInfo.get(0);
            String meansTransport = connectionInfo.get(1);
            List<String> surroundingLocationDestinations = new ArrayList<>();
            for (Destination destination : destinationsDBObjects) {
                if ((Arrays.asList(new String[]{destination.locationA, destination.locationB}).contains(surroundingLocation)) && (destination.meansTransport.equals(meansTransport)))
                    surroundingLocationDestinations.add(destination.destination);
            }
            locationDestinations.put(connectionInfo, surroundingLocationDestinations);
        }

        // Determine the remaining general parameters
        Location locationDBObject = getDatabase(context).dao().getLocationByName(locationName);
        double latitude = locationDBObject.latitude;
        double longitude = locationDBObject.longitude;
        int altitude = locationDBObject.altitude;
        String protectedArea = locationDBObject.extraInfo;
        int batch = locationDBObject.batch;

        // Determine the country-specific parameters and create the location
        LocationInfo locationObject;
        if (country.equals("Portugal")) {
            locationObject = new LocationInfoPortugal();
            ((LocationInfoPortugal) locationObject).setParish(getDatabase(context).dao().getParish(locationName));
            ((LocationInfoPortugal) locationObject).setMunicipality(getDatabase(context).dao().getConcelho(locationName));
            ((LocationInfoPortugal) locationObject).setDistrict(getDatabase(context).dao().getPortugueseDistrict(locationName));
            ((LocationInfoPortugal) locationObject).setIntermunicipalEntity(getDatabase(context).dao().getIntermunicipalEntity(locationName));
            ((LocationInfoPortugal) locationObject).setRegion(getDatabase(context).dao().getRegion(locationName));
        } else if (country.equals("Spain")) {
            locationObject = new LocationInfoSpain();
            String municipio = getDatabase(context).dao().getMunicipio(locationName);
            String province = getDatabase(context).dao().getProvince(locationName);
            ((LocationInfoSpain) locationObject).setMunicipality(municipio);
            ((LocationInfoSpain) locationObject).setDistrict(getDatabase(context).dao().getSpanishDistrict(locationName));
            ((LocationInfoSpain) locationObject).setProvince(province);
            ((LocationInfoSpain) locationObject).setAutonomousCommunity(getDatabase(context).dao().getAutonomousCommunity(locationName));
            ((LocationInfoSpain) locationObject).setComarcas(Arrays.asList(getDatabase(context).dao().getComarcas(municipio, province)));
        } else if (country.equals("Gibraltar")) {
            locationObject = new LocationInfoGibraltar();
            ((LocationInfoGibraltar) locationObject).setMajorResidentialAreas(Arrays.asList(getDatabase(context).dao().getMajorResidentialAreas(locationName)));
        } else {
            return null; // Add more countries as required
        }
        locationObject.setName(locationName);
        locationObject.setSurroundingLocations(surroundingLocations);
        locationObject.setAllDestinations(locationDestinations);
        locationObject.setRouteNames(routeNames);
        locationObject.setCoordinates(latitude, longitude);
        locationObject.setAltitude(altitude);
        locationObject.setProtectedArea(protectedArea);
        locationObject.setBatch(batch);

        return locationObject;
    }

    public int getLocationNumber(Context context) {
        return getDatabase(context).dao().getLocationNumber();
    }

    public ArrayList<String[]> getAllCoordinatesNamesBatches(Context context) {
        ArrayList<String[]> allCoordinatesNamesBatches = new ArrayList<>();
        Location[] locations = getDatabase(context).dao().getAllLocations();
        for (Location location : locations) {
            String[] locationData = new String[4];
            locationData[0] = Double.toString(location.latitude);
            locationData[1] = Double.toString(location.longitude);
            locationData[2] = location.name;
            locationData[3] = String.valueOf(location.batch);
            allCoordinatesNamesBatches.add(locationData);
        }
        return allCoordinatesNamesBatches;
    }

    public ArrayList<String[]> getAllConnectionCoordinates(Context context) {
        ArrayList<String[]> allConnectionsCoordinates = new ArrayList<>();
        dao.ConnectionCoordinates[] connectionsCoordinates = getDatabase(context).dao().getConnectionsCoordinates();
        for (dao.ConnectionCoordinates connectionCoordinates : connectionsCoordinates) {
            String[] coordinatesAndExtraInfo = new String[6];
            coordinatesAndExtraInfo[0] = Double.toString(connectionCoordinates.latitude1);
            coordinatesAndExtraInfo[1] = Double.toString(connectionCoordinates.longitude1);
            coordinatesAndExtraInfo[2] = Double.toString(connectionCoordinates.latitude2);
            coordinatesAndExtraInfo[3] = Double.toString(connectionCoordinates.longitude2);
            coordinatesAndExtraInfo[4] = connectionCoordinates.extraInfo;
            coordinatesAndExtraInfo[5] = connectionCoordinates.meansTransport;
            allConnectionsCoordinates.add(coordinatesAndExtraInfo);
        }
        return allConnectionsCoordinates;
    }

    public ArrayList<Double[]> getMapExtremePoints(Context context) {
        ArrayList<Double[]> mapExtremePoints = new ArrayList<>();

        Double northernmostLatitude = getDatabase(context).dao().getNorthernmostLatitude();
        Double southernmostLatitude = getDatabase(context).dao().getSouthernmostLatitude();
        Double westernmostLongitude = getDatabase(context).dao().getWesternmostLongitude();
        Double easternmostLongitude = getDatabase(context).dao().getEasternmostLongitude();

        Double[] northeastCorner = new Double[]{northernmostLatitude, easternmostLongitude};
        Double[] southwestCorner = new Double[]{southernmostLatitude, westernmostLongitude};

        mapExtremePoints.add(northeastCorner);
        mapExtremePoints.add(southwestCorner);

        return mapExtremePoints;
    }

    private String getOppositeCardinalPoint(String cardinalPoint) {
        switch (cardinalPoint) {
            case TravelActivity.NORTH:
                return TravelActivity.SOUTH;
            case TravelActivity.NORTHEAST:
                return TravelActivity.SOUTHWEST;
            case TravelActivity.EAST:
                return TravelActivity.WEST;
            case TravelActivity.SOUTHEAST:
                return TravelActivity.NORTHWEST;
            case TravelActivity.SOUTH:
                return TravelActivity.NORTH;
            case TravelActivity.SOUTHWEST:
                return TravelActivity.NORTHEAST;
            case TravelActivity.WEST:
                return TravelActivity.EAST;
            case TravelActivity.NORTHWEST:
                return TravelActivity.SOUTHEAST;
            default:
                return null;
        }
    }

    public SQLDatabase getDatabase(Context context) {
        if (sqlDatabase == null) {
            this.sqlDatabase = Room.databaseBuilder(context, SQLDatabase.class, DBInterface.dbName).createFromAsset(dbName).build();
        }
        return this.sqlDatabase;
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DBInterface.dbName);
    }

}
