package com.flamecode.greenx.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collectionName = "users")
public class DBUser {
    @DocumentId
    private String id;
    private Wallet wallet;
}
