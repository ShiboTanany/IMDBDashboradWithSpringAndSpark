package com.lunatech.IMDBDashboard.services;


import com.lunatech.IMDBDashboard.dtos.MostReviewed;
import com.lunatech.IMDBDashboard.dtos.TypeCasted;
import com.lunatech.IMDBDashboard.dtos.TypeCastingInformation;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class IMDBDashboardService {

    @Autowired
    @Qualifier("TitleBasics")
    private Dataset<Row> dataFrameForTitleBasics;

    @Autowired
    @Qualifier("TitleRatings")
    private Dataset<Row> dataFrameForTitleRating;

    @Autowired
    @Qualifier("NameBasicsPrimaryNameAndKnowForTitles")
    private Dataset<Row> dataFrameForNameBasicsPrimaryNameAndKnowForTitles;

    @Autowired
    @Qualifier("NameBasics")
    private Dataset<Row> nameBasics;

    @Autowired
    @Qualifier("WorksItemsForKevinBacon")
    private Set<String> titles;

    @Autowired
    @Qualifier("NameBasicsCached")
    private Dataset<Row> nameBasicsCached;


    @Cacheable("MostReviewed")
    public List<MostReviewed> getRatedFilmsWithGenre(@PathVariable String genre) {
        Dataset<Row> data = dataFrameForTitleBasics
                .select("genres", "tconst", "primaryTitle", "originalTitle")
                .where(functions.col("genres").contains(genre));
        Iterator<Row> dataOutput = dataFrameForTitleRating
                .join(data, data.col("tconst")
                        .equalTo(dataFrameForTitleRating.col("tconst")))
                .select("averageRating", "numVotes", "primaryTitle", "originalTitle", "genres")
                .orderBy(functions.desc("averageRating"))
                .toLocalIterator();
        List<MostReviewed> mostReviewedList = new ArrayList<>();
        dataOutput.forEachRemaining(row -> {
            mostReviewedList.add(new MostReviewed(
                    row.getDouble(0), row.getInt(1), row.getString(2)
                    , row.getString(3), row.getString(4)));
        });
        return mostReviewedList;
    }

    @Cacheable("typeCasted")
    public ResponseEntity<TypeCasted> isActorTypeCasted(String nameOfActor) {
        Iterator<Row> listOfKnowForTitles = dataFrameForNameBasicsPrimaryNameAndKnowForTitles
                .where(functions.col("primaryName").equalTo(nameOfActor))
                .toLocalIterator();
        TypeCasted typeCasted = new TypeCasted();

        listOfKnowForTitles.forEachRemaining(row -> {
            System.out.println("hello ************************************* " + row);
            String[] knownForTitles = row.getString(1).split(",");
            typeCasted.setNameOfActor(row.getString(0));
            HashMap<String, Integer> mapOfGenresWithCounter = new HashMap<>();
            for (String title : knownForTitles) {
                Iterator<Row> listOfGenresRows = dataFrameForTitleBasics
                        .select("genres", "tconst")
                        .where(functions.col("tconst").contains(title))
                        .select("genres")
                        .toLocalIterator();
                listOfGenresRows.forEachRemaining(genresRow -> {
                    for (String genre : genresRow.getString(0).split(",")) {
                        if (mapOfGenresWithCounter.containsKey(genre)) {
                            mapOfGenresWithCounter.put(genre, mapOfGenresWithCounter.get(genre) + 1);
                        } else {
                            mapOfGenresWithCounter.put(genre, 1);
                        }
                    }
                });
            }

            if (getMaximumGenre(mapOfGenresWithCounter) >= (knownForTitles.length / 2)) {
                typeCasted.setTypeCasted(true);
            } else {
                typeCasted.setTypeCasted(false);
            }

        });
        ResponseEntity<TypeCasted> responseEntity = null;
        if (typeCasted.getNameOfActor() != null) {
            responseEntity = new ResponseEntity<>(typeCasted, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @Cacheable("names")
    public List<String> getActorsNames() {
        Dataset<Row> actorNameRow = dataFrameForNameBasicsPrimaryNameAndKnowForTitles
                .select("knownForTitles");
        List<String> names = new ArrayList<>();

        actorNameRow.foreach(row -> {
            names.add(row.getString(0));
        });
        return names;
    }


    private Integer getMaximumGenre(HashMap<String, Integer> map) {
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry.getValue();
    }

    //using join
    @Cacheable("typeCastedJoin")
    public ResponseEntity<TypeCasted> getTypeCasting(String nameOfActor) {
        List<TypeCastingInformation> typeCastingInformations = new ArrayList<>();
        dataFrameForTitleBasics.join(nameBasics, nameBasics.col("knownForTitles").contains(dataFrameForTitleBasics.col("tconst")), "inner")
                .where(functions.col("primaryName").equalTo(nameOfActor))
                .select("tconst", "genres", "primaryName", "knownForTitles")
                .toLocalIterator()
                .forEachRemaining(row -> {
                    typeCastingInformations.add(new TypeCastingInformation(row.getString(0),
                            row.getString(1), row.getString(2), row.getString(3)));
                });
        int numberOfWorks = typeCastingInformations.size();
        TypeCasted typeCasted = new TypeCasted();
        HashMap<String, Integer> mapOfGenresWithCounter = new HashMap<>();
        for (TypeCastingInformation typeCastingInformation :
                typeCastingInformations) {
            typeCasted.setNameOfActor(typeCastingInformation.getPrimaryName());
            for (String genre : typeCastingInformation.getGenres().split(",")) {
                if (mapOfGenresWithCounter.containsKey(genre)) {
                    mapOfGenresWithCounter.put(genre, mapOfGenresWithCounter.get(genre) + 1);
                } else {
                    mapOfGenresWithCounter.put(genre, 1);
                }
            }
        }
        if (getMaximumGenre(mapOfGenresWithCounter) >= (numberOfWorks / 2)) {
            typeCasted.setTypeCasted(true);
        } else {
            typeCasted.setTypeCasted(false);
        }

        ResponseEntity<TypeCasted> responseEntity = null;
        if (typeCasted.getNameOfActor() != null) {
            responseEntity = new ResponseEntity<>(typeCasted, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }


    public void sixDegreeSeparation(String nameOfActor) {

        for (int i = 0; i < 6; i++) {
            titles.forEach(title -> {


            });
        }
    }

}
