package com.tscavenger.db;

public class DAOFactory {
    private IDAO dao = new DAO();
    private IDAO cacheableDAO = new CacheableDAO(dao);

    public IDAO getDAO() {
        return dao;
    }

    public IDAO getCacheableDAO() {
        return cacheableDAO;
    }

}
