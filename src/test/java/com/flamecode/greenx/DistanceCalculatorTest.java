package com.flamecode.greenx;

import com.mapbox.geojson.Point;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {


    @Test
    public void givenTwoPointsWhenComputeDistanceThenSuccess(){

        Point cityIasi = Point.fromLngLat(47.15151065131377, 27.587756006184808);
        Point cityOnesti = Point.fromLngLat(46.24467397399121, 26.762658033143648);

        double distance = DistanceCalculator.computeDistance(cityIasi, cityOnesti);

        assertEquals(200.0, distance);
    }
}