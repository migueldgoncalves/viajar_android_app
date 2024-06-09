package com.viajar.viajar;

import android.content.Context;

import java.util.List;

public class LocationInfoSpain extends LocationInfo {

    private String district;
    private String municipality;
    private String comarca;
    private String province;
    private String autonomousCommunity;

    public LocationInfoSpain(Context context) {
        setCountry(context.getString(R.string.spain));
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public boolean hasDistrict() {
        return district != null;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getComarca() {
        return comarca;
    }

    public void setComarca(String comarca) {
        this.comarca = comarca;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAutonomousCommunity() {
        return autonomousCommunity;
    }

    public void setAutonomousCommunity(String autonomousCommunity) {
        this.autonomousCommunity = autonomousCommunity;
    }
}
