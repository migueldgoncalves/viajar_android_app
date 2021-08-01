package com.viajar.viajar;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.viajar.viajar.database.Comarca;
import com.viajar.viajar.database.Concelho;
import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Destination;
import com.viajar.viajar.database.Location;
import com.viajar.viajar.database.LocationGibraltar;
import com.viajar.viajar.database.LocationPortugal;
import com.viajar.viajar.database.LocationSpain;
import com.viajar.viajar.database.Municipio;
import com.viajar.viajar.database.Province;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBInterface {

    private static DBInterface dbInterface;

    private SQLDatabase sqlDatabase;

    private static final String dbName = "Travel";

    // SET DEVELOPER MODE HERE
    private static final boolean developerMode = false; // If false, fetches DB from assets. If true, creates DB from .csv files

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

        return locationObject;
    }

    public int getLocationNumber(Context context) {
        return getDatabase(context).dao().getLocationNumber();
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

    public void populateDatabase(Context context) {
        Log.i("debug", "Starting population of database...");

        clearDatabase(context);
        populateProvinceTable(context);
        populateMunicipioTable(context);
        populateComarcaTable(context);
        populateConcelhoTable(context);
        populateLocationTable(context);
        populateConnectionTable(context);
        populateDestinationTable(context);
        populateLocationGibraltarTable(context);
        populateLocationPortugalTable(context);
        populateLocationSpainTable(context);

        Log.i("debug", "Database populated");
    }

    private void populateLocationTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.local);
            for (int i = 0; i < csvContent.size(); i++) {
                Location newLocation = new Location();
                List<String> csvLine = csvContent.get(i);

                newLocation.name = csvLine.get(0);
                newLocation.latitude = Double.parseDouble(csvLine.get(1));
                newLocation.longitude = Double.parseDouble(csvLine.get(2));
                newLocation.altitude = Integer.parseInt(csvLine.get(3));
                if (csvLine.size() == 5) { // Has extra info
                    newLocation.extraInfo = csvLine.get(4);
                }
                getDatabase(context).dao().insertLocations(newLocation);
            }
        }
        Log.i("debug", "Location table populated with " + getDatabase(context).dao().getLocationNumber() + " locations");
    }

    private void populateConnectionTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.ligacao);
            for (int i = 0; i < csvContent.size(); i++) {
                Connection newConnection = new Connection();
                List<String> csvLine = csvContent.get(i);

                newConnection.locationA = csvLine.get(0);
                newConnection.locationB = csvLine.get(1);
                newConnection.meansTransport = csvLine.get(2);
                newConnection.distance = Double.parseDouble(csvLine.get(3));
                if (csvLine.size() == 8) { // Has extra info
                    newConnection.extraInfo = csvLine.get(4);
                    newConnection.cardinalPoint = csvLine.get(5);
                    newConnection.orderA = Integer.parseInt(csvLine.get(6));
                    newConnection.orderB = Integer.parseInt(csvLine.get(7));
                } else {
                    newConnection.cardinalPoint = csvLine.get(4);
                    newConnection.orderA = Integer.parseInt(csvLine.get(5));
                    newConnection.orderB = Integer.parseInt(csvLine.get(6));
                }

                getDatabase(context).dao().insertConnections(newConnection);
            }
        }
        Log.i("debug", "Connection table populated with " + getDatabase(context).dao().getConnectionNumber() + " connections");
    }

    private void populateDestinationTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.destino);
            for (int i = 0; i < csvContent.size(); i++) {
                Destination newDestination = new Destination();
                List<String> csvLine = csvContent.get(i);

                newDestination.locationA = csvLine.get(0);
                newDestination.locationB = csvLine.get(1);
                newDestination.meansTransport = csvLine.get(2);
                newDestination.startingPoint = csvLine.get(3).equals("True"); // CSV files created using Python
                newDestination.destination = csvLine.get(4);
                getDatabase(context).dao().insertDestinations(newDestination);
            }
        }
        Log.i("debug", "Destination table populated with " + getDatabase(context).dao().getDestinationNumber() + " destinations");
    }

    private void populateLocationGibraltarTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.local_gibraltar);
            for (int i = 0; i < csvContent.size(); i++) {
                LocationGibraltar newGibraltarLocation = new LocationGibraltar();
                List<String> csvLine = csvContent.get(i);

                newGibraltarLocation.name = csvLine.get(0);
                newGibraltarLocation.majorResidentialArea = csvLine.get(1);

                getDatabase(context).dao().insertGibraltarLocations(newGibraltarLocation);
            }
        }
        Log.i("debug", "LocationGibraltar table populated with " + getDatabase(context).dao().getGibraltarLocationNumber() + " locations");
    }

    private void populateLocationPortugalTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.local_portugal);
            for (int i = 0; i < csvContent.size(); i++) {
                LocationPortugal newPortugueseLocation = new LocationPortugal();
                List<String> csvLine = csvContent.get(i);

                newPortugueseLocation.name = csvLine.get(0);
                newPortugueseLocation.parish = csvLine.get(1);
                newPortugueseLocation.concelho = csvLine.get(2);

                getDatabase(context).dao().insertPortugueseLocations(newPortugueseLocation);
            }
        }
        Log.i("debug", "LocationPortugal table populated with " + getDatabase(context).dao().getPortugueseLocationNumber() + " locations");
    }

    private void populateLocationSpainTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.local_espanha);
            for (int i = 0; i < csvContent.size(); i++) {
                LocationSpain newSpanishLocation = new LocationSpain();
                List<String> csvLine = csvContent.get(i);

                newSpanishLocation.name = csvLine.get(0);
                newSpanishLocation.municipio = csvLine.get(1);
                newSpanishLocation.province = csvLine.get(2);
                if (csvLine.size() == 4) { // Has district
                    newSpanishLocation.district = csvLine.get(3);
                }

                getDatabase(context).dao().insertSpanishLocations(newSpanishLocation);
            }
        }
        Log.i("debug", "LocationSpain table populated with " + getDatabase(context).dao().getSpanishLocationNumber() + " locations");
    }

    private void populateConcelhoTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.concelho);
            for (int i = 0; i < csvContent.size(); i++) {
                Concelho newConcelho = new Concelho();
                List<String> csvLine = csvContent.get(i);

                newConcelho.concelho = csvLine.get(0);
                newConcelho.intermunicipalEntity = csvLine.get(1);
                newConcelho.district = csvLine.get(2);
                newConcelho.region = csvLine.get(3);

                getDatabase(context).dao().insertConcelhos(newConcelho);
            }
        }
        Log.i("debug", "Concelho table populated with " + getDatabase(context).dao().getConcelhoNumber() + " concelhos");
    }

    private void populateMunicipioTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.municipio);
            for (int i = 0; i < csvContent.size(); i++) {
                Municipio newMunicipio = new Municipio();
                List<String> csvLine = csvContent.get(i);

                newMunicipio.municipio = csvLine.get(0);
                newMunicipio.province = csvLine.get(1);

                getDatabase(context).dao().insertMunicipios(newMunicipio);
            }
        }
        Log.i("debug", "Município table populated with " + getDatabase(context).dao().getMunicipioNumber() + " municípios");
    }

    private void populateComarcaTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.comarca);
            for (int i = 0; i < csvContent.size(); i++) {
                Comarca newComarca = new Comarca();
                List<String> csvLine = csvContent.get(i);

                newComarca.municipio = csvLine.get(0);
                newComarca.comarca = csvLine.get(1);
                newComarca.province = csvLine.get(2);

                getDatabase(context).dao().insertComarcas(newComarca);
            }
        }
        Log.i("debug", "Comarca table populated with " + getDatabase(context).dao().getComarcaNumber() + " comarcas");
    }

    private void populateProvinceTable(Context context) {
        if (developerMode) {
            List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.provincia);
            for (int i = 0; i < csvContent.size(); i++) {
                Province newProvince = new Province();
                List<String> csvLine = csvContent.get(i);

                newProvince.province = csvLine.get(0);
                newProvince.autonomousCommunity = csvLine.get(1);

                getDatabase(context).dao().insertProvincias(newProvince);
            }
        }
        Log.i("debug", "Province table populated with " + getDatabase(context).dao().getProvinceNumber() + " provinces");
    }

    public void clearDatabase(Context context) {
        if (developerMode) {
            getDatabase(context).dao().deletePortugueseLocations();
            getDatabase(context).dao().deleteSpanishLocations();
            getDatabase(context).dao().deleteGibraltarLocations();
            getDatabase(context).dao().deleteDestinations();
            getDatabase(context).dao().deleteConnections();
            getDatabase(context).dao().deleteLocations();
            getDatabase(context).dao().deleteConcelhos();
            getDatabase(context).dao().deleteComarcas();
            getDatabase(context).dao().deleteMunicipios();
            getDatabase(context).dao().deleteProvinces();
        }
    }

    public SQLDatabase getDatabase(Context context) {
        if (sqlDatabase == null) {
            if (DBInterface.developerMode) // DB will be created from .csv files, then manually copied to assets folder
                this.sqlDatabase = Room.databaseBuilder(context, SQLDatabase.class, DBInterface.dbName).build();
            else // DB will be fetched from assets folder
                this.sqlDatabase = Room.databaseBuilder(context, SQLDatabase.class, DBInterface.dbName).createFromAsset(dbName).build();
        }
        return this.sqlDatabase;
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DBInterface.dbName);
    }

    public static boolean getDeveloperMode() {
        return developerMode;
    }

}
