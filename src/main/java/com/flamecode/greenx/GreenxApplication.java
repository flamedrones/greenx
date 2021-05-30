package com.flamecode.greenx;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@Configuration
@EnableReactiveFirestoreRepositories
public class GreenxApplication {


    public static void main(String[] args) {
        SpringApplication.run(GreenxApplication.class, args);
    }

    @Bean
    FirebaseApp configFirebaseApp(@Value("${spring.cloud.gcp.credentials.location}") Resource serviceAccountLocation) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(serviceAccountLocation.getFile());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseAuth configFirebaseAuth(@Autowired FirebaseApp firebase) {
        return FirebaseAuth.getInstance(firebase);
    }

    @Bean
    MapboxGeocoding.Builder configMapboxGeocoding(@Value("${mapbox.accessToken}") String accessToken) {
        return MapboxGeocoding.builder()
                .accessToken(accessToken);
    }

}
