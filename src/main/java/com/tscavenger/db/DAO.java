package com.tscavenger.db;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class DAO {

    private final static Logger logger = LogManager.getInstance(DAO.class);

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // should never happen
            logger.error(e);
        }
    }

    private String getConnectionUrl() {
        // TODO get from property file
        URL url = getClass().getResource("/db/scavengerdb.db");
        return "jdbc:sqlite:" + url.getPath();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionUrl());
    }

    public int addWebsite(String website) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = getStatement(connection, new QueryFactory().getAddWebsiteQuery());
            addParam(statement, 1, website);
            int added = statement.executeUpdate();
            close(statement);
            return added;
        } finally {
            close(connection);
        }
    }

    public int updateWebsiteWithStatus(String website, Status status) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = getStatement(connection,
                    new QueryFactory().getUpdateWebsiteStatusQuery());
            addParam(statement, 1, status.value());
            addParam(statement, 2, website);
            int updated = statement.executeUpdate();
            close(statement);
            return updated;
        } finally {
            close(connection);
        }
    }

    private void addParam(PreparedStatement statement, int index, int value) throws SQLException {
        statement.setInt(index, value);
    }

    private void addParam(PreparedStatement statement, int index, String value) throws SQLException {
        statement.setString(index, value);
    }

    private PreparedStatement getStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    private void close(PreparedStatement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            // nop
        }
    }

    private void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // nop
        }
    }

}
