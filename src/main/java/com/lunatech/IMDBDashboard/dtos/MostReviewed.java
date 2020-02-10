package com.lunatech.IMDBDashboard.dtos;

public class MostReviewed {


    private Double averageRating;
    private Integer numVotes;
    private String primaryTitle;
    private String originalTitle;
    private String genres;

    public MostReviewed( Double averageRating, Integer numVotes, String primaryTitle, String originalTitle, String genres) {

        this.averageRating = averageRating;
        this.numVotes = numVotes;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.genres = genres;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
