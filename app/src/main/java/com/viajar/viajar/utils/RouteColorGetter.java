package com.viajar.viajar.utils;

import android.graphics.Color;

import com.viajar.viajar.TravelActivity;

import java.util.Arrays;

/**
 * Returns the color associated with connection lines drawn on maps and destination views backgrounds
 */
public class RouteColorGetter {

    private static final int defaultLineColor = Color.BLACK;
    private static final int defaultBackgroundColor = Color.parseColor("#F0F0F0"); // Light gray

    private static final int autoEstradaColor = Color.BLUE;
    private static final int itinerarioPrincipalColor = Color.parseColor("#008000"); // Dark green
    private static final int itinerarioComplementarColor = Color.parseColor("#808080"); // Grey
    private static final int railwayColor = Color.parseColor("#800000"); // Dark brown
    private static final int highSpeedRailwayColor = Color.parseColor("#660066"); // Purple
    private static final int waterwayRiverColor = Color.CYAN;
    private static final int waterwayCoastColor = Color.parseColor("#007fff"); // Blue
    private static final int planeConnectionColor = Color.RED;

    private static final int redRouteHighlight = Color.RED; // Ex: Portuguese Itinerários Principais
    private static final int greenRouteHighlight = Color.parseColor("#009900"); // Ex: Some autovías in Andaluzia
    private static final int orangeRouteHighlight = Color.parseColor("#ff9900"); // Ex: Spanish autovías M-45 and A-92

    // General

