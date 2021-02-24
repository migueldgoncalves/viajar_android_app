package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = {"municipio", "province"},
        foreignKeys = {@ForeignKey(entity = Province.class, parentColumns = "province", childColumns = "province")}
        )
public class Municipio {
    @NonNull
    public String municipio = "";
    @NonNull
    public String province = "";
}
