package com.lunatech.IMDBDashboard.dtos;

public class TypeCasted {

    private String nameOfActor;
    private boolean isTypeCasted;

    public TypeCasted() {
    }

    public TypeCasted(String nameOfActor, boolean isTypeCasted) {
        this.nameOfActor = nameOfActor;
        this.isTypeCasted = isTypeCasted;
    }

    public String getNameOfActor() {
        return nameOfActor;
    }

    public void setNameOfActor(String nameOfActor) {
        this.nameOfActor = nameOfActor;
    }

    public boolean isTypeCasted() {
        return isTypeCasted;
    }

    public void setTypeCasted(boolean typeCasted) {
        isTypeCasted = typeCasted;
    }
}
