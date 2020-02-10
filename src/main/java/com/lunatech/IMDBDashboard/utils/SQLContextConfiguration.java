package com.lunatech.IMDBDashboard.utils;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SQLContextConfiguration {

    @Autowired
    private SparkSession sparkSession;

    @Bean
    public SQLContext getSqlContext() {
        return new SQLContext(sparkSession);
    }
}
