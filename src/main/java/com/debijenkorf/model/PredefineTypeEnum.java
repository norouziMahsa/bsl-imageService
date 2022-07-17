package com.debijenkorf.model;

public enum PredefineTypeEnum {

    ORIGINAL("original"),
    THUMBNAIL("thumbnail");

    private final String name;

    PredefineTypeEnum(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
