package com.flamecode.greenx.model;

import com.google.cloud.firestore.GeoPoint;

public class ModelGeoPoint extends GeoPoint {

    public ModelGeoPoint() {
        super(0, 0);
    }

    public ModelGeoPoint(double latitude, double longitude) {
        super(latitude, longitude);
    }
}
