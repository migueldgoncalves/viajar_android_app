package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"name", "major_residential_area"},
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name")}
)
public class LocationGibraltar {
    @NonNull
    public String name = "";

    @ColumnInfo(name = "major_residential_area")
    @NonNull
    public String majorResidentialArea = "";
}
