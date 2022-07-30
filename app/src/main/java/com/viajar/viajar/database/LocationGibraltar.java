package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {"name", "major_residential_area"},
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name", onDelete = CASCADE)}
)
public class LocationGibraltar {
    @NonNull
    public String name = "";

    @ColumnInfo(name = "major_residential_area")
    @NonNull
    public String majorResidentialArea = "";
}
