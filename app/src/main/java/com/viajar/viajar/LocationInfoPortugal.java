package com.viajar.viajar;

// A data object with all information concerning a location in Portugal

import android.content.Context;

public class LocationInfoPortugal extends LocationInfo {

    private String parish;
    private String municipality;
    private String district;
    private String intermunicipalEntity;
    private String region;

    public LocationInfoPortugal(Context context) {
        setCountry(context.getString(R.string.portugal));
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIntermunicipalEntity() {
        return intermunicipalEntity;
    }

    public void setIntermunicipalEntity(String intermunicipalEntity) {
        this.intermunicipalEntity = intermunicipalEntity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
