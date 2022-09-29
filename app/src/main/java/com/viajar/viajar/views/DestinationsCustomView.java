package com.viajar.viajar.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viajar.viajar.LocationInfo;
import com.viajar.viajar.utils.RouteColorGetter;

import java.util.List;

/**
 * Contains the different destinations info in the GPS tab
 */
public class DestinationsCustomView extends LinearLayout {
    private final int textSizeUnit = TypedValue.COMPLEX_UNIT_SP;
    private final int routeNameTextSize = 20;

    public DestinationsCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
    }

    public void setView(LocationInfo currentLocation, String currentTransportMeans) {
        int order = 1;
        boolean first = true;
        for (int i = 0; i < currentLocation.getSurroundingLocations().keySet().size(); i++) {
            for (List<String> connectionID : currentLocation.getSurroundingLocations().keySet()) {
                String surroundingLocation = connectionID.get(0);
                String meansTransport = connectionID.get(1);
                if ((order == currentLocation.getSurroundingLocationOrder(surroundingLocation, meansTransport)) && (
                        currentTransportMeans.equals(meansTransport)) && (
                        currentLocation.hasDestinationsFromSurroundingLocation(surroundingLocation, currentTransportMeans))) {
                    addDestinationsInfo(currentLocation, currentTransportMeans, surroundingLocation, first);
                    first = false;
                }
            }
            order += 1;
        }
    }

    private void addDestinationsInfo(LocationInfo currentLocation, String currentTransportMeans, String surroundingLocation, boolean first) {
        String routeName = currentLocation.getRouteName(surroundingLocation, currentTransportMeans);

        // Separator

        if (!first) {
            this.addView(new TextView(getContext()));
        }

        // Surrounding location

        TextView surroundingLocationTextView = new TextView(getContext());
        this.addView(surroundingLocationTextView);
        surroundingLocationTextView.setText(surroundingLocation);

        // Route name

        LinearLayout routeNameLinearLayout = new LinearLayout(getContext()); // Will allow to set different color just under route name and not entire screen width
        this.addView(routeNameLinearLayout);
        routeNameLinearLayout.setGravity(Gravity.CENTER);

        TextView routeTextView = new TextView(getContext());
        routeNameLinearLayout.addView(routeTextView);
        routeTextView.setText(routeName);
        routeTextView.setTextSize(textSizeUnit, routeNameTextSize);
        routeTextView.setTypeface(routeTextView.getTypeface(), Typeface.BOLD);
        routeTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        // Destinations and text color

        LinearLayout destinationsLinearLayout = new LinearLayout(getContext());
        this.addView(destinationsLinearLayout);
        destinationsLinearLayout.setOrientation(VERTICAL);

        for (String destinationText : currentLocation.getDestinationsFromSurroundingLocation(surroundingLocation, currentTransportMeans)) {
            TextView destinationTextView = new TextView(getContext());
            destinationTextView.setText(destinationText);
            destinationsLinearLayout.addView(destinationTextView);
            destinationTextView.setTextColor(RouteColorGetter.getDestinationsTextColor(routeName, currentTransportMeans));
            routeTextView.setTextColor(RouteColorGetter.getRouteNameTextColor(routeName, currentTransportMeans));
        }

        // Background color
        int backgroundColor = RouteColorGetter.getRouteBackgroundColor(routeName, currentTransportMeans);
        routeNameLinearLayout.setBackgroundColor(backgroundColor);
        destinationsLinearLayout.setBackgroundColor(backgroundColor);
        // Route name background color may be different from general background color
        routeTextView.setBackgroundColor(RouteColorGetter.getRouteTextBackgroundColor(routeName, currentTransportMeans, backgroundColor));

        // Required - https://developer.android.com/training/custom-views/create-view#addprop

        invalidate();
        requestLayout();
    }

}
