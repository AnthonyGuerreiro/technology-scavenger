package com.tscavenger.db;

import java.sql.SQLException;

import com.tscavenger.core.match.TechnologyMatcher;

public interface IDAO {
    Website getWebsiteByName(String website) throws SQLException;

    int addWebsite(String website) throws SQLException;

    int updateWebsiteWithStatus(String website, Status status, String detail, String url) throws SQLException;

    TechnologyMatcher getTechnologyMatcher(String technology) throws SQLException;
}
