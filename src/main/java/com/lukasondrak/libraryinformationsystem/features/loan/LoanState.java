package com.lukasondrak.libraryinformationsystem.features.loan;

public enum LoanState {
    NOT_YET_RETURNED ("Půjčeno"),
    RETURNED_ON_TIME ("Vráceno včas"),
    RETURNED_LATE ("Vráceno po termínu");

    private final String stateInCzech;

    LoanState(String stateInCzech) {
        this.stateInCzech = stateInCzech;
    }

    public String toString() {
        return this.stateInCzech;
    }

}
