package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {"comarca", "municipio", "province"},
        foreignKeys = {@ForeignKey(entity = Municipio.class, parentColumns = {"municipio", "province"}, childColumns = {"municipio", "province"}, onDelete = CASCADE)}
)
public class Comarca {
    @NonNull
    public String municipio = "";
    @NonNull
    public String comarca = "";
    @NonNull
    public String province = "";
}
