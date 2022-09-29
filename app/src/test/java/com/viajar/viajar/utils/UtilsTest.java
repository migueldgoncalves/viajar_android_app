package com.viajar.viajar.utils;

import com.viajar.viajar.utils.Coordinate;
import com.viajar.viajar.utils.Utils;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    public void testGetDistanceBetweenPoints() {
        assertTrue(Utils.isDistanceValid(666.2, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -5.0)))); // 0º
        assertTrue(Utils.isDistanceValid(671.6, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -4.0)))); // 0º > 45º
        assertTrue(Utils.isDistanceValid(839.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, 1.0)))); // ~45º
        assertTrue(Utils.isDistanceValid(541.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(38.0, 1.0)))); // 45º > 90º
        assertTrue(Utils.isDistanceValid(534.0, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(37.0, 1.0)))); // 90º
        assertTrue(Utils.isDistanceValid(548.8, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(36.0, 1.0)))); // 90º > 135º
        assertTrue(Utils.isDistanceValid(865.7, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, 1.0)))); // ~135º
        assertTrue(Utils.isDistanceValid(671.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -4.0)))); // 135º > 180º
        assertTrue(Utils.isDistanceValid(665.5, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -5.0)))); // 180º
        assertTrue(Utils.isDistanceValid(671.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -6.0)))); // 180º > 225º
        assertTrue(Utils.isDistanceValid(865.7, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -11.0)))); // ~225º
        assertTrue(Utils.isDistanceValid(548.8, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(36.0, -11.0)))); // 225º > 270º
        assertTrue(Utils.isDistanceValid(534.0, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(37.0, -11.0)))); // 270º
        assertTrue(Utils.isDistanceValid(541.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(38.0, -11.0)))); // 270º > 315º
        assertTrue(Utils.isDistanceValid(839.9, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -11.0)))); // ~315º
        assertTrue(Utils.isDistanceValid(671.6, Utils.getDistanceBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -6.0)))); // 315º > 360º
    }

    public void testIsDistanceValid() {
        assertFalse(Utils.isDistanceValid(100, 99.4));
        assertTrue(Utils.isDistanceValid(100, 99.5));
        assertTrue(Utils.isDistanceValid(100, 99.6));
        assertTrue(Utils.isDistanceValid(100, 99.7));
        assertTrue(Utils.isDistanceValid(100, 99.8));
        assertTrue(Utils.isDistanceValid(100, 99.9));
        assertTrue(Utils.isDistanceValid(100, 100));
        assertTrue(Utils.isDistanceValid(100, 100.1));
        assertTrue(Utils.isDistanceValid(100, 100.2));
        assertTrue(Utils.isDistanceValid(100, 100.3));
        assertTrue(Utils.isDistanceValid(100, 100.4));
        assertTrue(Utils.isDistanceValid(100, 100.5));
        assertFalse(Utils.isDistanceValid(100, 100.6));
    }

    public void testGetAngleBetweenPoints() {
        assertEquals(0, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(37.0, -5.0))); // Same point

        assertEquals(0, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -5.0))); // 0º
        assertEquals(7, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -4.0))); // 0º > 45º
        assertEquals(38, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, 1.0))); // ~45º
        assertEquals(78, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(38.0, 1.0))); // 45º > 90º
        assertEquals(90, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(37.0, 1.0))); // 90º
        assertEquals(102, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(36.0, 1.0))); // 90º > 135º
        assertEquals(142, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, 1.0))); // ~135º
        assertEquals(173, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -4.0))); // 135º > 180º
        assertEquals(180, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -5.0))); // 180º
        assertEquals(187, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -6.0))); // 180º > 225º
        assertEquals(218, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(31.0, -11.0))); // ~225º
        assertEquals(258, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(36.0, -11.0))); // 225º > 270º
        assertEquals(270, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(37.0, -11.0))); // 270º
        assertEquals(282, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(38.0, -11.0))); // 270º > 315º
        assertEquals(322, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -11.0))); // ~315º
        assertEquals(353, Utils.getAngleBetweenPoints(
                new Coordinate(37.0, -5.0), new Coordinate(43.0, -6.0))); // 315º > 360º
    }
}