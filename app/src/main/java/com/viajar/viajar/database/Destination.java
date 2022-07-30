package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {"location_a", "location_b", "means_transport", "starting_point", "destination"},
        foreignKeys = {@ForeignKey(entity = Connection.class, parentColumns = {"location_a", "location_b", "means_transport"}, childColumns = {"location_a", "location_b", "means_transport"}, onDelete = CASCADE)}
)
public class Destination {
    @ColumnInfo(name = "location_a")
    @NonNull
    public String locationA = "";

    @ColumnInfo(name = "location_b")
    @NonNull
    public String locationB = "";

    @ColumnInfo(name = "means_transport")
    @NonNull
    public String meansTransport = "";

    @ColumnInfo(name = "starting_point")
    public boolean startingPoint;

    @NonNull
    public String destination = "";
}
