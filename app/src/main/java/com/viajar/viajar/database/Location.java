package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey
    @NonNull
    public String name = "";

    public double latitude;
    public double longitude;
    public int altitude;
    public int batch;

    @ColumnInfo(name = "protected_area")
    public String protectedArea;
}
