package com.viajar.viajar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viajar.viajar.LocationInfo;
import com.viajar.viajar.R;
import com.viajar.viajar.utils.Utils;

/**
 * Represents a destination button along with the associated compass icon and distance text
 */
public class DestinationButtonView extends LinearLayout {

    private Button button;

    public DestinationButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the view with its different components
     * Button callback must be set by caller activity or fragment code, if callback is in that activity or fragment
     * @param currentLocation Current location of the journey
     * @param currentTransportMeans Current means of transport of the journey
     * @param surroundingLocation The surrounding location for this view instance
     */
    public void setView(LocationInfo currentLocation, String currentTransportMeans, LocationInfo surroundingLocation) {

        // Compass icon

        ImageView compassView = new ImageView(getContext());
        int angle = Utils.getAngleBetweenPoints(currentLocation.getCoordinatesAsObject(), surroundingLocation.getCoordinatesAsObject());
        compassView.setImageResource(R.drawable.compass_icon);
        compassView.setRotation(angle);

        // Distance text

        TextView distanceView = new TextView(getContext());
        double distance = currentLocation.getSurroundingLocationDistance(surroundingLocation.getName(), currentTransportMeans);
        distanceView.setText(String.valueOf(distance));

        // Destination button

        this.button = new Button(getContext());
        button.setText(surroundingLocation.getName());
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // Compass and distance view

        LinearLayout compassAndDistanceView = new LinearLayout(getContext());
        compassAndDistanceView.setOrientation(LinearLayout.VERTICAL);
        compassAndDistanceView.addView(compassView);
        compassAndDistanceView.addView(distanceView);

        // Assembling the destination button view

        this.addView(compassAndDistanceView);
        this.addView(button);
        this.setOrientation(HORIZONTAL);
    }

    /**
     * Allows to get layout destination button, for example to set callbacks
     * @return Destination button object
     */
    public Button getButton() {
        return this.button;
    }
}
