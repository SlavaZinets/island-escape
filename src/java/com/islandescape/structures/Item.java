package com.islandescape.structures;

public class Item {
    private String name;
    private String classification;

    public Item(String name, String classification) {
        this.name = name;
        this.classification = classification;
    }
    public String getName() {
        return name;
    }
    public String getClassification() {
        return classification;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}