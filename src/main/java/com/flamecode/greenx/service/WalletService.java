package com.flamecode.greenx.service;

import com.flamecode.greenx.FGXManager;
import com.flamecode.greenx.model.DBUser;
import com.flamecode.greenx.repo.UsersRepo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletService {
    FirebaseApp firebase;
    FirebaseAuth auth;
    UsersRepo users;
    FGXManager fgx;

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    public WalletService(FirebaseApp firebase, FirebaseAuth auth, UsersRepo users, FGXManager fgx) {
        this.firebase = firebase;
        this.auth = auth;
        this.users = users;
        this.fgx = fgx;
    }

    public Mono<Boolean> checkIfUserExists(String email) throws FirebaseAuthException {
        try {
            auth.getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            if (e.getErrorCode().equals("user-not-found")) {
                return Mono.just(Boolean.FALSE);
            }
            throw e;
        }
        return Mono.just(Boolean.TRUE);
    }

    public Mono<UserRecord> createUser(String email) throws FirebaseAuthException {
        // I know stuff like this should be in a user service, but right now I'm tight on time
        return Mono.just(auth.createUser(new UserRecord.CreateRequest().setEmail(email)));
    }

    public Mono<UserRecord> getUser(String email) throws FirebaseAuthException {
        return Mono.just(auth.getUserByEmail(email));
    }

    public Mono<UserRecord> getOrCreateUser(String email) throws FirebaseAuthException {
        return checkIfUserExists(email).flatMap(result -> {
            try {
                if (result)
                    return getUser(email);
                return createUser(email);
            } catch (FirebaseAuthException e) {
                LOGGER.error("User Creation Failed", e);
            }
            return Mono.empty();
        });
    }

    public Mono<Boolean> checkIfWalletExistsByEmail(String email) throws FirebaseAuthException {
        return checkIfWalletExistsByUid(auth.getUserByEmail(email).getUid());
    }

    public Mono<Boolean> checkIfWalletExistsByUid(String uid) {
        return users.findById(uid).map(document -> {
            if (document.getWallet() == null || document.getWallet().getAddress().equals(""))
                return Boolean.FALSE;
            return Boolean.TRUE;
        }).switchIfEmpty(Mono.just(Boolean.FALSE));
    }

    public Mono<DBUser> createWalletByEmail(String email) throws FirebaseAuthException {
        return createWalletByUid(auth.getUserByEmail(email).getUid());
    }

    public Mono<DBUser> createWalletByUid(String uid) {
        var dbuser = new DBUser();
        dbuser.setId(uid);
        dbuser.setWallet(fgx.generateWallet());
        return users.save(dbuser);
    }

    public Mono<DBUser> getOrCreateWalletByUid(String uid) {
        return checkIfWalletExistsByUid(uid).flatMap(result -> {
            if (!result) {
                return createWalletByUid(uid);
            }
            return users.findById(uid);
        });
    }


    public Mono<String> sendTokenByEmail(String email, Double amount) throws FirebaseAuthException {
        var user = getOrCreateUser(email);
        var dbUser = user.flatMap(userrec -> getOrCreateWalletByUid(userrec.getUid()));
        return dbUser.map(userdb -> {
            try {
                return fgx.sendFGX(amount, userdb.getWallet().getAddress());
            } catch (Exception e) {
                LOGGER.error("Failed Sending FGX", e);
            }
            return "";
        });
    }

}
