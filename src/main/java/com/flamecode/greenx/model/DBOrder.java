package com.flamecode.greenx.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collectionName = "orders")
public class DBOrder {
    @DocumentId
    private String id;
    private GeoPoint original_pos;
    private Dimension dimension;
    private GeoPoint pos;
    private GeoPoint delivery_pos;
    private Timestamp delivery_time;
    private Timestamp pickup_time;
    private String status;
}
