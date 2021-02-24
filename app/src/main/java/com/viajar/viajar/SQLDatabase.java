package com.viajar.viajar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.viajar.viajar.database.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class SQLDatabase extends RoomDatabase {
    public abstract locationDAO locationDAO();
}
