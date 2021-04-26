package com.viajar.viajar;

import java.util.List;

public class LocationInfoSpain extends LocationInfo {

    private String district;
    private String municipality;
    private List<String> comarcas;
    private String province;
    private String autonomousCommunity;

    public LocationInfoSpain() {
        setCountry("Spain");
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

    public List<String> getComarcas() {
        return comarcas;
    }

    public void setComarcas(List<String> comarcas) {
        this.comarcas = comarcas;
    }

    public void addComarca(String comarca) {
        comarcas.add(comarca);
    }

    public void removeComarca(String comarca) {
        comarcas.remove(comarca);
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
