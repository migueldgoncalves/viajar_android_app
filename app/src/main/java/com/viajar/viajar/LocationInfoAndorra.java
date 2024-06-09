package com.viajar.viajar;

// A data object with all information concerning a location in Gibraltar

import android.content.Context;

public class LocationInfoAndorra extends LocationInfo {

    private String parish;

    public LocationInfoAndorra(Context context) {
        setCountry(context.getString(R.string.andorra));
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }
}
