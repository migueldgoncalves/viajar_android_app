package com.viajar.viajar.database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "name", childColumns = "name", onDelete = CASCADE)}
)
public class LocationBeyondIberianPeninsula {
    @PrimaryKey
    @NonNull
    public String name = "";

    @NonNull
    public String country = "";

    @ColumnInfo(name = "osm_admin_level_3")
    public String osmAdminLevel3;
    @ColumnInfo(name = "osm_admin_level_4")
    public String osmAdminLevel4;
    @ColumnInfo(name = "osm_admin_level_5")
    public String osmAdminLevel5;
    @ColumnInfo(name = "osm_admin_level_6")
    public String osmAdminLevel6;
    @ColumnInfo(name = "osm_admin_level_7")
    public String osmAdminLevel7;
    @ColumnInfo(name = "osm_admin_level_8")
    public String osmAdminLevel8;
    @ColumnInfo(name = "osm_admin_level_9")
    public String osmAdminLevel9;
}
