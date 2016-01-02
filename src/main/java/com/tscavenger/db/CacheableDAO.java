package com.tscavenger.db;

import java.sql.SQLException;

public class CacheableDAO implements IDAO {

    private IDAO dao;

    public CacheableDAO(IDAO dao) {
        this.dao = dao;
    }

    @Override
    public Website getWebsiteByName(String website) throws SQLException {
        return dao.getWebsiteByName(website); // no cache
    }

    @Override
    public int addWebsite(String website) throws SQLException {
        return dao.addWebsite(website);// no cache
    }

    @Override
    public int updateWebsiteWithStatus(String website, Status status, String detail, String url)
            throws SQLException {

        // no cache
        return dao.updateWebsiteWithStatus(website, status, detail, url);
    }

}
