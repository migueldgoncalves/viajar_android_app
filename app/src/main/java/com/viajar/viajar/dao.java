package com.viajar.viajar;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.viajar.viajar.database.Comarca;
import com.viajar.viajar.database.Concelho;
import com.viajar.viajar.database.Connection;
import com.viajar.viajar.database.Destination;
import com.viajar.viajar.database.Location;
import com.viajar.viajar.database.LocationGibraltar;
import com.viajar.viajar.database.LocationPortugal;
import com.viajar.viajar.database.LocationSpain;
import com.viajar.viajar.database.Municipio;
import com.viajar.viajar.database.Province;

@Dao
public interface dao {

    // Location table

    @Insert
    void insertLocations(Location... locations);

    @Query("DELETE FROM location")
    void deleteLocations();

    @Query("SELECT COUNT(name) FROM location")
    int getLocationNumber();

    @Query("SELECT * FROM location")
    Location[] getAllLocations();

    // Connection table

    @Insert
    void insertConnections(Connection... connections);

    @Query("DELETE FROM connection")
    void deleteConnections();

    @Query("SELECT COUNT(order_a) FROM connection")
    int getConnectionNumber();

    // Destination table

    @Insert
    void insertDestinations(Destination... destinations);

    @Query("DELETE FROM destination")
    void deleteDestinations();

    @Query("SELECT COUNT(location_a) FROM destination")
    int getDestinationNumber();

    // Gibraltar location table

    @Insert
    void insertGibraltarLocations(LocationGibraltar... gibraltarLocations);

    @Query("DELETE FROM locationGibraltar")
    void deleteGibraltarLocations();

    @Query("SELECT COUNT(name) FROM locationGibraltar")
    int getGibraltarLocationNumber();

    // Portuguese location table

    @Insert
    void insertPortugueseLocations(LocationPortugal... portugueseLocations);

    @Query("DELETE FROM locationPortugal")
    void deletePortugueseLocations();

    @Query("SELECT COUNT(name) FROM locationPortugal")
    int getPortugueseLocationNumber();

    // Spanish location table

    @Insert
    void insertSpanishLocations(LocationSpain... spanishLocations);

    @Query("DELETE FROM locationSpain")
    void deleteSpanishLocations();

    @Query("SELECT COUNT(name) FROM locationSpain")
    int getSpanishLocationNumber();

    // Concelho table

    @Insert
    void insertConcelhos(Concelho... concelhos);

    @Query("DELETE FROM concelho")
    void deleteConcelhos();

    @Query("SELECT COUNT(concelho) FROM concelho")
    int getConcelhoNumber();

    // Município table

    @Insert
    void insertMunicipios(Municipio... municipios);

    @Query("DELETE FROM municipio")
    void deleteMunicipios();

    @Query("SELECT COUNT(municipio) FROM municipio")
    int getMunicipioNumber();

    // Comarca table

    @Insert
    void insertComarcas(Comarca... comarcas);

    @Query("DELETE FROM comarca")
    void deleteComarcas();

    @Query("SELECT COUNT(municipio) FROM comarca")
    int getComarcaNumber();

    // Província table

    @Insert
    void insertProvincias(Province... provinces);

    @Query("DELETE FROM province")
    void deleteProvinces();

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
}
