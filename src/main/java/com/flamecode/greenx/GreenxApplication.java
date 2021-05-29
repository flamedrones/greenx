package com.flamecode.greenx;

import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableReactiveFirestoreRepositories
public class GreenxApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenxApplication.class, args);
    }

}
