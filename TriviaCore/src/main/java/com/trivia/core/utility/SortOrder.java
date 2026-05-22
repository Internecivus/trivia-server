package com.trivia.core.utility;



public enum SortOrder {
    ASCENDING("asc"),
    DESCENDING("desc"),
    DEFAULT("default");

    private String sortName;

    SortOrder(String sortName) {
        this.sortName = sortName;
    }

    public String getSortName() {
        return sortName;
    }

    // Used by JAX-RS
    public static SortOrder fromString(String sortName) {
        for (SortOrder sortOrder : SortOrder.values()) {
            if (sortOrder.sortName.equals(sortName)) {
                return sortOrder;
            }
        }
        return null;
    }
}