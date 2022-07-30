package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {"location_a", "location_b", "means_transport"},
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "location_a", onDelete = CASCADE),
                @ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "location_b", onDelete = CASCADE)}
)
public class Connection {
    @ColumnInfo(name = "location_a")
    @NonNull
    public String locationA = "";

    @ColumnInfo(name = "location_b")
    @NonNull
    public String locationB = "";

    @ColumnInfo(name = "means_transport")
    @NonNull
    public String meansTransport = "";

    public double distance = 0.0;

    @ColumnInfo(name = "way")
    public String way;

    @ColumnInfo(name = "cardinal_point")
    @NonNull
    public String cardinalPoint = "";

    @ColumnInfo(name = "order_a")
    public int orderA;

    @ColumnInfo(name = "order_b")
    public int orderB;
}
