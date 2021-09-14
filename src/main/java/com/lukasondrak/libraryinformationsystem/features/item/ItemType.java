package com.lukasondrak.libraryinformationsystem.features.item;

public enum ItemType {
    BOOK ("Kniha"),
    CD ("CD"),
    MAGAZINE ("Časopis");

    private final String nameInCzech;

    ItemType(String nameInCzech) {
        this.nameInCzech = nameInCzech;
    }

    public String getNameInCzech() {
        return this.nameInCzech;
    }

}
