package com.lunatech.IMDBDashboard.utils;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SparkSessionConfiguration {

    @Bean
    public SparkSession getSparkSession() {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("IMDBDashboard")
                .getOrCreate();
        return spark;
    }
}
