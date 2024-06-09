package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {"municipio", "province"},
        foreignKeys = {@ForeignKey(entity = Province.class, parentColumns = "province", childColumns = "province", onDelete = CASCADE)}
        )
public class Municipio {
    @NonNull
    public String municipio = "";
    @NonNull
    public String comarca = "";
    @NonNull
    public String province = "";
}
