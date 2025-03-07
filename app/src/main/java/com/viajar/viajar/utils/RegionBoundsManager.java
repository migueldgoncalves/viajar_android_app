package com.viajar.viajar.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.viajar.viajar.LocationInfo;
import com.viajar.viajar.LocationInfoPortugal;
import com.viajar.viajar.LocationInfoSpain;
import com.viajar.viajar.LocationInfoAndorra;
import com.viajar.viajar.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionBoundsManager {

    Context context;

    // ADD SUBREGION DEFINITIONS HERE

    // Algarve
    private final SubregionBounds sotaventoAlgarvio = new SubregionBounds("Sotavento Algarvio");
    private final SubregionBounds barlaventoAlgarvio = new SubregionBounds("Barlavento Algarvio");
    // Alentejo
    private final SubregionBounds beja = new SubregionBounds("Beja");
    private final SubregionBounds alentejoLitoral = new SubregionBounds("Alentejo Litoral");
    private final SubregionBounds evora = new SubregionBounds("Évora");
    private final SubregionBounds portalegre = new SubregionBounds("Portalegre");
    // Lisbon and Tagus Valley
    private final SubregionBounds peninsulaSetubal = new SubregionBounds("Península de Setúbal");
    private final SubregionBounds lisboa = new SubregionBounds("Lisboa");
    private final SubregionBounds leiria = new SubregionBounds("Leiria");
    private final SubregionBounds santarem = new SubregionBounds("Santarém");
    // Beiras
    private final SubregionBounds coimbra = new SubregionBounds("Coimbra");
    private final SubregionBounds aveiro = new SubregionBounds("Aveiro");
    private final SubregionBounds viseu = new SubregionBounds("Viseu");
    private final SubregionBounds casteloBranco = new SubregionBounds("Castelo Branco");
    private final SubregionBounds guarda = new SubregionBounds("Guarda");
    // North
    private final SubregionBounds porto = new SubregionBounds("Porto");
    private final SubregionBounds braga = new SubregionBounds("Braga");
    private final SubregionBounds vianaDoCastelo = new SubregionBounds("Viana do Castelo");
    private final SubregionBounds vilaReal = new SubregionBounds("Vila Real");
    private final SubregionBounds braganca = new SubregionBounds("Bragança");
    // Azores - Eastern Group
    private final SubregionBounds islandSaoMiguel = new SubregionBounds("Ilha de São Miguel");
    private final SubregionBounds islandSantaMariaAndFormigas = new SubregionBounds("Ilha de Santa Maria e Ilhéus das Formigas");

    // Andalucía
    private final SubregionBounds huelva = new SubregionBounds("Huelva");
    private final SubregionBounds sevilha = new SubregionBounds("Sevilha");
    private final SubregionBounds cadiz = new SubregionBounds("Cádiz");
    private final SubregionBounds cordoba = new SubregionBounds("Córdoba");
    private final SubregionBounds malaga = new SubregionBounds("Málaga");
    private final SubregionBounds jaen = new SubregionBounds("Jaén");
    private final SubregionBounds granada = new SubregionBounds("Granada");
    private final SubregionBounds almeria = new SubregionBounds("Almería");
    // Extremadura
    private final SubregionBounds badajoz = new SubregionBounds("Badajoz");
    private final SubregionBounds caceres = new SubregionBounds("Cáceres");
    // Castilla-La Mancha, Madrid Community and Murcia Region
    private final SubregionBounds ciudadReal = new SubregionBounds("Ciudad Real");
    private final SubregionBounds toledo = new SubregionBounds("Toledo");
    private final SubregionBounds albacete = new SubregionBounds("Albacete");
    private final SubregionBounds cuenca = new SubregionBounds("Cuenca");
    private final SubregionBounds guadalajara = new SubregionBounds("Guadalajara");
    private final SubregionBounds comunidadeDeMadrid = new SubregionBounds("Comunidade de Madrid");
    private final SubregionBounds regiaoDeMurcia = new SubregionBounds("Região de Murcia");
    // Castilla y León, Asturias and Cantabria
    private final SubregionBounds leon = new SubregionBounds("León");
    private final SubregionBounds zamora = new SubregionBounds("Zamora");
    private final SubregionBounds salamanca = new SubregionBounds("Salamanca");
    private final SubregionBounds segovia = new SubregionBounds("Segóvia");
    private final SubregionBounds palencia = new SubregionBounds("Palencia");
    private final SubregionBounds valladolid = new SubregionBounds("Valladolid");
    private final SubregionBounds avila = new SubregionBounds("Ávila");
    private final SubregionBounds burgos = new SubregionBounds("Burgos");
    private final SubregionBounds soria = new SubregionBounds("Soria");
    private final SubregionBounds asturias = new SubregionBounds("Asturias");
    private final SubregionBounds cantabria = new SubregionBounds("Cantabria");
    // Galicia
    private final SubregionBounds pontevedra = new SubregionBounds("Pontevedra");
    private final SubregionBounds aCoruna = new SubregionBounds("A Coruña");
    private final SubregionBounds ourense = new SubregionBounds("Ourense");
    private final SubregionBounds lugo = new SubregionBounds("Lugo");
    // Basque Country, La Rioja and Navarra
    private final SubregionBounds vizcaya = new SubregionBounds("Vizcaya");
    private final SubregionBounds guipuzcoa = new SubregionBounds("Guipúzcoa");
    private final SubregionBounds alava = new SubregionBounds("Álava");
    private final SubregionBounds laRioja = new SubregionBounds("La Rioja");
    private final SubregionBounds navarra = new SubregionBounds("Navarra");
    // Aragón
    private final SubregionBounds huesca = new SubregionBounds("Huesca");
    private final SubregionBounds zaragoza = new SubregionBounds("Zaragoza");
    private final SubregionBounds teruel = new SubregionBounds("Teruel");
    // Catalonia
    private final SubregionBounds lleida = new SubregionBounds("Lleida");
    private final SubregionBounds tarragona = new SubregionBounds("Tarragona");
    private final SubregionBounds girona = new SubregionBounds("Girona");
    private final SubregionBounds barcelona = new SubregionBounds("Barcelona");
    // Valencian Community
    private final SubregionBounds castellonDeLaPlana = new SubregionBounds("Castellón de la Plana");
    private final SubregionBounds valencia = new SubregionBounds("Valencia");
    private final SubregionBounds alicante = new SubregionBounds("Alicante");

    private final SubregionBounds gibraltar = new SubregionBounds("Gibraltar");

    private final SubregionBounds santJuliaDeLoria = new SubregionBounds("Sant Julià de Lòria");
    private final SubregionBounds andorraLaVella = new SubregionBounds("Andorra la Vella");
    private final SubregionBounds canillo = new SubregionBounds("Canillo");
    private final SubregionBounds encamp = new SubregionBounds("Encamp");
    private final SubregionBounds ordino = new SubregionBounds("Ordino");
    private final SubregionBounds laMassana = new SubregionBounds("La Massana");
    private final SubregionBounds escaldesEngordany	 = new SubregionBounds("Escaldes-Engordany");

    private final SubregionBounds defaultSubregion = new SubregionBounds("Default");

    // ADD REGION DEFINITIONS HERE

    private final RegionBounds algarve = new RegionBounds("Algarve");
    private final RegionBounds alentejo = new RegionBounds("Alentejo");
    private final RegionBounds lisboaValeTejo = new RegionBounds("Lisboa e Vale do Tejo"); // Approx. Lisboa e Vale do Tejo
    private final RegionBounds beiraLitoral = new RegionBounds("Beira Litoral"); // Approx. Beira Litoral
    private final RegionBounds beiraInterior = new RegionBounds("Beira Interior"); // Approx. Beira Interior
    private final RegionBounds entreDouroMinho = new RegionBounds("Entre-Douro-e-Minho"); // Approx Entre-Douro-e-Minho
    private final RegionBounds trasOsMontes = new RegionBounds("Trás-os-Montes");
    private final RegionBounds azoresEasternGroup = new RegionBounds("Azores - Eastern Group");

    private final RegionBounds westernAndaluciaAndGibraltar = new RegionBounds("Western Andalucía + Gibraltar");
    private final RegionBounds centralAndalucia = new RegionBounds("Central Andalucía");
    private final RegionBounds easternAndalucia = new RegionBounds("Eastern Andalucía");
    private final RegionBounds extremadura = new RegionBounds("Extremadura");
    private final RegionBounds westernCastillaLaManchaAndMadrid = new RegionBounds("Western Castilla-La Mancha + Comunidade de Madrid");
    private final RegionBounds easternCastillaLaManchaAndMurcia = new RegionBounds("Eastern Castilla-La Mancha + Região de Murcia");
    private final RegionBounds westernCastillaYLeonAndAsturias = new RegionBounds("Western Castilla y León + Asturias");
    private final RegionBounds centralCastillaYLeon = new RegionBounds("Central Castilla y León");
    private final RegionBounds easternCastillaYLeonAndCantabria = new RegionBounds("Eastern Castilla y León + Cantabria");
    private final RegionBounds westernGalicia = new RegionBounds("Western Galicia");
    private final RegionBounds easternGalicia = new RegionBounds("Eastern Galicia");
    private final RegionBounds northernIberia = new RegionBounds("Basque Country + Navarra + La Rioja + Soria");
    private final RegionBounds aragon = new RegionBounds("Aragón");
    private final RegionBounds westernCatalonia = new RegionBounds("Western Catalonia");
    private final RegionBounds easternCatalonia = new RegionBounds("Eastern Catalonia");
    private final RegionBounds valencianCommunity = new RegionBounds("Valencian Community");

    private final RegionBounds andorra = new RegionBounds("Andorra");

    private final RegionBounds defaultRegion = new RegionBounds("Default");

    public RegionBoundsManager(Context context) {
        this.context = context;

        setPortugueseSubregionBounds();
        setSpanishSubregionBounds();
        setGibraltarSubregionBounds();
        setAndorranSubregionBounds();

        setPortugueseRegionBounds();
        setSpanishAndGibraltarRegionBounds();
        setAndorranRegionBounds();

        setDefaults();
    }

    public boolean hasRegionMultipleSubregions(LocationInfo locationInfo) {
        RegionBounds regionBounds = getRegionBoundsObjectByLocation(locationInfo);
        if (regionBounds == null)
            return false;
        int subregionsNumber = regionBounds.getSubregionsNumber();

        return subregionsNumber > 1;
    }

    public LatLng[] getGlobalBoundsByLocation(LocationInfo locationInfo) {
        double northernmostLatitude;
        double southernmostLatitude;
        double westernmostLongitude;
        double easternmostLongitude;

        if (locationInfo.getCountry().equals(context.getString(R.string.portugal)) && ((LocationInfoPortugal) locationInfo).getDistrict().equals(context.getString(R.string.acores))) { // Açores islands
            northernmostLatitude = 39.727435; // Ilhéu do Torrão, north of Corvo Island
            southernmostLatitude = 36.927742; // Ponta do Castelo, Santa Maria Island
            westernmostLongitude = -31.275617; // Ilhéu de Monchique, west of Flores Island
            easternmostLongitude = -24.780056; // Ilhéus das Formigas, between São Miguel Island and Santa Maria Island

        } else if (locationInfo.isInIberianPeninsula(context)) { // Iberian Peninsula, Balearic Islands, Ceuta, and Melilla
            northernmostLatitude = 43.783333; // Punta de Estaca de Bares, A Coruña
            // southernmostLatitude = 36.0; // Punta de Tarifa, Cádiz
            southernmostLatitude = 35.265629; // Southernmost point of Melilla
            westernmostLongitude = -9.500587; // Cabo da Roca, Lisbon
            // easternmostLongitude = 3.322270; // Cap de Creus, Girona
            easternmostLongitude = 4.327554; // Punta de S'Esperó, Menorca Island, Balearic Islands

        } else { // Locations beyond the Iberian Peninsula
            int latitudeDegrees = 2; // In the Iberian Peninsula, 1 degree of longitude and latitude is very approximately 100 km
            int longitudeDegrees = 2;

            northernmostLatitude = locationInfo.getLatitude() + latitudeDegrees;
            southernmostLatitude = locationInfo.getLatitude() - latitudeDegrees;
            westernmostLongitude = locationInfo.getLongitude() - longitudeDegrees;
            easternmostLongitude = locationInfo.getLongitude() + longitudeDegrees;
        }

        return new LatLng[]{
                new LatLng(northernmostLatitude, westernmostLongitude),
                new LatLng(northernmostLatitude, easternmostLongitude),
                new LatLng(southernmostLatitude, westernmostLongitude),
                new LatLng(southernmostLatitude, easternmostLongitude),
        };
    }

    public LatLng[] getRegionBoundsByLocation(LocationInfo locationInfo) {
        RegionBounds regionBounds = getRegionBoundsObjectByLocation(locationInfo);
        if (regionBounds == null)
            return null;
        else
            return regionBounds.getBounds();
    }

    private RegionBounds getRegionBoundsObjectByLocation(LocationInfo locationInfo) {
        if (locationInfo.getCountry().equals(context.getString(R.string.portugal))) {
            String district = ((LocationInfoPortugal) locationInfo).getDistrict();
            String intermunicipalEntity = ((LocationInfoPortugal) locationInfo).getIntermunicipalEntity();
            String municipality = ((LocationInfoPortugal) locationInfo).getMunicipality();

            if (district.equals("Faro"))
                return algarve;
            else if (Arrays.asList("Beja", "Évora", "Portalegre").contains(district) ||
                    intermunicipalEntity.equals("Alentejo Litoral"))
                return alentejo;
            else if (Arrays.asList("Lisboa", "Santarém", "Leiria").contains(district) ||
                    intermunicipalEntity.equals("Península de Setúbal")) // Approx. Lisboa e Vale do Tejo
                return lisboaValeTejo;
            else if (Arrays.asList("Coimbra", "Aveiro", "Viseu").contains(district)) // Approx. Beira Litoral
                return beiraLitoral;
            else if (Arrays.asList("Castelo Branco", "Guarda").contains(district)) // Approx. Beira Interior
                return beiraInterior;
            else if (Arrays.asList("Porto", "Braga", "Viana do Castelo").contains(district)) // Approx. Entre-Douro-e-Minho
                return entreDouroMinho;
            else if (Arrays.asList("Vila Real", "Bragança").contains(district))
                return trasOsMontes;
            else if (Arrays.asList("Vila do Porto", "Lagoa, Açores", "Nordeste", "Ponta Delgada", "Povoação", "Ribeira Grande", "Vila Franca do Campo").contains(municipality))
                return azoresEasternGroup;
            else
                return null;
        } else if (locationInfo.getCountry().equals(context.getString(R.string.spain))) {
            String autonomousCommunity = ((LocationInfoSpain) locationInfo).getAutonomousCommunity();
            String province = ((LocationInfoSpain) locationInfo).getProvince();

            if (Arrays.asList("Huelva", "Sevilha", "Cádiz").contains(province))
                return westernAndaluciaAndGibraltar;
            else if (Arrays.asList("Córdoba", "Málaga").contains(province))
                return centralAndalucia;
            else if (Arrays.asList("Jaén", "Granada", "Almería").contains(province))
                return easternAndalucia;
            else if (Arrays.asList("Badajoz", "Cáceres").contains(province))
                return extremadura;
            else if (autonomousCommunity.equals("Comunidade de Madrid") ||
                    Arrays.asList("Ciudad Real", "Toledo").contains(province))
                return westernCastillaLaManchaAndMadrid;
            else if (Arrays.asList("Albacete", "Cuenca", "Guadalajara").contains(province) ||
                    autonomousCommunity.equals("Região de Murcia"))
                return easternCastillaLaManchaAndMurcia;
            else if (Arrays.asList("León", "Zamora", "Salamanca").contains(province) ||
                    autonomousCommunity.equals("Astúrias"))
                return westernCastillaYLeonAndAsturias;
            else if (Arrays.asList("Palencia", "Valladolid", "Ávila").contains(province))
                return centralCastillaYLeon;
            else if (Arrays.asList("Burgos", "Segóvia").contains(province) ||
                    autonomousCommunity.equals("Cantábria"))
                return easternCastillaYLeonAndCantabria;
            else if (Arrays.asList("Pontevedra", "A Coruña").contains(province))
                return westernGalicia;
            else if (Arrays.asList("Ourense", "Lugo").contains(province))
                return easternGalicia;
            else if (province.equals("Soria") ||
                    Arrays.asList("País Basco", "La Rioja", "Navarra").contains(autonomousCommunity))
                return northernIberia;
            else if (autonomousCommunity.equals("Aragão"))
                return aragon;
            else if (Arrays.asList("Lleida", "Tarragona").contains(province))
                return westernCatalonia;
            else if (Arrays.asList("Girona / Gerona", "Barcelona").contains(province))
                return easternCatalonia;
            else if (autonomousCommunity.equals("Comunidade Valenciana"))
                return valencianCommunity;
            else
                return null;
        } else if (locationInfo.getCountry().equals(context.getString(R.string.gibraltar_short_name))) {
            return westernAndaluciaAndGibraltar;
        } else if (locationInfo.getCountry().equals(context.getString(R.string.andorra))) {
            return andorra;
        } else { // Location beyond Iberian Peninsula
            return defaultRegion;
        }
    }

    public LatLng[] getSubregionBoundsByLocation(LocationInfo locationInfo) {
        SubregionBounds subregionBounds = getSubregionBoundsObjectByLocation(locationInfo);
        if (subregionBounds == null)
            return null;
        else
            return subregionBounds.getBounds();
    }

    private SubregionBounds getSubregionBoundsObjectByLocation(LocationInfo locationInfo) {
        if (locationInfo.getCountry().equals(context.getString(R.string.portugal))) {
            String district = ((LocationInfoPortugal) locationInfo).getDistrict();
            String intermunicipalEntity = ((LocationInfoPortugal) locationInfo).getIntermunicipalEntity();
            String concelho = ((LocationInfoPortugal) locationInfo).getMunicipality();

            // Algarve
            if (Arrays.asList("Alcoutim", "Castro Marim", "Faro", "Loulé", "Olhão",
                    "São Brás de Alportel", "Tavira", "Vila Real de Santo António").contains(concelho))
                return sotaventoAlgarvio;
            else if (Arrays.asList("Albufeira", "Aljezur", "Lagoa", "Lagos", "Monchique",
                    "Portimão", "Silves", "Vila do Bispo").contains(concelho))
                return barlaventoAlgarvio;
            // Alentejo
            else if ((intermunicipalEntity.equals("Alentejo Litoral")) &&
                    !concelho.equals("Odemira")) // Approx. Alentejo Litoral
                return alentejoLitoral;
            else if (district.equals("Beja"))
                return beja;
            else if (district.equals("Évora"))
                return evora;
            else if (district.equals("Portalegre"))
                return portalegre;
            // Lisbon and Tagus Valley
            else if (intermunicipalEntity.equals("Península de Setúbal"))
                return peninsulaSetubal;
            else if (district.equals("Lisboa"))
                return lisboa;
            else if (district.equals("Leiria"))
                return leiria;
            else if (district.equals("Santarém"))
                return santarem;
            // Beiras
            else if (district.equals("Coimbra"))
                return coimbra;
            else if (district.equals("Aveiro"))
                return aveiro;
            else if (district.equals("Viseu"))
                return viseu;
            else if (district.equals("Castelo Branco"))
                return casteloBranco;
            else if (district.equals("Guarda"))
                return guarda;
            // North
            else if (district.equals("Porto"))
                return porto;
            else if (district.equals("Braga"))
                return braga;
            else if (district.equals("Viana do Castelo"))
                return vianaDoCastelo;
            else if (district.equals("Vila Real"))
                return vilaReal;
            else if (district.equals("Bragança"))
                return braganca;
            // Azores - Eastern Group
            else if (concelho.equals("Vila do Porto"))
                return islandSantaMariaAndFormigas;
            else if (Arrays.asList("Lagoa, Açores", "Nordeste", "Ponta Delgada", "Povoação", "Ribeira Grande", "Vila Franca do Campo").contains(concelho))
                return islandSaoMiguel;
            else
                return null;
        } else if (locationInfo.getCountry().equals(context.getString(R.string.spain))) {
            String province = ((LocationInfoSpain) locationInfo).getProvince();
            return switch (province) {
                // Andalucía
                case "Huelva" -> huelva;
                case "Sevilha" -> sevilha;
                case "Cádiz" -> cadiz;
                case "Córdoba" -> cordoba;
                case "Málaga" -> malaga;
                case "Jaén" -> jaen;
                case "Granada" -> granada;
                case "Almería" -> almeria;
                // Extremadura
                case "Badajoz" -> badajoz;
                case "Cáceres" -> caceres;
                // Castilla-La Mancha, Madrid Community and Murcia Region
                case "Ciudad Real" -> ciudadReal;
                case "Toledo" -> toledo;
                case "Albacete" -> albacete;
                case "Cuenca" -> cuenca;
                case "Guadalajara" -> guadalajara;
                case "Comunidade de Madrid" -> comunidadeDeMadrid;
                case "Região de Murcia" -> regiaoDeMurcia;
                // Castilla y León, Asturias and Cantabria
                case "León" -> leon;
                case "Zamora" -> zamora;
                case "Salamanca" -> salamanca;
                case "Segóvia" -> segovia;
                case "Palencia" -> palencia;
                case "Valladolid" -> valladolid;
                case "Ávila" -> avila;
                case "Burgos" -> burgos;
                case "Soria" -> soria;
                case "Astúrias" -> asturias;
                case "Cantábria" -> cantabria;
                // Galicia
                case "Pontevedra" -> pontevedra;
                case "A Coruña" -> aCoruna;
                case "Ourense" -> ourense;
                case "Lugo" -> lugo;
                // Basque Country, La Rioja and Navarra
                case "Vizcaya" -> vizcaya;
                case "Guipúzcoa" -> guipuzcoa;
                case "Álava" -> alava;
                case "La Rioja" -> laRioja;
                case "Navarra" -> navarra;
                // Aragón
                case "Huesca" -> huesca;
                case "Zaragoza" -> zaragoza;
                case "Teruel" -> teruel;
                // Catalonia
                case "Lleida" -> lleida;
                case "Tarragona" -> tarragona;
                case "Girona / Gerona" -> girona;
                case "Barcelona" -> barcelona;
                // Valencian Community
                case "Castelló de la Plana / Castellón de la Plana" -> castellonDeLaPlana;
                case "València / Valencia" -> valencia;
                case "Alacant / Alicante" -> alicante;
                default -> null;
            };
        } else if (locationInfo.getCountry().equals(context.getString(R.string.gibraltar_short_name))) {
            return gibraltar;
        } else if (locationInfo.getCountry().equals(context.getString(R.string.andorra))) {
            String parish = ((LocationInfoAndorra) locationInfo).getParish();
            return switch (parish) {
                case "Sant Julià de Lòria" -> santJuliaDeLoria;
                case "Andorra la Vella" -> andorraLaVella;
                case "Canillo" -> canillo;
                case "Encamp" -> encamp;
                case "Ordino" -> ordino;
                case "La Massana" -> laMassana;
                case "Escaldes-Engordany" -> escaldesEngordany;
                default -> null;
            };
        } else { // Location beyond Iberian Peninsula
            return defaultSubregion;
        }
    }

    private void setPortugueseRegionBounds() {
        // ADD PORTUGUESE REGION BOUNDS HERE

        algarve.addSubregion(sotaventoAlgarvio);
        algarve.addSubregion(barlaventoAlgarvio);

        alentejo.addSubregion(beja);
        alentejo.addSubregion(alentejoLitoral);
        alentejo.addSubregion(evora);
        alentejo.addSubregion(portalegre);

        lisboaValeTejo.addSubregion(peninsulaSetubal);
        lisboaValeTejo.addSubregion(lisboa);
        lisboaValeTejo.addSubregion(leiria);
        lisboaValeTejo.addSubregion(santarem);

        beiraLitoral.addSubregion(coimbra);
        beiraLitoral.addSubregion(aveiro);
        beiraLitoral.addSubregion(viseu);

        beiraInterior.addSubregion(casteloBranco);
        beiraInterior.addSubregion(guarda);

        entreDouroMinho.addSubregion(porto);
        entreDouroMinho.addSubregion(braga);
        entreDouroMinho.addSubregion(vianaDoCastelo);

        trasOsMontes.addSubregion(vilaReal);
        trasOsMontes.addSubregion(braganca);

        azoresEasternGroup.addSubregion(islandSantaMariaAndFormigas);
        azoresEasternGroup.addSubregion(islandSaoMiguel);
    }

    private void setSpanishAndGibraltarRegionBounds() {
        // ADD SPANISH AND GIBRALTAR REGION BOUNDS HERE

        westernAndaluciaAndGibraltar.addSubregion(huelva);
        westernAndaluciaAndGibraltar.addSubregion(sevilha);
        westernAndaluciaAndGibraltar.addSubregion(cadiz);
        westernAndaluciaAndGibraltar.addSubregion(gibraltar);

        centralAndalucia.addSubregion(cordoba);
        centralAndalucia.addSubregion(malaga);

        easternAndalucia.addSubregion(jaen);
        easternAndalucia.addSubregion(granada);
        easternAndalucia.addSubregion(almeria);

        extremadura.addSubregion(badajoz);
        extremadura.addSubregion(caceres);

        westernCastillaLaManchaAndMadrid.addSubregion(ciudadReal);
        westernCastillaLaManchaAndMadrid.addSubregion(toledo);
        westernCastillaLaManchaAndMadrid.addSubregion(comunidadeDeMadrid);

        easternCastillaLaManchaAndMurcia.addSubregion(guadalajara);
        easternCastillaLaManchaAndMurcia.addSubregion(cuenca);
        easternCastillaLaManchaAndMurcia.addSubregion(albacete);
        easternCastillaLaManchaAndMurcia.addSubregion(regiaoDeMurcia);

        westernCastillaYLeonAndAsturias.addSubregion(leon);
        westernCastillaYLeonAndAsturias.addSubregion(zamora);
        westernCastillaYLeonAndAsturias.addSubregion(salamanca);
        westernCastillaYLeonAndAsturias.addSubregion(asturias);

        centralCastillaYLeon.addSubregion(palencia);
        centralCastillaYLeon.addSubregion(valladolid);
        centralCastillaYLeon.addSubregion(avila);

        easternCastillaYLeonAndCantabria.addSubregion(burgos);
        easternCastillaYLeonAndCantabria.addSubregion(segovia);
        easternCastillaYLeonAndCantabria.addSubregion(cantabria);

        westernGalicia.addSubregion(pontevedra);
        westernGalicia.addSubregion(aCoruna);

        easternGalicia.addSubregion(ourense);
        easternGalicia.addSubregion(lugo);

        northernIberia.addSubregion(soria);
        northernIberia.addSubregion(vizcaya);
        northernIberia.addSubregion(guipuzcoa);
        northernIberia.addSubregion(alava);
        northernIberia.addSubregion(laRioja);
        northernIberia.addSubregion(navarra);

        aragon.addSubregion(huesca);
        aragon.addSubregion(zaragoza);
        aragon.addSubregion(teruel);

        westernCatalonia.addSubregion(tarragona);
        westernCatalonia.addSubregion(lleida);

        easternCatalonia.addSubregion(girona);
        easternCatalonia.addSubregion(barcelona);

        valencianCommunity.addSubregion(castellonDeLaPlana);
        valencianCommunity.addSubregion(valencia);
        valencianCommunity.addSubregion(alicante);
    }

    private void setAndorranRegionBounds() {
        andorra.addSubregion(santJuliaDeLoria);
        andorra.addSubregion(andorraLaVella);
        andorra.addSubregion(canillo);
        andorra.addSubregion(encamp);
        andorra.addSubregion(ordino);
        andorra.addSubregion(laMassana);
        andorra.addSubregion(escaldesEngordany);
    }

    private void setPortugueseSubregionBounds() {
        // ADD PORTUGUESE SUBREGION BOUNDS HERE

        sotaventoAlgarvio.setMaxNorth(37.528930, -7.574430);
        sotaventoAlgarvio.setMaxSouth(36.960175, -7.888063);
        sotaventoAlgarvio.setMaxWest(37.21675,-8.23201);
        sotaventoAlgarvio.setMaxEast(37.163375, -7.399764);

        barlaventoAlgarvio.setMaxNorth(37.44295,-8.79633);
        barlaventoAlgarvio.setMaxSouth(36.99349,-8.94886);
        barlaventoAlgarvio.setMaxWest(37.02227,-8.99752);
        barlaventoAlgarvio.setMaxEast(37.07500,-8.12693);

        beja.setMaxNorth(38.3311,-7.2878);
        beja.setMaxSouth(37.31894,-8.06589);
        beja.setMaxWest(37.5994,-8.8196);
        beja.setMaxEast(38.20832,-6.93152);

        alentejoLitoral.setMaxNorth(38.55311,-8.61576);
        alentejoLitoral.setMaxSouth(37.3753,-8.6399);
        alentejoLitoral.setMaxWest(38.5026,-8.9128);
        alentejoLitoral.setMaxEast(38.2243,-8.1299);

        evora.setMaxNorth(39.0270,-8.1520);
        evora.setMaxSouth(38.1547,-7.1521);
        evora.setMaxWest(38.6022,-8.6579);
        evora.setMaxEast(38.1901,-7.1068);

        portalegre.setMaxNorth(39.6637,-7.5417);
        portalegre.setMaxSouth(38.7515,-7.2037);
        portalegre.setMaxWest(39.1048,-8.3440);
        portalegre.setMaxEast(39.0246,-6.9521);

        peninsulaSetubal.setMaxNorth(38.84543,-8.70803);
        peninsulaSetubal.setMaxSouth(38.40910,-9.19838);
        peninsulaSetubal.setMaxWest(38.66263,-9.26352);
        peninsulaSetubal.setMaxEast(38.76131,-8.49098);

        lisboa.setMaxNorth(39.3177,-9.2556);
        lisboa.setMaxSouth(38.6731,-9.3238);
        lisboa.setMaxWest(38.78094,-9.50059);
        lisboa.setMaxEast(39.05839,-8.78186);

        leiria.setMaxNorth(40.0897,-8.1791);
        leiria.setMaxSouth(39.2116,-9.1781);
        leiria.setMaxWest(39.47372,-9.54983);
        leiria.setMaxEast(39.96209,-8.10805);

        santarem.setMaxNorth(39.83873,-8.47797);
        santarem.setMaxSouth(38.7315,-8.9037);
        santarem.setMaxWest(39.3545,-9.0016);
        santarem.setMaxEast(39.5658,-7.8090);

        coimbra.setMaxNorth(40.5203,-8.7840);
        coimbra.setMaxSouth(39.9240,-8.3858);
        coimbra.setMaxWest(40.1864,-8.9092);
        coimbra.setMaxEast(40.1060,-7.7317);

        aveiro.setMaxNorth(41.0801,-8.2924);
        aveiro.setMaxSouth(40.2788,-8.4593);
        aveiro.setMaxWest(40.5203,-8.7841);
        aveiro.setMaxEast(40.9877,-8.0892);

        viseu.setMaxNorth(41.2148,-7.4462);
        viseu.setMaxSouth(40.3227,-8.2370);
        viseu.setMaxWest(40.3717,-8.3595);
        viseu.setMaxEast(41.1396,-7.3046);

        casteloBranco.setMaxNorth(40.4161,-7.2929);
        casteloBranco.setMaxSouth(39.5374,-7.8252);
        casteloBranco.setMaxWest(39.8077,-8.2932);
        casteloBranco.setMaxEast(40.0120,-6.8640);

        guarda.setMaxNorth(41.1794,-7.1180);
        guarda.setMaxSouth(40.2299,-7.7824);
        guarda.setMaxWest(40.4276,-7.8492);
        guarda.setMaxEast(40.3645,-6.7811);

        porto.setMaxNorth(41.47198,-8.77643);
        porto.setMaxSouth(41.0015,-8.3892);
        porto.setMaxWest(41.4160,-8.7891);
        porto.setMaxEast(41.2420,-7.8756);

        braga.setMaxNorth(41.82064,-8.05343);
        braga.setMaxSouth(41.3188,-8.0358);
        braga.setMaxWest(41.6116,-8.8116);
        braga.setMaxEast(41.5734,-7.8107);

        vianaDoCastelo.setMaxNorth(42.15431,-8.19876);
        vianaDoCastelo.setMaxSouth(41.6064,-8.8008);
        vianaDoCastelo.setMaxWest(41.7528,-8.8810);
        vianaDoCastelo.setMaxEast(42.0283,-8.0829);

        vilaReal.setMaxNorth(41.9272,-7.9064);
        vilaReal.setMaxSouth(41.1188,-7.9055);
        vilaReal.setMaxWest(41.6893,-8.1195);
        vilaReal.setMaxEast(41.7677,-7.1706);

        braganca.setMaxNorth(41.9925,-6.8114);
        braganca.setMaxSouth(41.0245,-6.9891);
        braganca.setMaxWest(41.2109,-7.4320);
        braganca.setMaxEast(41.5749,-6.1892);

        islandSantaMariaAndFormigas.setMaxNorth(37.275690, -24.780918);
        islandSantaMariaAndFormigas.setMaxSouth(36.927637, -25.017685);
        islandSantaMariaAndFormigas.setMaxWest(36.995426, -25.186179);
        islandSantaMariaAndFormigas.setMaxEast(37.274297, -24.779848);

        islandSaoMiguel.setMaxNorth(37.910624, -25.781361);
        islandSaoMiguel.setMaxSouth(37.704009, -25.443287);
        islandSaoMiguel.setMaxWest(37.861270, -25.856082);
        islandSaoMiguel.setMaxEast(37.807619, -25.134165);
    }

    private void setSpanishSubregionBounds() {
        // ADD SPANISH SUBREGION BOUNDS HERE

        huelva.setMaxNorth(38.20830,-6.93152);
        huelva.setMaxSouth(36.79558,-6.36748);
        huelva.setMaxWest(37.5554,-7.5227);
        huelva.setMaxEast(37.8161,-6.1215);

        sevilha.setMaxNorth(38.1972,-5.7283);
        sevilha.setMaxSouth(36.8441,-5.9730);
        sevilha.setMaxWest(37.6268,-6.5389);
        sevilha.setMaxEast(37.2560,-4.6536);

        cadiz.setMaxNorth(37.0525,-5.4337);
        cadiz.setMaxSouth(36.00004,-5.61111);
        cadiz.setMaxWest(36.73624,-6.44402);
        cadiz.setMaxEast(36.90511,-5.08678);

        cordoba.setMaxNorth(38.7291,-5.0470);
        cordoba.setMaxSouth(37.1844,-4.3277);
        cordoba.setMaxWest(38.1392,-5.5860);
        cordoba.setMaxEast(37.40209,-4.00086);

        malaga.setMaxNorth(37.28243,-4.41799);
        malaga.setMaxSouth(36.31035,-5.25125);
        malaga.setMaxWest(36.5416,-5.6118);
        malaga.setMaxEast(36.7882,-3.7659);

        jaen.setMaxNorth(38.53302,-2.76724);
        jaen.setMaxSouth(37.3782,-3.9510);
        jaen.setMaxWest(37.8014,-4.2876);
        jaen.setMaxEast(38.2648,-2.4344);

        granada.setMaxNorth(38.0842,-2.5514);
        granada.setMaxSouth(36.69351,-3.46828);
        granada.setMaxWest(37.1844,-4.3275);
        granada.setMaxEast(37.9166,-2.2078);

        almeria.setMaxNorth(37.9166,-2.2077);
        almeria.setMaxSouth(36.6800,-2.7602);
        almeria.setMaxWest(36.7877,-3.1401);
        almeria.setMaxEast(37.37554,-1.62981);

        badajoz.setMaxNorth(39.4518,-4.6903);
        badajoz.setMaxSouth(37.9410,-6.1804);
        badajoz.setMaxWest(38.4359,-7.3358);
        badajoz.setMaxEast(39.16493,-4.64895);

        caceres.setMaxNorth(40.4866,-6.2313);
        caceres.setMaxSouth(39.0319,-6.1419);
        caceres.setMaxWest(39.66371,-7.54169);
        caceres.setMaxEast(39.3951,-4.9526);

        ciudadReal.setMaxNorth(39.5768,-4.1588);
        ciudadReal.setMaxSouth(38.3426,-4.2871);
        ciudadReal.setMaxWest(38.7291,-5.0469);
        ciudadReal.setMaxEast(38.7350,-2.6365);

        toledo.setMaxNorth(40.3185,-4.3812);
        toledo.setMaxSouth(39.2586,-3.8547);
        toledo.setMaxWest(39.8778,-5.4062);
        toledo.setMaxEast(39.6424,-2.9083);

        comunidadeDeMadrid.setMaxNorth(41.165731, -3.543958);
        comunidadeDeMadrid.setMaxSouth(39.884752, -3.804706);
        comunidadeDeMadrid.setMaxWest(40.217163, -4.579124);
        comunidadeDeMadrid.setMaxEast(40.099441, -3.053298);

        leon.setMaxNorth(43.2382,-4.9074);
        leon.setMaxSouth(42.0293,-5.4204);
        leon.setMaxWest(42.5080,-7.0771);
        leon.setMaxEast(43.0470,-4.7324);

        zamora.setMaxNorth(42.2542,-6.7875);
        zamora.setMaxSouth(41.1172,-5.3289);
        zamora.setMaxWest(42.0717,-7.0343);
        zamora.setMaxEast(41.5285,-5.2289);

        salamanca.setMaxNorth(41.2943,-6.4794);
        salamanca.setMaxSouth(40.2389,-6.7555);
        salamanca.setMaxWest(41.0174,-6.9318);
        salamanca.setMaxEast(40.9967,-5.0899);

        regiaoDeMurcia.setMaxNorth(38.7551,-1.1836);
        regiaoDeMurcia.setMaxSouth(37.3738,-1.6287);
        regiaoDeMurcia.setMaxWest(38.0300,-2.3446);
        regiaoDeMurcia.setMaxEast(37.65611,-0.64717);

        pontevedra.setMaxNorth(42.86016,-8.18286);
        pontevedra.setMaxSouth(41.86941,-8.87346);
        pontevedra.setMaxWest(42.36741,-8.94964);
        pontevedra.setMaxEast(42.7063,-7.8614);

        aCoruna.setMaxNorth(43.79042,-7.68841);
        aCoruna.setMaxSouth(42.46291,-9.01062);
        aCoruna.setMaxWest(43.0486,-9.3015);
        aCoruna.setMaxEast(43.7767,-7.6618);

        segovia.setMaxNorth(41.58608,-3.96383);
        segovia.setMaxSouth(40.63345,-4.41964);
        segovia.setMaxWest(41.1234,-4.7247);
        segovia.setMaxEast(41.3067,-3.2069);

        guadalajara.setMaxNorth(41.3276322, -2.9066681);
        guadalajara.setMaxSouth(40.1524557, -3.0165175);
        guadalajara.setMaxWest(41.1505144, -3.5405498);
        guadalajara.setMaxEast(40.6870188, -1.5356379);

        cuenca.setMaxNorth(40.6586924, -2.1569365);
        cuenca.setMaxSouth(39.2266611, -2.2886773);
        cuenca.setMaxWest(40.048139, -3.1702345);
        cuenca.setMaxEast(39.9718415, -1.1423952);

        albacete.setMaxNorth(39.4229815, -1.4845298);
        albacete.setMaxSouth(38.0224402, -2.357922);
        albacete.setMaxWest(38.9412811, -2.8819231);
        albacete.setMaxEast(38.6959646, -0.9157933);

        palencia.setMaxNorth(43.061022, -4.4619533);
        palencia.setMaxSouth(41.7581663, -4.4456991);
        palencia.setMaxWest(42.173629, -5.0318945);
        palencia.setMaxEast(41.9888509, -3.8896224);

        valladolid.setMaxNorth(42.311842, -5.1120022);
        valladolid.setMaxSouth(41.0940153, -4.8636772);
        valladolid.setMaxWest(42.0971396, -5.5208908);
        valladolid.setMaxEast(41.5820144, -3.9804421);

        avila.setMaxNorth(41.1642497, -5.0138569);
        avila.setMaxSouth(40.0824504, -5.1997924);
        avila.setMaxWest(40.6898436, -4.1602545);
        avila.setMaxEast(40.2941518, -5.7375829);

        burgos.setMaxNorth(43.1987474, -3.2532734);
        burgos.setMaxSouth(41.4507842, -3.7394831);
        burgos.setMaxWest(42.6461953, -2.5175691);
        burgos.setMaxEast(42.430611, -4.335344);

        soria.setMaxNorth(42.1467061, -2.3481906);
        soria.setMaxSouth(41.0565078, -2.3234072);
        soria.setMaxWest(41.5965878, -3.5504383);
        soria.setMaxEast(41.724789, -1.7753716);

        asturias.setMaxNorth(43.6665324, -5.861129);
        asturias.setMaxSouth(42.8825428, -6.7728488);
        asturias.setMaxWest(43.3926979, -7.1831688);
        asturias.setMaxEast(43.3806142, -4.5105944);

        cantabria.setMaxNorth(43.5136234, -3.5907207);
        cantabria.setMaxSouth(42.7580499, -3.9772955);
        cantabria.setMaxWest(43.1728722, -4.8517354);
        cantabria.setMaxEast(43.3187852, -3.149652);

        ourense.setMaxNorth(42.5785999, -7.9307491);
        ourense.setMaxSouth(41.8073642, -7.4278996);
        ourense.setMaxWest(42.4873477, -8.3663444);
        ourense.setMaxEast(42.3591114, -6.7339533);

        lugo.setMaxNorth(43.761668, -7.6301032);
        lugo.setMaxSouth(42.3260122, -7.2032049);
        lugo.setMaxWest(42.8487418, -7.9997779);
        lugo.setMaxEast(42.8716833, -6.8140568);

        vizcaya.setMaxNorth(43.4568595, -2.7520347);
        vizcaya.setMaxSouth(42.9687184, -3.1225414);
        vizcaya.setMaxWest(43.321225, -2.4127205);
        vizcaya.setMaxEast(43.2359756, -3.4503463);

        guipuzcoa.setMaxNorth(43.3959579, -1.7910926);
        guipuzcoa.setMaxSouth(42.8950743, -2.2402124);
        guipuzcoa.setMaxWest(42.9822474, -2.6027119);
        guipuzcoa.setMaxEast(43.2942364, -1.7292688);

        alava.setMaxNorth(43.216969, -3.0272493);
        alava.setMaxSouth(42.4713392, -2.5893992);
        alava.setMaxWest(42.8848185, -3.2870106);
        alava.setMaxEast(42.8433196, -2.2328693);

        laRioja.setMaxNorth(42.6442647, -3.0047559);
        laRioja.setMaxSouth(41.919034, -1.9814424);
        laRioja.setMaxWest(42.5420529, -3.1342714);
        laRioja.setMaxEast(42.1889547, -1.6787015);

        navarra.setMaxNorth(43.3149461, -1.6683452);
        navarra.setMaxSouth(41.9098937, -1.5233068);
        navarra.setMaxWest(42.616265, -2.4999443);
        navarra.setMaxEast(42.9181957, -0.7233368);

        huesca.setMaxNorth(42.9244952, -0.7534742);
        huesca.setMaxSouth(41.3478056, -0.0079313);
        huesca.setMaxWest(42.5750452, -0.9344305);
        huesca.setMaxEast(42.3506427, 0.7713067);

        zaragoza.setMaxNorth(42.744126, -0.9241096);
        zaragoza.setMaxSouth(40.9362157, -1.5895609);
        zaragoza.setMaxWest(41.288085, -2.1736713);
        zaragoza.setMaxEast(41.2788442, 0.3856642);

        teruel.setMaxNorth(41.3543894, -0.502556);
        teruel.setMaxSouth(39.8467782, -0.8674198);
        teruel.setMaxWest(40.3982305, -1.806359);
        teruel.setMaxEast(40.970561, 0.2935008);

        lleida.setMaxNorth(42.8615226, 0.7086465);
        lleida.setMaxSouth(41.2741254, 0.4239894);
        lleida.setMaxWest(41.3947297, 0.3201019);
        lleida.setMaxEast(42.3696485, 1.8550577);

        tarragona.setMaxNorth(41.5826063, 1.4196815);
        tarragona.setMaxSouth(40.5230524, 0.5147059);
        tarragona.setMaxWest(40.750486, 0.1594133);
        tarragona.setMaxEast(41.2074067, 1.6530883);

        girona.setMaxNorth(42.4953859, 1.7471011);
        girona.setMaxSouth(41.6504759, 2.7780029);
        girona.setMaxWest(42.4865831, 1.7242671);
        girona.setMaxEast(42.3194399, 3.3222508);

        barcelona.setMaxNorth(42.3233011, 1.9031201);
        barcelona.setMaxSouth(41.1927078, 1.6472978);
        barcelona.setMaxWest(41.6137597, 1.3603839);
        barcelona.setMaxEast(41.6504759, 2.7780029);

        castellonDeLaPlana.setMaxNorth(40.7886312, -0.164957);
        castellonDeLaPlana.setMaxSouth(39.7146978, -0.4561763);
        castellonDeLaPlana.setMaxWest(39.9476979, -0.8462969);
        castellonDeLaPlana.setMaxEast(39.8961848, 0.6903174);

        valencia.setMaxNorth(40.2116832, -1.298228);
        valencia.setMaxSouth(38.6865536, -0.6286996);
        valencia.setMaxWest(39.4548706, -1.5289448);
        valencia.setMaxEast(38.8726112, -0.024722);

        alicante.setMaxNorth(38.8873915, -0.160832);
        alicante.setMaxSouth(37.8437887, -0.7717885);
        alicante.setMaxWest(38.4379292, -1.0941624);
        alicante.setMaxEast(38.7369798, 0.2341588);
    }

    private void setGibraltarSubregionBounds() {
        // ADD GIBRALTAR SUBREGION BOUNDS HERE

        gibraltar.setMaxNorth(36.155101, -5.345433);
        gibraltar.setMaxSouth(36.108838, -5.346123);
        gibraltar.setMaxEast(36.145086, -5.337617);
        gibraltar.setMaxWest(36.142543, -5.367428);
    }

    private void setAndorranSubregionBounds() {
        santJuliaDeLoria.setMaxNorth(42.5044741, 1.4603314);
        santJuliaDeLoria.setMaxSouth(42.4288238, 1.5157512);
        santJuliaDeLoria.setMaxEast(42.4586557, 1.5611409);
        santJuliaDeLoria.setMaxWest(42.486294, 1.4077997);

        andorraLaVella.setMaxNorth(42.5228745, 1.5215643);
        andorraLaVella.setMaxSouth(42.4687463, 1.5493625);
        andorraLaVella.setMaxEast(42.47383, 1.5591178);
        andorraLaVella.setMaxWest(42.5044741, 1.4603314);

        canillo.setMaxNorth(42.630316, 1.637122);
        canillo.setMaxSouth(42.543562, 1.6101369);
        canillo.setMaxEast(42.5741828, 1.786664);
        canillo.setMaxWest(42.546288, 1.562015);

        encamp.setMaxNorth(42.5591974, 1.6829666);
        encamp.setMaxSouth(42.4895387, 1.7038417);
        encamp.setMaxEast(42.5551747, 1.7388868);
        encamp.setMaxWest(42.5237965, 1.5460477);

        ordino.setMaxNorth(42.6559357, 1.5492987);
        ordino.setMaxSouth(42.545847, 1.541481);
        ordino.setMaxEast(42.6146061, 1.6074521);
        ordino.setMaxWest(42.6308896, 1.4680186);

        laMassana.setMaxNorth(42.6069182, 1.4646436);
        laMassana.setMaxSouth(42.509996, 1.4683999);
        laMassana.setMaxEast(42.546288, 1.562015);
        laMassana.setMaxWest(42.5353776, 1.4135781);

        escaldesEngordany.setMaxNorth(42.5237965, 1.5460477);
        escaldesEngordany.setMaxSouth(42.449886, 1.5788543);
        escaldesEngordany.setMaxEast(42.4815295, 1.6632892);
        escaldesEngordany.setMaxWest(42.5228745, 1.5215643);
    }

    private void setDefaults() {
        // Defaults to the entire Iberian Peninsula
        defaultSubregion.setMaxNorth(43.783333, -9.500587);
        defaultSubregion.setMaxSouth(36.0, 3.322270);
        defaultSubregion.setMaxWest(43.783333, -9.500587);
        defaultSubregion.setMaxEast(36.0, 3.322270);

        defaultRegion.addSubregion(defaultSubregion);
    }
}

