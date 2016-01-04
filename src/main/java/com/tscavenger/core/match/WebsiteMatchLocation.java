package com.tscavenger.core.match;

public enum WebsiteMatchLocation {
    HTML("HTML"), HEADER("header");

    private String value;

    private WebsiteMatchLocation(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
