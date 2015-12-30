package com.tscavenger.db;

public class QueryFactory {

    private final static String TABLE_WEBSITE = "website";

    public String getAddWebsiteQuery() {
        return "INSERT INTO " + TABLE_WEBSITE + " (name) VALUES (?)";
    }

    public String getUpdateWebsiteStatusQuery() {
        return "UPDATE " + TABLE_WEBSITE + " set status = ? WHERE name = ?";
    }
}
