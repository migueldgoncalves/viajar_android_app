package com.viajar.viajar;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Location;

import java.util.ArrayList;
import java.util.List;

public class DBInterface {

    private static DBInterface dbInterface;

    private SQLDatabase sqlDatabase;

    private DBInterface() {

    }

    public static DBInterface getDBInterface(Context context) {
        if (DBInterface.dbInterface == null) {
            DBInterface.dbInterface = new DBInterface();
            DBInterface.dbInterface.sqlDatabase = DBInterface.dbInterface.getDatabase(context);
        }
        return DBInterface.dbInterface;
    }

    public void populateDatabase(Context context) {
        Log.d("debug", "Starting population of database...");

        clearDatabase(context);
        populateLocationTable(context);
        populateConnectionTable(context);

        Log.d("debug", "Database populated");
    }

    private void populateLocationTable(Context context) {
        List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.local);
        for (int i=0; i<csvContent.size(); i++) {
            Location newLocation = new Location();
            List<String> csvLine = csvContent.get(i);

            newLocation.name = csvLine.get(0);
            newLocation.latitude = Double.parseDouble(csvLine.get(1));
            newLocation.longitude = Double.parseDouble(csvLine.get(2));
            newLocation.altitude = Double.parseDouble(csvLine.get(3));
            if (csvLine.size() == 5) { // Has extra info
                newLocation.extraInfo = csvLine.get(4);
            }
            getDatabase(context).dao().insertLocations(newLocation);
        }
        Log.d("debug", "Location table populated with " + getDatabase(context).dao().getLocationNumber() + " locations");
    }

    private void populateConnectionTable(Context context) {
        List<List<String>> csvContent = CsvParser.csvParser(context, R.raw.ligacao);
        for (int i=0; i<csvContent.size(); i++) {
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
        Log.d("debug", "Connection table populated with " + getDatabase(context).dao().getConnectionNumber() + " connections");
    }

    public void clearDatabase(Context context) {
        getDatabase(context).dao().deleteConnections();
        getDatabase(context).dao().deleteLocations();
    }

    public SQLDatabase getDatabase(Context context) {
        if (sqlDatabase == null) {
            this.sqlDatabase = Room.databaseBuilder(context, SQLDatabase.class, context.getString(R.string.app_name)).build();
        }
        return this.sqlDatabase;
    }

}
