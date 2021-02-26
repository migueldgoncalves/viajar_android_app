package com.viajar.viajar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Location;

@Dao
public interface dao {
    @Insert
    void insertConnections(Connection... connections);

    @Insert
    void insertLocations(Location... locations);

    @Query("DELETE FROM connection")
    void deleteConnections();

    @Query("DELETE FROM location")
    void deleteLocations();

    @Query("SELECT COUNT(order_a) FROM connection")
    int getConnectionNumber();

    @Query("SELECT COUNT(name) FROM location")
    int getLocationNumber();
}
