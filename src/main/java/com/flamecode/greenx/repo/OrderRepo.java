package com.flamecode.greenx.repo;

import com.flamecode.greenx.model.DBOrder;
import com.flamecode.greenx.model.DBUser;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

public interface OrderRepo extends FirestoreReactiveRepository<DBOrder> {
}
