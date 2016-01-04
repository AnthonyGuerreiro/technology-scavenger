package com.tscavenger.core;

import java.sql.SQLException;
import java.util.List;

public interface IScavengerController {

    /**
     * Returns the list of websites that use the technologies added with
     * {@link #addTechnology(String)}.
     *
     * @return the list of websites that use the technologies added with
     *         {@link #addTechnology(String)}
     */
    List<String> getWebsitesUsingTechnologies();

    /**
     * Starts the controller
     * 
     * @throws SQLException
     */
    void start() throws SQLException;
}
