package com.tscavenger.conf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.tscavenger.core.IURLFollowDecider;
import com.tscavenger.core.IVisitor;
import com.tscavenger.db.DAOFactory;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

/**
 * Base configuration class
 *
 */
public class Configuration {

    private final static Configuration configuration = new Configuration();

    private static Logger logger = LogManager.getInstance(Configuration.class);

    private IVisitor visitor;
    private IURLFollowDecider urlFollowDecider;

    private List<String> technologies;
    private List<String> websites;

    private DAOFactory daoFactory = new DAOFactory();

    private Properties properties = new Properties();

    private Configuration() {

        // TODO read file location from properties
        technologies = initList("technologies", "/conf/technologies");
        websites = initList("websites", "/conf/websites");
        initProperties();
    }

    private void initProperties() {
        try {
            properties.load(getClass().getResourceAsStream("/conf/tscavenger.properties"));
        } catch (IOException e) {
            logger.error("Failed to load tscavenger properties", e);
        }
    }

    /**
     *
     * @param name
     *            resource name, used for logging purposes only
     * @param resource
     * @return lines read in the {@code resource}, or empty List if there is a
     *         problem reading the {@code resource}
     */
    private List<String> initList(String name, String resource) {
        try {
            return new ResourceLineReader().getLines(resource);
        } catch (IOException | URISyntaxException e) {
            logger.error("Error reading " + name + " file", e);
            return new ArrayList<>();
        }
    }

    public static Configuration getInstance() {
        return configuration;
    }

    /**
     *
     * @return the {@link IVisitor} to use throughout the application
     */
    public IVisitor getVisitor() {
        return visitor;
    }

    /**
     * Sets the {@link IVisitor} to use throughout the application.
     *
     * @param visitor
     */
    public void setVisitor(IVisitor visitor) {
        this.visitor = visitor;
    }

    /**
     *
     * @return the {@link IURLFollowDecider} to use throughout the application
     */
    public IURLFollowDecider getUrlFollowDecider() {
        return urlFollowDecider;
    }

    /**
     * Sets the {@link IURLFollowDecider} to use throughout the application.
     *
     * @param urlFollowDecider
     */
    public void setUrlFollowDecider(IURLFollowDecider urlFollowDecider) {
        this.urlFollowDecider = urlFollowDecider;
    }

    /**
     *
     * @return technologies listed in file
     */
    public List<String> getTechnologies() {
        return technologies;
    }

    /**
     *
     * @return websites listed in file
     */
    public List<String> getWebsites() {
        return websites;
    }

    /**
     *
     * @return the {@link DAOFactory} to use throughout the application
     */
    public DAOFactory getDAOFactory() {
        return daoFactory;
    }

    /**
     *
     * @return {@link Properties} instance loaded with properties found in
     *         tscavenger.properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param property
     * @param defaultValue
     * @return boolean value of the property with key {@code property}, or
     *         {@code defaultValue} if the {@code property} does not exist
     * 
     * @see #getProperties()
     */
    public boolean getBooleanFromProperty(String property, boolean defaultValue) {
        String value = properties.getProperty(property);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