    public static int getDestinationsTextColor(String routeName, String transportMeans) {
        if (isHighway(routeName) ||
                isItinerarioPrincipal(routeName) ||
                isWaterway(transportMeans) ||
                isRailway(transportMeans)) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public static int getRouteNameTextColor(String routeName, String transportMeans) {
        if (isAutoviaWithOrangeBackground(routeName)) {
            return Color.BLACK;
        } else if (isHighway(routeName) ||
                isItinerarioPrincipal(routeName) ||
                isWaterway(transportMeans) ||
                isRailway(transportMeans) ||
                isCarreteraDelEstado(routeName)) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public static int getRouteLineColor(String routeName, String transportMeans) {
        if (RouteColorGetter.isHighway(routeName)) {
            return autoEstradaColor;
        } else if (RouteColorGetter.isItinerarioPrincipal(routeName)) {
            return itinerarioPrincipalColor;
        } else if (RouteColorGetter.isItinerarioComplementar(routeName)) {
            return itinerarioComplementarColor;
        } else if (RouteColorGetter.isWaterway(transportMeans)) {
            return RouteColorGetter.getColorByWaterway(transportMeans, routeName);
        } else if (RouteColorGetter.isStandardRailway(transportMeans)) {
            return RouteColorGetter.getColorByStandardRailway(routeName);
        } else if (RouteColorGetter.isHighSpeedRailway(transportMeans)) {
            return highSpeedRailwayColor;
        } else if (RouteColorGetter.isPlaneConnection(transportMeans)) {
            return planeConnectionColor;
        } else { // Ex: Portuguese Estradas Nacionais and Spanish Carreteras del Estado
            return defaultLineColor;
        }
    }

    public static int getRouteBackgroundColor(String routeName, String transportMeans) {
        int color = getRouteLineColor(routeName, transportMeans);
        if (color == defaultLineColor)
            color = defaultBackgroundColor; // Only difference to line colors is default color
        return color;
    }

    public static int getRouteTextBackgroundColor(String routeName, String transportMeans, int routeBackgroundColor) {
        if (isCarreteraDelEstado(routeName) || isItinerarioPrincipal(routeName))
            return redRouteHighlight;
        else if (RouteColorGetter.isAutoviaWithOrangeBackground(routeName))
            return orangeRouteHighlight;
        else if (RouteColorGetter.isAutoviaWithGreenBackground(routeName))
            return greenRouteHighlight;
        else
            return routeBackgroundColor; // Default - Text background has same color as view background
    }

    // Highways

    public static boolean isHighway(String routeName) {
        // Portugal - auto-estrada (Ex: A1)
        // Spain - Either autovía (Ex: A-1) or autopista (Ex: AP-1)
        if ((routeName == null) || (routeName.length() == 0))
            return false;

        return (
                // General highways
                routeName.startsWith("A-") || // State autovía (also autovía from Andalucía)
                        routeName.startsWith("AP-") || // State autopista
                        routeName.startsWith("R-") || // Radial
                        (routeName.charAt(0) == 'A' && ((routeName.length() == 2) || (routeName.length() == 3))) || // Ex: A2, A22
                        routeName.equals("A9 CREL") ||
                        routeName.equals("A13-1") ||
                        routeName.equals("A26-1") ||
                        routeName.equals("A10 - Ponte da Lezíria") ||
                        routeName.equals("A12 - Ponte Vasco da Gama") ||
                        routeName.contains("IC23 VCI") || // Ex: A20/IC23 VCI/Ponte do Freixo

                        // Spanish autonomous community autovías
                        routeName.startsWith("CM-") || // Castilla-La Mancha
                        routeName.startsWith("EX-A") || // Extremadura
                        routeName.startsWith("M-") || // Comunidad de Madrid
                        routeName.startsWith("AG-") || // Galicia

                        // Spanish provincial autovías
                        routeName.startsWith("CA-") || // Cádiz
                        routeName.startsWith("CO-") || // Córdoba
                        routeName.startsWith("GR-") || // Granada
                        routeName.startsWith("H-") || // Huelva
                        routeName.startsWith("MA-") || // Málaga
                        routeName.startsWith("SE-") || // Seville
                        routeName.startsWith("TO-") || // Toledo
                        routeName.startsWith("AV-") // Ávila
        );
    }

    public static boolean isAutoviaWithOrangeBackground(String routeName) {
        // Specific to Spain
        // Only specific autovías have orange background - Call before isAutoviaWithGreenBackground
        String[] autovias = new String[]{
                "A-92", "A-92G", "A-92M", "A-92N", "A-316", "A-318", "A-381", "A-382", "M-45"
        };
        return (Arrays.asList(autovias).contains(routeName));
    }

    public static boolean isAutoviaWithGreenBackground(String routeName) {
        // Specific to Spain
        // Ex: some autovías in Andalucía, such as A-483
        // Must be called after isAutoviaWithOrangeBackground - This routine is more general
        if (routeName == null)
            return false;

        if (routeName.startsWith("A-")) {
            if ((routeName.contains(" ")) && (routeName.split(" ")[0].split("A-")[1].length() >= 3)) // Ex: A-7 - Ronda Oeste de Málaga
                return true;
            else if ((routeName.contains("/")) && (routeName.split("/")[0].split("A-")[1].length() >= 3)) // Ex: A-7/AP-7
                return true;
            else
                return (routeName.split("A-")[1].length() >= 3) &&
                        !(routeName.contains(" ")) &&
                        !(routeName.contains("/")); // Ex: A-483
        } else {
            return false;
        }
    }

    // Other roads

    public static boolean isItinerarioPrincipal(String routeName) {
        // Specific to Portugal
        if ((routeName == null) || (routeName.length() == 0))
            return false;
        return (routeName.startsWith("IP"));
    }

    public static boolean isItinerarioComplementar(String routeName) {
        // Specific to Portugal
        if ((routeName == null) || (routeName.length() == 0))
            return false;
        return (routeName.startsWith("IC"));
    }

    // Specific to Spain
    public static boolean isCarreteraDelEstado(String routeName) {
        if ((routeName == null) || (routeName.length() == 0))
            return false;

        return (
                routeName.startsWith("N-") || // Standard Spanish Carretera del Estado

                        // Other Spanish roads whose traffic signs have white background and red highlight of the route name
                        routeName.startsWith("SA-") // Salamanca
        );
    }

    // Railways

    public static int getColorByStandardRailway(String railway) {
        // Given the name of a railway (ex: Linha do Sul - Intercidades, Linha do Sado - CP Lisboa), returns
        //  the color to use to represent the line. Ex: Sado Line is blue, while Cascais Line is yellow

        // TRAIN - Add new train lines HERE

        // Lisbon
        if (railway.contains("Linha do Sado"))
            return Color.BLUE;
        else if (railway.contains("Linha do Sul") && railway.contains("Fertagus"))
            return Color.parseColor("#6fa8dc"); // Light blue
        else if (railway.contains("Linha de Sintra") && railway.contains("CP Lisboa"))
            return Color.parseColor("#008000"); // Green
        else if (railway.contains("Linha da Azambuja"))
            return Color.parseColor("#be2c2c"); // Reddish-brown
        else if (railway.contains("Linha de Cascais"))
            return Color.parseColor("#ffab2e"); // Yellow
        // Coimbra
        else if (railway.contains("Urbanos de Coimbra"))
            return Color.parseColor("#3c3c3c"); // Dark gray
        // Porto
        else if (railway.contains("Linha de Aveiro"))
            return Color.parseColor("#ffa700"); // Yellow
        else if (railway.contains("Linha do Marco de Canaveses"))
            return Color.parseColor("#0083d7"); // Blue
        else if (railway.contains("Linha de Guimarães") && !railway.contains("Intercidades")) // Only suburban stretches should be receive this color
            return Color.parseColor("#e62621"); // Red
        else if (railway.contains("Linha de Braga"))
            return Color.parseColor("#009c5a"); // Green
        // Madrid
        else if (railway.contains("C-1"))
            return Color.parseColor("#66aede"); // Blue
        else if (railway.contains("C-3"))
            return Color.parseColor("#6a329f"); // Purple

        // SUBWAY - Add new subway lines HERE

        // Lisbon
        else if (railway.contains("Linha Vermelha - Metro de Lisboa"))
            return Color.RED;
        // Madrid
        else if (railway.contains("Linha 1 - Metro de Madrid"))
            return Color.parseColor("#39b5e6"); // Light blue
        else if (railway.contains("Linha 8 - Metro de Madrid"))
            return Color.parseColor("#f373b7"); // Pink

        // Default - Likely intercity railways without assigned colors
        else
            return railwayColor;
    }

    public static boolean isRailway(String meansTransport) {
        // General routine
        return isStandardRailway(meansTransport) || isHighSpeedRailway(meansTransport);
    }

    public static boolean isStandardRailway(String meansTransport) {
        // Excludes high speed railways
        return meansTransport.equals(TravelActivity.TRAIN) ||
                meansTransport.equals(TravelActivity.SUBWAY);
    }

    public static boolean isHighSpeedRailway(String meansTransport) {
        return meansTransport.equals(TravelActivity.HIGH_SPEED_TRAIN);
    }

    // Waterways

    public static int getColorByWaterway(String meansTransport, String routeName) {
        if (isRiverWaterway(meansTransport, routeName))
            return waterwayRiverColor;
        else if (isCoastWaterway(meansTransport, routeName))
            return waterwayCoastColor;
        else // Default
            return waterwayRiverColor;
    }

    public static boolean isWaterway(String meansTransport) {
        return (meansTransport.equals(TravelActivity.BOAT));
    }

    public static boolean isRiverWaterway(String meansTransport, String routeName) {
        return (isWaterway(meansTransport) && (!routeName.contains("Costa"))); // Costa == Coast
    }

    public static boolean isCoastWaterway(String meansTransport, String routeName) {
        return (isWaterway(meansTransport) && (routeName.contains("Costa"))); // Costa == Coast
    }

    // Other means of transport

    public static boolean isPlaneConnection(String meansTransport) {
        return meansTransport.equals(TravelActivity.PLANE);
    }
}