class RegionBounds {
    // PT - Groups of districts or of parts of them
    // ES - Autonomous Communities or parts of them
    // GI - Gibraltar
    // AD - Andorra

    String name;
    ArrayList<SubregionBounds> subregions = new ArrayList<>();

    public RegionBounds(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SubregionBounds> getSubregions() {
        return subregions;
    }

    public int getSubregionsNumber() {
        return getSubregions().size();
    }

    public void addSubregion(SubregionBounds subregion) {
        this.subregions.add(subregion);
    }

    public LatLng[] getBounds() {
        return new LatLng[]{getMaxNorth(), getMaxSouth(), getMaxWest(), getMaxEast()};
    }

    private LatLng getMaxNorth() {
        LatLng maxNorth = new LatLng(-90.0, 0.0);
        for (int i = 0; i<getSubregions().size(); i++) {
            LatLng subregionMaxNorth = getSubregions().get(i).getMaxNorth();
            if (subregionMaxNorth.latitude > maxNorth.latitude) {
                maxNorth = subregionMaxNorth;
            }
        }
        return maxNorth;
    }

    private LatLng getMaxSouth() {
        LatLng maxSouth = new LatLng(90.0, 0.0);
        for (int i = 0; i<getSubregions().size(); i++) {
            LatLng subregionMaxSouth = getSubregions().get(i).getMaxSouth();
            if (subregionMaxSouth.latitude < maxSouth.latitude) {
                maxSouth = subregionMaxSouth;
            }
        }
        return maxSouth;
    }

    private LatLng getMaxWest() {
        LatLng maxWest = new LatLng(0, 179.9);
        for (int i = 0; i<getSubregions().size(); i++) {
            LatLng subregionMaxWest = getSubregions().get(i).getMaxWest();
            if (subregionMaxWest.longitude < maxWest.longitude) {
                maxWest = subregionMaxWest;
            }
        }
        return maxWest;
    }

    private LatLng getMaxEast() {
        LatLng maxEast = new LatLng(0, -180.0);
        for (int i = 0; i<getSubregions().size(); i++) {
            LatLng subregionMaxEast = getSubregions().get(i).getMaxEast();
            if (subregionMaxEast.longitude > maxEast.longitude) {
                maxEast = subregionMaxEast;
            }
        }
        return maxEast;
    }

}

class SubregionBounds {
    // PT - District, intermunicipal entity, or similar
    // ES - Province
    // GI - Gibraltar
    // AD - Parish

    String name;
    LatLng maxNorth;
    LatLng maxSouth;
    LatLng maxWest;
    LatLng maxEast;

    public SubregionBounds(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getMaxNorth() {
        return maxNorth;
    }

    public void setMaxNorth(double latitude, double longitude) {
        this.maxNorth = new LatLng(latitude, longitude);
    }

    public LatLng getMaxSouth() {
        return maxSouth;
    }

    public void setMaxSouth(double latitude, double longitude) {
        this.maxSouth = new LatLng(latitude, longitude);
    }

    public LatLng getMaxWest() {
        return maxWest;
    }

    public void setMaxWest(double latitude, double longitude) {
        this.maxWest = new LatLng(latitude, longitude);
    }

    public LatLng getMaxEast() {
        return maxEast;
    }

    public void setMaxEast(double latitude, double longitude) {
        this.maxEast = new LatLng(latitude, longitude);
    }

    public LatLng[] getBounds() {
        return new LatLng[]{getMaxNorth(), getMaxSouth(), getMaxWest(), getMaxEast()};
    }
}
