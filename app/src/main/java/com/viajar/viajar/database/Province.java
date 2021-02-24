package com.viajar.viajar.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Province {
    @PrimaryKey
    @NonNull
    public String province = "";

    @ColumnInfo(name = "autonomous_community")
    @NonNull
    public String autonomousCommunity = "";
}
