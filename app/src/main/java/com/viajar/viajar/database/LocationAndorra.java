package com.viajar.viajar.database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name", onDelete = CASCADE)}
)
public class LocationAndorra {
    @PrimaryKey
    @NonNull
    public String name = "";

    @NonNull
    public String parish = "";
}
