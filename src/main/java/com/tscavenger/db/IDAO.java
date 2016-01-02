package com.tscavenger.db;

import java.sql.SQLException;

public interface IDAO {
    Website getWebsiteByName(String website) throws SQLException;

    int addWebsite(String website) throws SQLException;

    int updateWebsiteWithStatus(String website, Status status, String detail, String url) throws SQLException;
}
