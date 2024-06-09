package com.viajar.viajar;

// A data object with all information concerning a location in Gibraltar

import android.content.Context;

import java.util.List;

public class LocationInfoGibraltar extends LocationInfo {

    public LocationInfoGibraltar(Context context) {
        setCountry(context.getString(R.string.gibraltar_short_name));
    }

}
