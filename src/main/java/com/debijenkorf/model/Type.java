package com.debijenkorf.model;

public enum Type {
    JPG("JPG"),
    PNG("PNG");

    private final String name;

    Type(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
