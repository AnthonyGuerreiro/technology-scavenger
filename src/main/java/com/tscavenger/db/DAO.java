package com.tscavenger.db;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class DAO {

    private final static Logger logger = LogManager.getInstance(DAO.class);
    private Connection connection;

    private boolean reuseConnection = true;

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

        if (!reuseConnection) {
            return DriverManager.getConnection(getConnectionUrl());
        }

        if (connection == null) {
            synchronized (DAO.class) {
                if (connection == null) {
                    connection = DriverManager.getConnection(getConnectionUrl());
                }
            }
        }
        return connection;
    }

    public Website getWebsiteByName(String website) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = getStatement(connection, new QueryFactory().getWebsiteByName());
            addParam(statement, 1, website);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Website site = new Website(rs.getString("name"), Status.fromValue(rs.getInt("status")));
            return site;
        } finally {
            close(connection);
        }
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

    public int updateWebsiteWithStatus(String website, Status status, String detail, String url)
            throws SQLException {

        Connection connection = getConnection();
        try {
            PreparedStatement statement = getStatement(connection,
                    new QueryFactory().getUpdateWebsiteStatusQuery());
            addParam(statement, 1, status.value());
            addParam(statement, 2, detail);
            addParam(statement, 3, url);
            addParam(statement, 4, website);
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

        if (!reuseConnection) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                // nop
            }
        }
    }
}
