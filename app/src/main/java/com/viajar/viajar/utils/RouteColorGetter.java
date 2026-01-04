package com.viajar.viajar.utils;

import android.graphics.Color;

import com.viajar.viajar.TravelActivity;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final int hikingConnectionColor = Color.parseColor("#800000"); // Dark brown
    private static final int highSpeedRailwayColor = Color.parseColor("#660066"); // Purple
    private static final int waterwayRiverColor = Color.CYAN;
    private static final int waterwayCoastColor = Color.parseColor("#007fff"); // Blue
    private static final int waterwayOceanColor = Color.parseColor("#0000e6"); // Dark blue
    private static final int planeConnectionColor = Color.RED;

    private static final int redRouteHighlight = Color.RED; // Ex: Portuguese Itinerários Principais
    private static final int greenRouteHighlight = Color.parseColor("#009900"); // Ex: Some autovías in Andaluzia
    private static final int orangeRouteHighlight = Color.parseColor("#ff9900"); // Ex: Spanish autovías M-45 and A-92

    private static final String[] specialHighwayNames = new String[]{
            "A20/IC23 VCI/Ponte do Freixo", "A20/IC23 VCI/Freixo Bridge", "VRI",// Portugal
            "A-376", "A-381", "A-483", "A-497", "A-8009", "A-8057", "A-8058",   // Andalucía
            "AG-31", "AG-42", "AG-51", "AG-53", "AG-54",                        // Galícia
            "AS-I", "AS-II", "AS-17",                                           // Asturias
            "AV-20",                                                            // Ávila
            "B-23",                                                             // Barcelona
            "CA-34", "CA-35",                                                   // Cádiz
            "CM-41",                                                            // Castilla-La Mancha
            "CO-32",                                                            // Córdoba
            "CV-80",                                                            // Comunitat Valenciana
            "EX-A1", "EX-A2",                                                   // Extremadura
            "GR-30", "GR-43",                                                   // Granada
            "H-30", "H-31",                                                     // Huelva
            "M-11", "M-12", "M-13/M-14", "M-13", "M-14", "M-23", "M-30",        // Comunidad de Madrid
            "M-30 - Avenida de la Ilustración", "M-30 - Bypass Sul", "M-31",
            "M-40", "M-45", "M-45/M-50", "M-50", "M-50/R-2", "M-607",
            "MA-20", "MA-23",                                                   // Málaga
            "O-11", "O-12", "O-14",                                             // Oviedo
            "PT-10",                                                            // Puertollano
            "RM-2", "RM-16", "RM-17",                                           // Región de Murcia
            "SE-20", "SE-30", "SE-40",                                          // Seville
            "V-11", "V-31", "V-31 - Avinguda d'Ausiàs March",                   // Valencia
    };

    private static final String[] autoviasWithOrangeBackground = new String[]{ // Specific to Spain
            "A-92", "A-92G", "A-92M", "A-92N", "A-316", "A-318", "A-381", "A-382",
            "CV-80",
            "M-45",
            "RM-17"
    };

    private static final String[] autoviasWithGreenBackground = new String[] { // Specific to Spain
            "A-376", "A-483", "A-497", "A-8009", "A-8057", "A-8058",
    };

    // General

    public static int getDestinationsTextColor(String routeName, String transportMeans) {
        if (isHighway(routeName) ||
                isItinerarioPrincipal(routeName) ||
                isWaterway(transportMeans) ||
                isRailway(transportMeans) ||
                isHikingConnection(transportMeans)) {
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
                isCarreteraDelEstado(routeName) ||
                isHikingConnection(transportMeans) ||
                isCarreteraGeneral(routeName)) {
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
        } else if (RouteColorGetter.isHikingConnection(transportMeans)) {
            return hikingConnectionColor;
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
        if (isCarreteraDelEstado(routeName) || isItinerarioPrincipal(routeName) || isCarreteraGeneral(routeName))
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

        if ((routeName == null) || (routeName.isEmpty()))
            return false;

        // Generic use case - Valid for most scenarios in countries such as Portugal and France
        Pattern genericPattern = Pattern.compile("^A\\d+"); // A1, A9, A99, A999, A999, etc.
        Matcher genericMatcher = genericPattern.matcher(routeName);

        if (genericMatcher.find()) {
            return true;
        }

        // Handles most Spanish autopistas
        if ((routeName.startsWith("AP-")) || routeName.startsWith("R-")) {
            return true;
        }

        // Handles many Spanish autovías
        Pattern autoviaPattern = Pattern.compile("^A-\\d{1,2}"); // A-1, A-9, A-10, A-99
        Matcher autoviaMatcher = autoviaPattern.matcher(routeName);
        Pattern nonAutoviaPattern = Pattern.compile("^A-\\d{3,}"); // A-100, A-999, etc. By default, not an autovía
        Matcher nonAutoviaMatcher = nonAutoviaPattern.matcher(routeName);

        if (autoviaMatcher.find() && !nonAutoviaMatcher.find()) {
            return true;
        }

        // Handles special names
        return (Arrays.asList(specialHighwayNames).contains(routeName));
    }

    public static boolean isAutoviaWithOrangeBackground(String routeName) {
        // Specific to Spain
        return (Arrays.asList(autoviasWithOrangeBackground).contains(routeName));
    }

    public static boolean isAutoviaWithGreenBackground(String routeName) {
        // Specific to Spain
        return (Arrays.asList(autoviasWithGreenBackground).contains(routeName));
    }

    // Other roads

    public static boolean isItinerarioPrincipal(String routeName) {
        // Specific to Portugal
        if ((routeName == null) || (routeName.isEmpty()))
            return false;
        return (routeName.startsWith("IP"));
    }

    public static boolean isItinerarioComplementar(String routeName) {
        // Specific to Portugal
        if ((routeName == null) || (routeName.isEmpty()))
            return false;
        else if (routeName.equals("Eixo Sul"))
            return true;
        return (routeName.startsWith("IC"));
    }

    // Specific to Spain
    public static boolean isCarreteraDelEstado(String routeName) {
        if ((routeName == null) || (routeName.isEmpty()))
            return false;

        return (
                routeName.startsWith("N-") || // Standard Spanish Carretera del Estado

                        // Other Spanish roads whose traffic signs have white background and red highlight of the route name
                        routeName.startsWith("SA-") // Salamanca
        );
    }

    // Specific to Andorra
    public static boolean isCarreteraGeneral(String routeName) {
        if ((routeName == null) || (routeName.isEmpty()))
            return false;
        String[] roadNames = new String[]{
                "CG-1", "CG-2", "CG-3", "CG-4", "CG-5", "CG-6"
        };
        return (Arrays.asList(roadNames).contains(routeName));
    }

    // Railways

    public static int getColorByStandardRailway(String railway) {
        // Given the name of a railway (ex: Sul Line, Sado Line), returns the color to use to represent the line.
        //  Ex: Sado Line is blue, while Cascais Line is yellow

        // TRAIN - Add new train lines HERE

        // Lisbon suburban railways
        if (railway.contains("Sado"))
            return Color.parseColor("#0000ff");
        else if (railway.contains("Fertagus"))
            return Color.parseColor("#6fa8dc");
        else if (railway.contains("Sintra"))
            return Color.parseColor("#008000");
        else if (railway.contains("Azambuja"))
            return Color.parseColor("#be2c2c");
        else if (railway.contains("Cascais"))
            return Color.parseColor("#ffab2e");

        // Coimbra suburban railways
        else if (railway.contains("Coimbra"))
            return Color.parseColor("#3c3c3c");

        // Porto suburban railways
        else if (railway.contains("Aveiro Line")) // To distinguish from the Aveiro Branch Line
            return Color.parseColor("#ffa700");
        else if (railway.contains("Marco de Canaveses"))
            return Color.parseColor("#0083d7");
        else if (railway.contains("Guimarães"))
            return Color.parseColor("#e62621");
        else if (railway.contains("Braga"))
            return Color.parseColor("#009c5a");
        else if (railway.contains("Leixões"))
            return Color.parseColor("#a887a6");

        // Madrid suburban railways
        else if (railway.contains("Cercanías Madrid")) {
            if (railway.contains("C-1") && !railway.contains("C-10"))
                return Color.parseColor("#66aede");
            else if (railway.contains("C-2"))
                return Color.parseColor("#008A29");
            else if (railway.contains("C-3"))
                return Color.parseColor("#6a329f");
            else if (railway.contains("C-4"))
                return Color.parseColor("#00289C");
            else if (railway.contains("C-5"))
                return Color.parseColor("#FAB700");
            // Line C-6 of Madrid suburban railways was absorbed by line C-5
            else if (railway.contains("C-7"))
                return Color.parseColor("#DE0118");
            else if (railway.contains("C-8"))
                return Color.parseColor("#a0a0a0");
            else if (railway.contains("C-9"))
                return Color.parseColor("#926037");
            else if (railway.contains("C-10"))
                return Color.parseColor("#8FBE00");
        }

        // Seville suburban railways
        else if (railway.contains("Cercanías Sevilla")) {
            if (railway.contains("C-1"))
                return Color.parseColor("#69B3E7");
            if (railway.contains("C-2"))
                return Color.parseColor("#009739");
            if (railway.contains("C-3"))
                return Color.parseColor("#EF3340");
            if (railway.contains("C-4"))
                return Color.parseColor("#BB29BB");
            if (railway.contains("C-5"))
                return Color.parseColor("#0033a0");
        }

        // SUBWAY - Add new subway lines HERE

        // Lisbon Metro
        else if (railway.contains("Lisbon Metro")) {
            if (railway.contains("Red"))
                return Color.parseColor("#DF096F");
            else if (railway.contains("Green"))
                return Color.parseColor("#00AA40");
            else if (railway.contains("Blue"))
                return Color.parseColor("#4E84C4");
            else if (railway.contains("Yellow"))
                return Color.parseColor("#F4BC18");
        }

        // Porto Metro
        else if (railway.contains("Porto Metro")) {
            if (railway.contains("A"))
                return Color.parseColor("#3caeef");
            else if (railway.contains("B"))
                return Color.parseColor("#e62621");
            else if (railway.contains("C"))
                return Color.parseColor("#8bc63e");
            else if (railway.contains("D"))
                return Color.parseColor("#f9c212");
            else if (railway.contains("E"))
                return Color.parseColor("#937bb8");
            else if (railway.contains("F"))
                return Color.parseColor("#f68B1f");
        }

        // Metro Sul do Tejo (== South Tagus)
        else if (railway.contains("Metro Sul do Tejo")) {
            // Has Y-shape and 3 lines, most stations are served by 2 lines
            if (railway.contains("1") && railway.contains("3")) // Display color of line 1
                return Color.parseColor("#218FCE");
            else if (railway.contains("1") && railway.contains("2")) // Display color of line 2
                return Color.parseColor("#F7941C");
            else // Display color of line 3
                return Color.parseColor("#A2A730");
        }

        // Madrid Metro
        if (railway.contains("Madrid Metro")) {
            if (railway.contains("1") && !railway.contains("10") && !railway.contains("11") && !railway.contains("12"))
                return Color.parseColor("#39b5e6");
            else if (railway.contains("2"))
                return Color.parseColor("#fb0f0c");
            else if (railway.contains("3"))
                return Color.parseColor("#FFDF00");
            else if (railway.contains("4"))
                return Color.parseColor("#824100");
            else if (railway.contains("5"))
                return Color.parseColor("#96bf0d");
            else if (railway.contains("6"))
                return Color.parseColor("#999999");
            else if (railway.contains("7"))
                return Color.parseColor("#ff8501");
            else if (railway.contains("8"))
                return Color.parseColor("#f373b7");
            else if (railway.contains("9"))
                return Color.parseColor("#9F1F99");
            else if (railway.contains("10"))
                return Color.parseColor("#003da6");
            else if (railway.contains("11"))
                return Color.parseColor("#00953b");
            else if (railway.contains("12"))
                return Color.parseColor("#a19200");
            else if (railway.contains("R"))
                return Color.parseColor("#ffffff");
            else if (railway.contains("ML1"))
                return Color.parseColor("#287ee2");
            else if (railway.contains("ML2"))
                return Color.parseColor("#aa148e");
            else if (railway.contains("ML3"))
                return Color.parseColor("#ff4336");
            else if (railway.contains("ML4"))
                return Color.parseColor("#77ba26");
        }

        // Valencia Metro (known as Metrovalencia)
        else if (railway.contains("Metrovalencia")) {
            if (railway.contains("1") && !railway.contains("10"))
                return Color.parseColor("#E6B036");
            else if (railway.contains("2"))
                return Color.parseColor("#D23983");
            else if (railway.contains("3"))
                return Color.parseColor("#C21E2D");
            else if (railway.contains("4"))
                return Color.parseColor("#0F4583");
            else if (railway.contains("5"))
                return Color.parseColor("#008358");
            else if (railway.contains("6"))
                return Color.parseColor("#80629F");
            else if (railway.contains("7"))
                return Color.parseColor("#DB8319");
            else if (railway.contains("8"))
                return Color.parseColor("#41B1CB");
            else if (railway.contains("9"))
                return Color.parseColor("#AC7D4E");
            else if (railway.contains("10"))
                return Color.parseColor("#B3CB6D");
        }

        // Seville Metro
        else if (railway.contains("Seville Metro")) {
            if (railway.contains("1")) {
                return Color.parseColor("#01820b");
            }
        }

        // Default - Likely intercity railways without assigned colors
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
        else if (isOceanWaterway(meansTransport, routeName))
            return waterwayOceanColor;
        else // Default
            return waterwayRiverColor;
    }

    public static boolean isWaterway(String meansTransport) {
        return (meansTransport.equals(TravelActivity.BOAT) || meansTransport.equals(TravelActivity.SHIP));
    }

    public static boolean isRiverWaterway(String meansTransport, String routeName) {
        return (isWaterway(meansTransport) && !routeName.contains("Coast") && meansTransport.equals(TravelActivity.BOAT));
    }

    public static boolean isCoastWaterway(String meansTransport, String routeName) {
        return (isWaterway(meansTransport) && routeName.contains("Coast"));
    }

    public static boolean isOceanWaterway(String meansTransport, String routeName) {
        return (isWaterway(meansTransport) && !routeName.contains("Coast") && meansTransport.equals(TravelActivity.SHIP));
    }

    // Other means of transport

    public static boolean isPlaneConnection(String meansTransport) {
        return meansTransport.equals(TravelActivity.PLANE);
    }

    public static boolean isHikingConnection(String meansTransport) {
        return meansTransport.equals(TravelActivity.HIKING);
    }
}
