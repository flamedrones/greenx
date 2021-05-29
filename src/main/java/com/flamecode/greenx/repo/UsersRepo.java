package com.flamecode.greenx.repo;

import com.flamecode.greenx.model.DBUser;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

public interface UsersRepo extends FirestoreReactiveRepository<DBUser> {
}
