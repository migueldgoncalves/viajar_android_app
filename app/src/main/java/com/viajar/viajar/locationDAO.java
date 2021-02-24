package com.viajar.viajar;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.viajar.viajar.database.Location;

@Dao
public interface locationDAO {
    @Insert
    void insertElements(Location... elements);

    @Query("SELECT COUNT(name) FROM location")
    int getLocationNumber();
}
