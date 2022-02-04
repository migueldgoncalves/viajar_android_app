package com.viajar.viajar;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class RegionBoundsManager {

    // ADD SUBREGION DEFINITIONS HERE

    private final SubregionBounds sotaventoAlgarvio = new SubregionBounds("Sotavento Algarvio");
    private final SubregionBounds barlaventoAlgarvio = new SubregionBounds("Barlavento Algarvio");
    private final SubregionBounds beja = new SubregionBounds("Beja");
    private final SubregionBounds alentejoLitoral = new SubregionBounds("Alentejo Litoral");
    private final SubregionBounds evora = new SubregionBounds("Évora");
    private final SubregionBounds portalegre = new SubregionBounds("Portalegre");
    private final SubregionBounds peninsulaSetubal = new SubregionBounds("Península de Setúbal");
    private final SubregionBounds lisboa = new SubregionBounds("Lisboa");
    private final SubregionBounds leiria = new SubregionBounds("Leiria");
    private final SubregionBounds santarem = new SubregionBounds("Santarém");
    private final SubregionBounds coimbra = new SubregionBounds("Coimbra");
    private final SubregionBounds aveiro = new SubregionBounds("Aveiro");
    private final SubregionBounds viseu = new SubregionBounds("Viseu");
    private final SubregionBounds casteloBranco = new SubregionBounds("Castelo Branco");
    private final SubregionBounds guarda = new SubregionBounds("Guarda");
    private final SubregionBounds porto = new SubregionBounds("Porto");
    private final SubregionBounds braga = new SubregionBounds("Braga");
    private final SubregionBounds vianaDoCastelo = new SubregionBounds("Viana do Castelo");
    private final SubregionBounds vilaReal = new SubregionBounds("Vila Real");
    private final SubregionBounds braganca = new SubregionBounds("Bragança");

    private final SubregionBounds huelva = new SubregionBounds("Huelva");
    private final SubregionBounds sevilha = new SubregionBounds("Sevilha");
    private final SubregionBounds cadiz = new SubregionBounds("Cádiz");
    private final SubregionBounds cordoba = new SubregionBounds("Córdoba");
    private final SubregionBounds malaga = new SubregionBounds("Málaga");
    private final SubregionBounds jaen = new SubregionBounds("Jaén");
    private final SubregionBounds granada = new SubregionBounds("Granada");
    private final SubregionBounds almeria = new SubregionBounds("Almería");
    private final SubregionBounds badajoz = new SubregionBounds("Badajoz");
    private final SubregionBounds caceres = new SubregionBounds("Cáceres");
    private final SubregionBounds ciudadReal = new SubregionBounds("Ciudad Real");
    private final SubregionBounds toledo = new SubregionBounds("Toledo");
    private final SubregionBounds comunidadeDeMadrid = new SubregionBounds("Comunidade de Madrid");
    private final SubregionBounds leon = new SubregionBounds("León");
    private final SubregionBounds zamora = new SubregionBounds("Zamora");
    private final SubregionBounds salamanca = new SubregionBounds("Salamanca");

    private final SubregionBounds gibraltar = new SubregionBounds("Gibraltar");

    // ADD REGION DEFINITIONS HERE

    private final RegionBounds algarve = new RegionBounds("Algarve");
    private final RegionBounds alentejo = new RegionBounds("Alentejo");
    private final RegionBounds lisboaValeTejo = new RegionBounds("Lisboa e Vale do Tejo"); // Approx. Lisboa e Vale do Tejo
    private final RegionBounds beiraLitoral = new RegionBounds("Beira Litoral"); // Approx. Beira Litoral
    private final RegionBounds beiraInterior = new RegionBounds("Beira Interior"); // Approx. Beira Interior
    private final RegionBounds entreDouroMinho = new RegionBounds("Entre-Douro-e-Minho"); // Approx Entre-Douro-e-Minho
    private final RegionBounds trasOsMontes = new RegionBounds("Trás-os-Montes");

    private final RegionBounds westernAndaluciaAndGibraltar = new RegionBounds("Western Andalucía + Gibraltar");
    private final RegionBounds centralAndalucia = new RegionBounds("Central Andalucía");
    private final RegionBounds easternAndalucia = new RegionBounds("Eastern Andalucía");
    private final RegionBounds extremadura = new RegionBounds("Extremadura");
    private final RegionBounds westernCastillaLaManchaAndMadrid = new RegionBounds("Western Castilla-La Mancha + Comunidade de Madrid");
    private final RegionBounds westernCastillaYLeon = new RegionBounds("Western Castilla y León");

    public RegionBoundsManager() {
        setPortugueseSubregionBounds();
        setSpanishSubregionBounds();
        setGibraltarSubregionBounds();

        setPortugueseRegionBounds();
        setSpanishAndGibraltarRegionBounds();
    }

    public boolean hasRegionMultipleSubregions(LocationInfo locationInfo) {
        RegionBounds regionBounds = getRegionBoundsObjectByLocation(locationInfo);
        if (regionBounds == null)
            return false;
        int subregionsNumber = regionBounds.getSubregionsNumber();

        return subregionsNumber > 1;
    }

    public LatLng[] getRegionBoundsByLocation(LocationInfo locationInfo) {
        RegionBounds regionBounds = getRegionBoundsObjectByLocation(locationInfo);
        if (regionBounds == null)
            return null;
        else
            return regionBounds.getBounds();
    }

    private RegionBounds getRegionBoundsObjectByLocation(LocationInfo locationInfo) {
        switch (locationInfo.getCountry()) {
            case "Portugal":
                String district = ((LocationInfoPortugal) locationInfo).getDistrict();
                String intermunicipalEntity = ((LocationInfoPortugal) locationInfo).getIntermunicipalEntity();

                if (district.equals("Faro"))
                    return algarve;
                else if (Arrays.asList("Beja", "Évora", "Portalegre").contains(district) ||
                        intermunicipalEntity.equals("Alentejo Litoral"))
                    return alentejo;
                else if (Arrays.asList("Lisboa", "Santarém", "Leiria").contains(district) ||
                        intermunicipalEntity.equals("Área Metropolitana de Lisboa")) // Approx. Lisboa e Vale do Tejo
                    return lisboaValeTejo;
                else if (Arrays.asList("Coimbra", "Aveiro", "Viseu").contains(district)) // Approx. Beira Litoral
                    return beiraLitoral;
                else if (Arrays.asList("Castelo Branco", "Guarda").contains(district)) // Approx. Beira Interior
                    return beiraInterior;
                else if (Arrays.asList("Porto", "Braga", "Viana do Castelo").contains(district)) // Approx. Entre-Douro-e-Minho
                    return entreDouroMinho;
                else if (Arrays.asList("Vila Real", "Bragança").contains(district))
                    return trasOsMontes;
                else
                    return null;
            case "Spain":
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
                else if (Arrays.asList("León", "Zamora", "Salamanca").contains(province))
                    return westernCastillaYLeon;
                else
                    return null;
            case "Gibraltar":
                return westernAndaluciaAndGibraltar;
            default: // Error
                return null;
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
        switch (locationInfo.getCountry()) {
            case "Portugal":
                String district = ((LocationInfoPortugal) locationInfo).getDistrict();
                String intermunicipalEntity = ((LocationInfoPortugal) locationInfo).getIntermunicipalEntity();
                String concelho = ((LocationInfoPortugal) locationInfo).getMunicipality();

                if (Arrays.asList("Alcoutim", "Castro Marim", "Faro", "Loulé", "Olhão",
                        "São Brás de Alportel", "Tavira", "Vila Real de Santo António").contains(concelho))
                    return sotaventoAlgarvio;
                else if (Arrays.asList("Albufeira", "Aljezur", "Lagoa", "Lagos", "Monchique",
                        "Portimão", "Silves", "Vila do Bispo").contains(concelho))
                    return barlaventoAlgarvio;
                else if ((intermunicipalEntity.equals("Alentejo Litoral")) &&
                        !concelho.equals("Odemira")) // Approx. Alentejo Litoral
                    return alentejoLitoral;
                else if (district.equals("Beja"))
                    return beja;
                else if (district.equals("Évora"))
                    return evora;
                else if (district.equals("Portalegre"))
                    return portalegre;
                else if ((district.equals("Setúbal")) && ( // Península de Setúbal
                        intermunicipalEntity.equals("Área Metropolitana de Lisboa")))
                    return peninsulaSetubal;
                else if (district.equals("Lisboa"))
                    return lisboa;
                else if (district.equals("Leiria"))
                    return leiria;
                else if (district.equals("Santarém"))
                    return santarem;
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
                else
                    return null;
            case "Spain":
                String province = ((LocationInfoSpain) locationInfo).getProvince();
                switch (province) {
                    case "Huelva":
                        return huelva;
                    case "Sevilha":
                        return sevilha;
                    case "Cádiz":
                        return cadiz;
                    case "Córdoba":
                        return cordoba;
                    case "Málaga":
                        return malaga;
                    case "Jaén":
                        return jaen;
                    case "Granada":
                        return granada;
                    case "Almería":
                        return almeria;
                    case "Badajoz":
                        return badajoz;
                    case "Cáceres":
                        return caceres;
                    case "Ciudad Real":
                        return ciudadReal;
                    case "Toledo":
                        return toledo;
                    case "Comunidade de Madrid":
                        return comunidadeDeMadrid;
                    case "León":
                        return leon;
                    case "Zamora":
                        return zamora;
                    case "Salamanca":
                        return salamanca;
                    default:
                        return null;
                }
            case "Gibraltar":
                return gibraltar;
            default: // Error
                return null;
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

        westernCastillaYLeon.addSubregion(leon);
        westernCastillaYLeon.addSubregion(zamora);
        westernCastillaYLeon.addSubregion(salamanca);
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
    }

    private void setGibraltarSubregionBounds() {
        // ADD GIBRALTAR SUBREGION BOUNDS HERE

        gibraltar.setMaxNorth(36.155101, -5.345433);
        gibraltar.setMaxSouth(36.108838, -5.346123);
        gibraltar.setMaxEast(36.145086, -5.337617);
        gibraltar.setMaxWest(36.142543, -5.367428);
    }
}

class RegionBounds {
    // PT - Groups of districts or of parts of them
    // ES - Autonomous Communities or parts of them
    // GI - Gibraltar

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
