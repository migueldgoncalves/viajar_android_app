package com.viajar.viajar.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.viajar.viajar.LocationInfo;
import com.viajar.viajar.LocationInfoGibraltar;
import com.viajar.viajar.LocationInfoPortugal;
import com.viajar.viajar.LocationInfoSpain;
import com.viajar.viajar.R;
import com.viajar.viajar.TravelActivity;
import com.viajar.viajar.utils.Utils;

import java.util.List;

public class InfoPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travel_info, container, false);
    }

    @Override
    public void onResume() {
        LocationInfo currentLocation = ((TravelActivity) requireActivity()).getCurrentLocation();
        String currentLocationName = ((TravelActivity) requireActivity()).getCurrentLocationName();

        super.onResume();

        // Hides both bottom navigation bars
        View bottomNavigationViewGPS = requireActivity().findViewById(R.id.bottomNavigationViewGPS);
        requireActivity().runOnUiThread(() -> bottomNavigationViewGPS.setVisibility(View.INVISIBLE));
        View bottomNavigationViewMap = requireActivity().findViewById(R.id.bottomNavigationViewMap);
        requireActivity().runOnUiThread(() -> bottomNavigationViewMap.setVisibility(View.INVISIBLE));

        TextView textView = requireActivity().findViewById(R.id.locationTextViewInfo);
        textView.setText(currentLocationName);
        EditText editText = requireActivity().findViewById(R.id.travelInfoText);

        if (currentLocation != null) {
            setLocationInfo(editText, currentLocation); // Sets all text related to the location info
        }
    }

    /**
     * Sets the location info for the Info Fragment
     * @param editText The EditText widget that will contain the text to display
     * @param currentLocation The object representing the current location
     */
    private void setLocationInfo(EditText editText, LocationInfo currentLocation) {
        editText.setText(""); // Clears widget

        setGlobalLocationInfo(editText, currentLocation);

        // Country-specific information
        if (currentLocation.getCountry().equals(getString(R.string.gibraltar_short_name))) {
            setGibraltarLocationInfo(editText, currentLocation);
        } else if (currentLocation.getCountry().equals(getString(R.string.portugal))) {
            setPortugueseLocationInfo(editText, currentLocation);
        } else if (currentLocation.getCountry().equals(getString(R.string.spain))) {
            setSpanishLocationInfo(editText, currentLocation);
        }
    }

    /**
     * Adds the global location info to the Info Fragment
     * @param editText The EditText widget that will contain the text to display
     * @param currentLocation The object representing the current location
     */
    private void setGlobalLocationInfo(EditText editText, LocationInfo currentLocation) {
        // Values
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        int altitude = currentLocation.getAltitude();
        String protectedArea = currentLocation.getProtectedArea();

        // Coordinates
        String coordinatesString = getString(R.string.info_tab_coordinates_string, latitude, longitude);
        editText.append(coordinatesString);
        editText.append("\n");

        // Altitude
        String altitudeString;
        if (altitude == 1) // 1 meter
            altitudeString = getString(R.string.info_tab_altitude_string_singular);
        else // Any altitude except 1 meter
            altitudeString = getString(R.string.info_tab_altitude_string, altitude);
        editText.append(altitudeString);
        editText.append("\n");

        // Protected area
        if (protectedArea != null && !protectedArea.equals("")) {
            editText.append(protectedArea); // Nothing to add to string - Will be written as is
            editText.append("\n");
        }

        editText.append("\n"); // Extra line to separate global info from country-specific info
    }

    /**
     * Adds the location info specific to Portugal to the Info Fragment, assuming the current location is in Portugal
     * @param editText The EditText widget that will contain the text to display
     * @param currentLocation The object representing the current location
     */
    private void setPortugueseLocationInfo(EditText editText, LocationInfo currentLocation) {
        // Values
        String parish = ((LocationInfoPortugal) currentLocation).getParish();
        String municipality = ((LocationInfoPortugal) currentLocation).getMunicipality();
        String district = ((LocationInfoPortugal) currentLocation).getDistrict();
        String intermunicipalEntity = ((LocationInfoPortugal) currentLocation).getIntermunicipalEntity();
        String region = ((LocationInfoPortugal) currentLocation).getRegion();

        // Parish (Freguesia in Portuguese)
        String parishFieldName = getString(R.string.parish_pt);
        String parishString = getString(R.string.info_tab_country_specific_info, parishFieldName, parish);
        editText.append(parishString);
        editText.append("\n");

        // Municipality (Concelho in Portuguese)
        String municipalityFieldName = getString(R.string.concelho);
        String municipalityString = getString(R.string.info_tab_country_specific_info, municipalityFieldName, municipality);
        editText.append(municipalityString);
        editText.append("\n");

        // District (Distrito in Portuguese)
        String districtFieldName = getString(R.string.district);
        String districtString = getString(R.string.info_tab_country_specific_info, districtFieldName, district);
        editText.append(districtString);
        editText.append("\n");

        // Intermunicipal Entity (Entidade Intermunicipal in Portuguese)
        String intermunicipalEntityFieldName = getString(R.string.intermunicipal_entity);
        String intermunicipalEntityString = getString(R.string.info_tab_country_specific_info, intermunicipalEntityFieldName, intermunicipalEntity);
        editText.append(intermunicipalEntityString);
        editText.append("\n");

        // Region (Região in Portuguese)
        String regionFieldName = getString(R.string.region);
        String regionString = getString(R.string.info_tab_country_specific_info, regionFieldName, region);
        editText.append(regionString);
        editText.append("\n");

        // Country
        String countryLongName = getString(R.string.portugal);
        setCountryInfo(editText, countryLongName);
    }

    /**
     * Adds the location info specific to Spain to the Info Fragment, assuming the current location is in Spain
     * @param editText The EditText widget that will contain the text to display
     * @param currentLocation The object representing the current location
     */
    private void setSpanishLocationInfo(EditText editText, LocationInfo currentLocation) {
        // Administrative divisions
        String province = ((LocationInfoSpain) currentLocation).getProvince();
        String autonomousCommunity = ((LocationInfoSpain) currentLocation).getAutonomousCommunity();
        List<String> comarcas = ((LocationInfoSpain) currentLocation).getComarcas();
        String municipality = ((LocationInfoSpain) currentLocation).getMunicipality();
        String district = ((LocationInfoSpain) currentLocation).getDistrict();

        // District (Distrito in Spanish)
        // Galicia has instead Parroquias (singular: Parroquia)
        // Region of Murcia has instead Pedanías (singular: Pedanía)

        // District - Get field name
        String districtFieldName = "";
        if (autonomousCommunity.equals(getString(R.string.galicia))) // Galicia has parroquias instead
            districtFieldName = getString(R.string.parish_es);
        else if (autonomousCommunity.equals(getString(R.string.region_of_murcia))) // Region of Murcia has pedanías instead
            districtFieldName = getString(R.string.pedania);
        else // Default
            districtFieldName = getString(R.string.district);

        // District - Get field value
        if (district != null && !(district.equals(""))) { // Only some municipalities have districts
            String districtString = getString(R.string.info_tab_country_specific_info, districtFieldName, district);
            editText.append(districtString);
            editText.append("\n");
        }

        // Municipality (Municipio in Spanish)
        // Galicia has instead Concellos (singular: Concello)

        // Municipality - Get field name
        String municipalityFieldName = "";
        if (autonomousCommunity.equals(getString(R.string.galicia))) // Galicia has concellos instead
            municipalityFieldName = getString(R.string.concello);
        else // Default
            municipalityFieldName = getString(R.string.municipio);

        // Municipality - Get field value
        String municipalityString = getString(R.string.info_tab_country_specific_info, municipalityFieldName, municipality);
        editText.append(municipalityString);
        editText.append("\n");

        // Comarca - Wikipedia page keeps original name: https://en.wikipedia.org/wiki/Comarca
        // Extremadura has instead mancomunidades integrales (singular: mancomunidad integral)

        // Comarca - Get field name
        String comarcaFieldName = "";
        if (comarcas.size() > 1) // Multiple comarcas - Does not occur in Extremadura, where mancomunidades integrales never overlap
            comarcaFieldName = getString(R.string.comarcas);
        else {
            if (autonomousCommunity.equals(getString(R.string.extremadura))) // Extremadura has instead mancomunidades integrales
                comarcaFieldName = getString(R.string.mancomunidad_integral);
            else // Default
                comarcaFieldName = getString(R.string.comarca);
        }

        // Comarca - Get field value
        String comarcaString = "";
        if (comarcas.size() == 0) { // Location is not inside a comarca
            if (autonomousCommunity.equals(getString(R.string.extremadura)))
                comarcaString = getString(R.string.no_mancomunidad_integral);
            else
                comarcaString = getString(R.string.no_comarca);
        } else if (comarcas.size() == 1) // Location is inside one comarca
            comarcaString = getString(R.string.info_tab_country_specific_info, comarcaFieldName, comarcas.get(0));
        else // Location is inside 2 comarcas. No more than 2 overlapping comarcas are expected
            comarcaString = getString(R.string.info_tab_country_specific_info_double, comarcaFieldName, comarcas.get(0), comarcas.get(1));
        editText.append(comarcaString);
        editText.append("\n");

        // Province (Provincia in Spanish)
        // Only applies to autonomous communities with multiple provinces
        if (!(Utils.isAutonomousCommunityWithSingleProvince(autonomousCommunity, province))) {
            String provinceFieldName = getString(R.string.province);
            String provinceString = getString(R.string.info_tab_country_specific_info, provinceFieldName, province);
            editText.append(provinceString);
            editText.append("\n");
        }

        // Autonomous Community (Comunidad Autónoma in Spanish)
        String autonomousCommunityFieldName = getString(R.string.autonomous_community);
        String autonomousCommunityString = getString(R.string.info_tab_country_specific_info, autonomousCommunityFieldName, autonomousCommunity);
        editText.append(autonomousCommunityString);
        editText.append("\n");

        // Country
        String countryLongName = getString(R.string.spain);
        setCountryInfo(editText, countryLongName);
    }

    /**
     * Adds the location info specific to Gibraltar to the Info Fragment, assuming the current location is in Gibraltar
     * @param editText The EditText widget that will contain the text to display
     * @param currentLocation The object representing the current location
     */
    private void setGibraltarLocationInfo(EditText editText, LocationInfo currentLocation) {
        // Major Residential Area
        String majorResidentialAreaFieldName = getString(R.string.major_residential_area);
        String majorResidentialArea = ((LocationInfoGibraltar) currentLocation).getMajorResidentialAreas().get(0);
        String majorResidentialAreaString = getString(R.string.info_tab_country_specific_info, majorResidentialAreaFieldName, majorResidentialArea);
        editText.append(majorResidentialAreaString);
        editText.append("\n");

        // Country
        String countryLongName = getString(R.string.gibraltar_long_name);
        setCountryInfo(editText, countryLongName);
    }

    /**
     * Adds the country name to the info fragment
     * @param editText The EditText widget that will contain the text to display
     * @param countryLongName The full name of the country to be displayed
     */
    private void setCountryInfo(EditText editText, String countryLongName) {
        String countryField = getString(R.string.country);
        String countryString = getString(R.string.info_tab_country_specific_info, countryField, countryLongName);
        editText.append(countryString);
    }
}
