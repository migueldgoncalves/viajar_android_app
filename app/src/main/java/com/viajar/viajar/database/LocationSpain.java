package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name", onDelete = CASCADE),
                @ForeignKey(entity = Municipio.class, parentColumns = {"municipio", "province"}, childColumns = {"municipio", "province"}, onDelete = CASCADE)}
)
public class LocationSpain {
    @PrimaryKey
    @NonNull
    public String name = "";

    @NonNull
    public String municipio = "";
    @NonNull
    public String province = "";
    public String district;
}
