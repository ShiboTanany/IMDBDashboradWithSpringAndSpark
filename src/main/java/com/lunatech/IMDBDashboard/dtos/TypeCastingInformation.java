package com.lunatech.IMDBDashboard.dtos;

public class TypeCastingInformation {

    private String tconst;
    private String genres;
    private String primaryName;
    private String knownForTitles;

    public TypeCastingInformation() {
    }

    public TypeCastingInformation(String tconst, String genres, String primaryName, String knownForTitles) {
        this.tconst = tconst;
        this.genres = genres;
        this.primaryName = primaryName;
        this.knownForTitles = knownForTitles;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getKnownForTitles() {
        return knownForTitles;
    }

    public void setKnownForTitles(String knownForTitles) {
        this.knownForTitles = knownForTitles;
    }
}
