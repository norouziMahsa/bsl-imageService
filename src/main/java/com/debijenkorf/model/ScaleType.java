package com.debijenkorf.model;

public enum ScaleType {
    CROP("Crop"),
    FILL("Fill"),
    SKEW("Skew");

    private final String name;

    ScaleType(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
