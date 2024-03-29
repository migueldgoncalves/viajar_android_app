package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name", onDelete = CASCADE),
                @ForeignKey(entity = Concelho.class, parentColumns = "concelho", childColumns = "concelho", onDelete = CASCADE)}
)
public class LocationPortugal {
    @PrimaryKey
    @NonNull
    public String name = "";

    @NonNull
    public String parish = "";
    @NonNull
    public String concelho = "";
}
