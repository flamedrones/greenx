package com.flamecode.greenx.model;

import com.google.cloud.firestore.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneOrderRequest {
    private Dimension dimension;
    private ModelGeoPoint pos;
    private ModelGeoPoint delivery_pos;
}
