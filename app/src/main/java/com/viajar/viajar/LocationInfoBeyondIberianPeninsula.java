package com.viajar.viajar;

// A data object with all information concerning a location in Gibraltar

import android.content.Context;

import java.util.HashMap;

public class LocationInfoBeyondIberianPeninsula extends LocationInfo {

    private HashMap<Integer, String> osmAdminLevels;

    public LocationInfoBeyondIberianPeninsula(Context context) {
        this.osmAdminLevels = new HashMap<>();
    }

    public void setOsmAdminLevels(String osmAdminLevel3, String osmAdminLevel4, String osmAdminLevel5,
                                  String osmAdminLevel6, String osmAdminLevel7, String osmAdminLevel8,
                                  String osmAdminLevel9) {
        this.osmAdminLevels.put(3, osmAdminLevel3);
        this.osmAdminLevels.put(4, osmAdminLevel4);
        this.osmAdminLevels.put(5, osmAdminLevel5);
        this.osmAdminLevels.put(6, osmAdminLevel6);
        this.osmAdminLevels.put(7, osmAdminLevel7);
        this.osmAdminLevels.put(8, osmAdminLevel8);
        this.osmAdminLevels.put(9, osmAdminLevel9);
    }

    public String getOsmAdminLevel(int osmAdminLevel, String value){
        return this.osmAdminLevels.get(osmAdminLevel);
    }

    public HashMap<Integer, String> getAllOsmAdminLevels() {
        return this.osmAdminLevels;
    }

    public String getOsmAdminLevelString() {
        // From smaller to larger
        StringBuilder returnValue = new StringBuilder();
        HashMap<Integer, String> adminLevels = getAllOsmAdminLevels();
        int maxAdminLevel = 9;
        int minAdminLevel = 3;
        for (int i=maxAdminLevel; i>=minAdminLevel; i--) {
            String adminLevelValue = adminLevels.get(i);
            if (adminLevelValue != null && !adminLevelValue.isEmpty()) {
                returnValue.append(adminLevelValue);
                returnValue.append(", ");
            }
        }
        returnValue.append(getCountry());
        return returnValue.toString();
    }
}
