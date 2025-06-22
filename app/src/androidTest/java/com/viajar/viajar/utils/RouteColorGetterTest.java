package com.viajar.viajar.utils;

import junit.framework.TestCase;

/*
 * Unit tests to test the Route Color Getter class
 */
public class RouteColorGetterTest extends TestCase {
    public void testIsHighwayPortugalStandard() {
        assertTrue(RouteColorGetter.isHighway("A1"));
        assertTrue(RouteColorGetter.isHighway("A12"));
        assertTrue(RouteColorGetter.isHighway("A99"));
        assertTrue(RouteColorGetter.isHighway("A999")); // Ensures compatibility with some French highways
        assertTrue(RouteColorGetter.isHighway("A9999")); // Just in case
    }

    public void testIsHighwayPortugalException() {
        assertTrue(RouteColorGetter.isHighway("A9 CREL"));
        assertTrue(RouteColorGetter.isHighway("A2 - 25 de Abril Bridge"));
        assertTrue(RouteColorGetter.isHighway("A4 - Avenida da Liberdade"));
        assertTrue(RouteColorGetter.isHighway("A28 - Avenida da Associação Empresarial de Portugal"));
        assertTrue(RouteColorGetter.isHighway("A13-1"));
        assertTrue(RouteColorGetter.isHighway("VRI"));
        assertTrue(RouteColorGetter.isHighway("A20/IC23 VCI/Ponte do Freixo"));
        assertTrue(RouteColorGetter.isHighway("A20/IC23 VCI/Freixo Bridge"));
    }

    public void testIsHighwaySpainStandard() {
        assertTrue(RouteColorGetter.isHighway("A-1"));
        assertTrue(RouteColorGetter.isHighway("A-1a"));
        assertTrue(RouteColorGetter.isHighway("A-1 - Avenida de Madrid"));
        assertTrue(RouteColorGetter.isHighway("A-9"));
        assertTrue(RouteColorGetter.isHighway("A-10"));
        assertTrue(RouteColorGetter.isHighway("A-10A"));
        assertTrue(RouteColorGetter.isHighway("A-10 - Avenida de Madrid"));
        assertTrue(RouteColorGetter.isHighway("A-99"));

        assertTrue(RouteColorGetter.isHighway("A-2/AP-7"));
        assertTrue(RouteColorGetter.isHighway("A-7/A-30"));

        assertTrue(RouteColorGetter.isHighway("AP-1"));
        assertTrue(RouteColorGetter.isHighway("AP-4F"));
        assertTrue(RouteColorGetter.isHighway("AP-9"));

        assertTrue(RouteColorGetter.isHighway("R-1"));
        assertTrue(RouteColorGetter.isHighway("R-6"));

        assertFalse(RouteColorGetter.isHighway("A-100"));
        assertFalse(RouteColorGetter.isHighway("A-999"));
        assertFalse(RouteColorGetter.isHighway("A-1000"));
        assertFalse(RouteColorGetter.isHighway("A-9999"));
    }

    public void testIsHighwaySpainException() {
        assertTrue(RouteColorGetter.isHighway("A-376"));
        assertTrue(RouteColorGetter.isHighway("AG-51"));
        assertTrue(RouteColorGetter.isHighway("AV-20"));
        assertTrue(RouteColorGetter.isHighway("B-23"));
        assertTrue(RouteColorGetter.isHighway("CA-34"));
        assertTrue(RouteColorGetter.isHighway("CM-41"));
        assertTrue(RouteColorGetter.isHighway("CO-32"));
        assertTrue(RouteColorGetter.isHighway("CV-80"));
        assertTrue(RouteColorGetter.isHighway("EX-A1"));
        assertTrue(RouteColorGetter.isHighway("GR-30"));
        assertTrue(RouteColorGetter.isHighway("H-30"));
        assertTrue(RouteColorGetter.isHighway("M-11"));
        assertTrue(RouteColorGetter.isHighway("MA-20"));
        assertTrue(RouteColorGetter.isHighway("PT-10"));
        assertTrue(RouteColorGetter.isHighway("RM-2"));
        assertTrue(RouteColorGetter.isHighway("SE-20"));
        assertTrue(RouteColorGetter.isHighway("V-31"));
    }
}
