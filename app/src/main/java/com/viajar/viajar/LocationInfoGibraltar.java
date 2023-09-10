package com.viajar.viajar;

// A data object with all information concerning a location in Gibraltar

import android.content.Context;

import java.util.List;

public class LocationInfoGibraltar extends LocationInfo {

    private List<String> majorResidentialAreas;

    public LocationInfoGibraltar(Context context) {
        setCountry(context.getString(R.string.gibraltar_short_name));
    }

    public List<String> getMajorResidentialAreas() {
        return majorResidentialAreas;
    }

    public void setMajorResidentialAreas(List<String> majorResidentialAreas) {
        this.majorResidentialAreas = majorResidentialAreas;
    }

}
