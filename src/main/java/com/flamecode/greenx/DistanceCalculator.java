package com.flamecode.greenx;

import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;

public class DistanceCalculator {

    public static double computeDistance(double originLng, double originLat, double destinationLng, double destinationLat){

        Point origin = Point.fromLngLat(originLng, originLat);
        Point destination = Point.fromLngLat(destinationLng, destinationLat);

        return computeDistance(origin, destination);
    }

    public static double computeDistance(Point origin, Point destination){

        return TurfMeasurement.distance(origin, destination, TurfConstants.UNIT_KILOMETERS);
    }

}
