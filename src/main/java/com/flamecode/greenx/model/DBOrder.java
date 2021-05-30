package com.flamecode.greenx.model;

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
public class DBOrder extends DroneOrderRequest {
    @DocumentId
    private String id;
    private LocalDateTime delivery_time;
    private LocalDateTime pickup_time;
    private String status;
}
