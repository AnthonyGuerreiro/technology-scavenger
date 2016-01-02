package com.tscavenger.db;

public class DAOFactory {
    private DAO dao = new DAO();

    public DAO getDAO() {
        return dao;
    }

}
