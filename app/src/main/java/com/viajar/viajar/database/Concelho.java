package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Concelho {
    @PrimaryKey
    @NonNull
    public String concelho = "";

    @ColumnInfo(name = "intermunicipal_entity")
    @NonNull
    public String intermunicipalEntity = "";

    @NonNull
    public String district = "";
    @NonNull
    public String region = "";
}
