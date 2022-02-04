package com.viajar.viajar;

import androidx.room.Dao;
import androidx.room.Query;

import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Destination;
import com.viajar.viajar.database.Location;

@Dao
public interface dao {

    // Location table

    @Query("SELECT COUNT(name) FROM location")
    int getLocationNumber();

    @Query("SELECT * FROM location")
    Location[] getAllLocations();

    // Connection table

    @Query("SELECT COUNT(order_a) FROM connection")
    int getConnectionNumber();

    // Destination table

    @Query("SELECT COUNT(location_a) FROM destination")
    int getDestinationNumber();

    // Gibraltar location table

    @Query("SELECT COUNT(name) FROM locationGibraltar")
    int getGibraltarLocationNumber();

    // Portuguese location table

    @Query("SELECT COUNT(name) FROM locationPortugal")
    int getPortugueseLocationNumber();

    // Spanish location table

    @Query("SELECT COUNT(name) FROM locationSpain")
    int getSpanishLocationNumber();

    // Concelho table

    @Query("SELECT COUNT(concelho) FROM concelho")
    int getConcelhoNumber();

    // Município table

    @Query("SELECT COUNT(municipio) FROM municipio")
    int getMunicipioNumber();

    // Comarca table

    @Query("SELECT COUNT(municipio) FROM comarca")
    int getComarcaNumber();

    // Província table

    @Query("SELECT COUNT(province) FROM province")
    int getProvinceNumber();

    // Required queries to collect all info about a location

    @Query("SELECT * FROM connection WHERE location_a = :locationName OR location_b = :locationName")
    Connection[] getLocationConnections(String locationName);

    @Query("SELECT * FROM location WHERE name = :name")
    Location getLocationByName(String name);

    @Query("SELECT COUNT(name) FROM locationGibraltar WHERE name = :locationName")
    int isLocationInGibraltar(String locationName);

    @Query("SELECT COUNT(name) FROM locationPortugal WHERE name = :locationName")
    int isLocationInPortugal(String locationName);

    @Query("SELECT COUNT(name) FROM locationSpain WHERE name = :locationName")
    int isLocationInSpain(String locationName);

    @Query("SELECT * FROM destination WHERE (location_a = :locationName AND starting_point = 1) OR (location_b = :locationName AND starting_point = 0)")
    Destination[] getLocationDestinations(String locationName);

    @Query("SELECT major_residential_area FROM locationgibraltar WHERE name = :name")
    String[] getMajorResidentialAreas(String name);

    @Query("SELECT parish FROM locationportugal, concelho WHERE locationportugal.concelho = concelho.concelho AND locationportugal.name = :locationName")
    String getParish(String locationName);

    @Query("SELECT concelho.concelho FROM locationportugal, concelho WHERE locationportugal.concelho = concelho.concelho AND locationportugal.name = :locationName")
    String getConcelho(String locationName);

    @Query("SELECT district FROM locationportugal, concelho WHERE locationportugal.concelho = concelho.concelho AND locationportugal.name = :locationName")
    String getPortugueseDistrict(String locationName);

    @Query("SELECT intermunicipal_entity FROM locationportugal, concelho WHERE locationportugal.concelho = concelho.concelho AND locationportugal.name = :locationName")
    String getIntermunicipalEntity(String locationName);

    @Query("SELECT region FROM locationportugal, concelho WHERE locationportugal.concelho = concelho.concelho AND locationportugal.name = :locationName")
    String getRegion(String locationName);

    @Query("SELECT municipio FROM locationspain, province WHERE locationspain.province = province.province AND locationspain.name = :locationName")
    String getMunicipio(String locationName);

    @Query("SELECT district FROM locationspain, province WHERE locationspain.province = province.province AND locationspain.name = :locationName")
    String getSpanishDistrict(String locationName);

    @Query("SELECT province.province FROM locationspain, province WHERE locationspain.province = province.province AND locationspain.name = :locationName")
    String getProvince(String locationName);

    @Query("SELECT autonomous_community FROM locationspain, province WHERE locationspain.province = province.province AND locationspain.name = :locationName")
    String getAutonomousCommunity(String locationName);

    @Query("SELECT comarca FROM comarca WHERE municipio = :municipio AND province = :province")
    String[] getComarcas(String municipio, String province);

    @Query("SELECT * FROM location WHERE batch > 0 AND batch <= :batch")
    Location[] getBatchLocations(int batch);

    // Required queries for the global map tab

    @Query("SELECT location1.latitude AS latitude1, location1.longitude AS longitude1, " +
            "location2.latitude AS latitude2, location2.longitude AS longitude2, " +
            "connection.extra_info AS extraInfo, connection.means_transport AS meansTransport " +
            "FROM location AS location1, location AS location2, connection " +
            "WHERE location1.name = connection.location_a AND location2.name = connection.location_b;")
    ConnectionCoordinates[] getConnectionsCoordinates();

    @Query("SELECT latitude FROM location WHERE latitude = (SELECT MAX(latitude) FROM location);")
    Double getNorthernmostLatitude();

    @Query("SELECT latitude FROM location WHERE latitude = (SELECT MIN(latitude) FROM location);")
    Double getSouthernmostLatitude();

    @Query("SELECT longitude FROM location WHERE longitude = (SELECT MIN(longitude) FROM location);")
    Double getWesternmostLongitude();

    @Query("SELECT longitude FROM location WHERE longitude = (SELECT MAX(longitude) FROM location);")
    Double getEasternmostLongitude();

    class ConnectionCoordinates {
        public double latitude1;
        public double longitude1;
        public double latitude2;
        public double longitude2;
        public String extraInfo;
        public String meansTransport;
    }
}
