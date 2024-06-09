package com.viajar.viajar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.viajar.viajar.database.Concelho;
import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Destination;
import com.viajar.viajar.database.Location;
import com.viajar.viajar.database.LocationGibraltar;
import com.viajar.viajar.database.LocationPortugal;
import com.viajar.viajar.database.LocationSpain;
import com.viajar.viajar.database.Municipio;
import com.viajar.viajar.database.Province;

@Database(entities = {
        Location.class, Connection.class, Destination.class, LocationGibraltar.class,
        LocationPortugal.class, LocationSpain.class, Concelho.class, Municipio.class,
        Province.class},
        version = 1)
public abstract class SQLDatabase extends RoomDatabase {
    public abstract dao dao();
}
