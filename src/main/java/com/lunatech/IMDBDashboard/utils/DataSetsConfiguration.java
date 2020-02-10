package com.lunatech.IMDBDashboard.utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataSetsConfiguration {

    @Autowired
    private SQLContext sqlContext;

    @Autowired
    private ApplicationPropertiesReader applicationPropertiesReader;

    @Bean("TitleBasics")
    public Dataset<Row> getTitleBasics() {

        return sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/title.basics.tsv");
        //.persist(StorageLevel.MEMORY_AND_DISK());
    }

    @Bean("TitleRatings")
    public Dataset<Row> getTitleRatings() {
        return sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/title.ratings.tsv")
                ;//.persist(StorageLevel.MEMORY_AND_DISK());
    }

    @Bean("NameBasicsPrimaryNameAndKnowForTitles")
    public Dataset<Row> getNameBasics() {
        return sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/name.basics.tsv")
                .select("primaryName", "knownForTitles")
                ;//.persist(StorageLevel.MEMORY_AND_DISK());
    }

    @Bean("NameBasics")
    public Dataset<Row> getNameBasicsOrigins() {
        return sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/name.basics.tsv")
                ;
    }


    @Bean("WorksItemsForKevinBacon")
    public Set<String> getWorksItemsForKevinBacon() {
        String nameOfActor = "Kevin Bacon";
        Set<String> titles = new HashSet<>();
        sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/name.basics.tsv")
                .where(functions.col("primaryName").equalTo(nameOfActor))
                .toLocalIterator().forEachRemaining(
                row -> {
                    titles.addAll(Arrays.asList(row.getString(5).split(",")));
                }
        );
        return titles;
    }

    @Cacheable("Actors")
    public Dataset<Row> getAllActorsWithTitles() {
        return sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema", "true")
                .option("header", "true")
                .option("delimiter", "\t")
                .load(applicationPropertiesReader.getIdmFilesPath() + "/name.basics.tsv")
                .select("primaryName","knownForTitles");
    }

    @Bean("NameBasicsCached")
    public Dataset<Row> getCachedNameAndBasics() {
        return getAllActorsWithTitles();
    }
}
