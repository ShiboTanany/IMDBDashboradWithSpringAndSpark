package com.lunatech.IMDBDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImdbDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImdbDashboardApplication.class, args);
    }
}

