package com.tscavenger.core.match;

public class MatchDetails {

    private MatchLocation location;
    private String matched;

    public MatchDetails(MatchLocation location, String matched) {
        this.location = location;
        this.matched = matched;
    }

    public String toString() {
        return "Found " + matched + " in " + location.value();
    }

}
