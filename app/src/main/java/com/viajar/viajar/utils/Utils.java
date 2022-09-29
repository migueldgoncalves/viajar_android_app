package com.viajar.viajar.utils;

public class Utils {

    private static final double earthRadius = 6367.4445; // Approximate average between earth radius in equator and in poles
    private static final double haversineMaxErrorRate = 0.005; // Max error rate when using Haversine formula (0.5%)

    /**
     * Calculates distance between 2 points using Haversine formula - See https://en.wikipedia.org/wiki/Haversine_formula
     * Max error rate is 0,5%
     * @param coordinateA Coordinates of the point A
     * @param coordinateB Coordinates of the point B
     * @return Distance in km
     */
    public static double getDistanceBetweenPoints(Coordinate coordinateA, Coordinate coordinateB) {
        double divisionA = (coordinateB.getLatitude() - coordinateA.getLatitude()) / 2;
        double divisionB = (coordinateB.getLongitude() - coordinateA.getLongitude()) / 2;
        double sqrsinA = Math.pow(Math.sin(Math.toRadians(divisionA)), 2);
        double sqrsinB = Math.pow(Math.sin(Math.toRadians(divisionB)), 2);
        double cosineA = Math.cos(Math.toRadians(coordinateA.getLatitude()));
        double cosineB = Math.cos(Math.toRadians(coordinateB.getLatitude()));
        double sqroot = Math.sqrt(sqrsinA + cosineA * cosineB * sqrsinB);

        return 2 * earthRadius * Math.asin(sqroot);
    }

    /**
     * To use in unit testing only
     * Returns true if obtained distance is close enough to the expected distance, false otherwise
     * @param expectedDistance The known distance between two points
     * @param obtainedDistance The distance between the same two points obtained with the Haversine formula
     * @return true if obtained distance is close enough to the expected distance, false otherwise
     */
    protected static boolean isDistanceValid(double expectedDistance, double obtainedDistance) {
        double lowestAcceptableValue = expectedDistance - (expectedDistance * haversineMaxErrorRate);
        double highestAcceptableValue = expectedDistance + (expectedDistance * haversineMaxErrorRate);

        return (obtainedDistance >= lowestAcceptableValue) && (obtainedDistance <= highestAcceptableValue);
    }

    /**
     * Returns the angle, in degrees, of the straight line between point A and point B from the point of view of point A
     * Ex: If point A is (39.0, -4.0) and point B is (40.0, -4.0), angle is 0º
     * @param startingPoint The starting point. Angle will be measured here, comparing with a north-south line
     * @param destination The destination point.
     * @return The angle, in degrees, of the straight line between point A and point B from the point of view of point A
     */
    public static int getAngleBetweenPoints(Coordinate startingPoint, Coordinate destination) {
        double adjacent = Utils.getDistanceBetweenPoints(startingPoint, new Coordinate(destination.getLatitude(), startingPoint.getLongitude())); // North-south
        double opposite = Utils.getDistanceBetweenPoints(startingPoint, new Coordinate(startingPoint.getLatitude(), destination.getLongitude())); // East-west

        int angle = (int) Math.toDegrees(Math.atan(opposite / adjacent));

        // Same location
        if (startingPoint == destination)
            return 0; // Unable to calculate

        // Special angles
        if ((destination.getLatitude() > startingPoint.getLatitude()) && (destination.getLongitude() == startingPoint.getLongitude()))
            return 0;
        else if ((destination.getLatitude() == startingPoint.getLatitude()) && (destination.getLongitude() > startingPoint.getLongitude()))
            return 90;
        else if ((destination.getLatitude() < startingPoint.getLatitude()) && (destination.getLongitude() == startingPoint.getLongitude()))
            return 180;
        else if ((destination.getLatitude() == startingPoint.getLatitude()) && (destination.getLongitude() < startingPoint.getLongitude()))
            return 270;

        // Remaining angles
        if ((destination.getLatitude() > startingPoint.getLatitude()) && (destination.getLongitude() > startingPoint.getLongitude())) { // 0º > 90º
            // Nothing to do
        } else if ((destination.getLatitude() < startingPoint.getLatitude()) && (destination.getLongitude() > startingPoint.getLongitude())) { // 90º > 180º
            angle = (90 - angle) + 90;
        } else if ((destination.getLatitude() < startingPoint.getLatitude()) && (destination.getLongitude() < startingPoint.getLongitude())) { // 180º > 270º
            angle += 180;
        } else if ((destination.getLatitude() > startingPoint.getLatitude()) && (destination.getLongitude() < startingPoint.getLongitude())) { // 270º > 360º
            angle = (180 - angle) + 180;
        }

        return angle;
    }
}
