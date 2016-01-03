package com.tscavenger.db;

public class DAOFactory {
    private IDAO dao = new DAO();

    public IDAO getDAO() {
        return dao;
    }

}
