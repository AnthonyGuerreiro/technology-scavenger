package com.tscavenger.core.match;

public enum MatchLocation {
    HTML("HTML"), HEADER("header");

    private String value;

    private MatchLocation(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
