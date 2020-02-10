package com.lunatech.IMDBDashboard.controllers;

import com.lunatech.IMDBDashboard.dtos.MostReviewed;
import com.lunatech.IMDBDashboard.dtos.TypeCasted;
import com.lunatech.IMDBDashboard.services.IMDBDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/test")
public class IMDBDashboradController {

    @Autowired
    private IMDBDashboardService imdbDashboardService;


    @GetMapping("highRated/{genre}")
    public List<MostReviewed> getRatedFilmsWithGenre(@PathVariable String genre) {

        return imdbDashboardService.getRatedFilmsWithGenre(genre);
    }

    @GetMapping("typeCasted/{nameOfActor}")
    public ResponseEntity<TypeCasted> isActorTypeCasted(@PathVariable String nameOfActor) {
        return imdbDashboardService.isActorTypeCasted(nameOfActor);
    }

    @GetMapping("actorNames")
    public List<String> getActorName() {
        return imdbDashboardService.getActorsNames();
    }

    @GetMapping("degreeSepration")
    public void is6DegreeSeperation() {
        imdbDashboardService.sixDegreeSeparation("");
    }

}


