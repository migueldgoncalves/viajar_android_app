package com.viajar.viajar.utils;

import com.viajar.viajar.utils.Coordinate;

import junit.framework.TestCase;

public class CoordinateTest extends TestCase {

    private Coordinate coordinate;

    @Override
    public void setUp() {
        this.coordinate = new Coordinate(1.0, 2.0);
    }

    public void testGetLatitude() {
        assertEquals(1.0, coordinate.getLatitude());
    }

    public void testSetLatitude() {
        coordinate.setLatitude(3.0);
        assertEquals(3.0, coordinate.getLatitude());
    }

    public void testGetLongitude() {
        assertEquals(2.0, coordinate.getLongitude());
    }

    public void testSetLongitude() {
        coordinate.setLongitude(3.0);
        assertEquals(3.0, coordinate.getLongitude());
    }

    public void testTestEquals() {
        Coordinate sameCoordinate = new Coordinate(1.0, 2.0);
        Coordinate anotherCoordinate = new Coordinate(3.0, 4.0);

        assertEquals(coordinate, sameCoordinate);
        assertNotSame(coordinate, anotherCoordinate);
    }
}