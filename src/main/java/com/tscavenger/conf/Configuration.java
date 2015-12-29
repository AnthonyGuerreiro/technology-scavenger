package com.tscavenger.conf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.tscavenger.core.IVisitDecider;
import com.tscavenger.core.IVisitor;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class Configuration {

    private final static Configuration configuration = new Configuration();

    private static Logger logger = LogManager.getInstance(Configuration.class);

    private IVisitor visitor;
    private IVisitDecider visitDecider;

    private List<String> technologies;
    private List<String> websites;

    private Configuration() {
        technologies = initList("technologies", "/conf/technologies");
        websites = initList("websites", "/conf/websites");
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
            return new LineResourceReader().getLines(resource);
        } catch (IOException | URISyntaxException e) {
            logger.error("Error reading " + name + " file", e);
            return new ArrayList<>();
        }
    }

    public static Configuration getInstance() {
        return configuration;
    }

    public IVisitor getVisitor() {
        return visitor;
    }

    public void setVisitor(IVisitor visitor) {
        this.visitor = visitor;
    }

    public IVisitDecider getVisitDecider() {
        return visitDecider;
    }

    public void setVisitDecider(IVisitDecider visitDecider) {
        this.visitDecider = visitDecider;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public List<String> getWebsites() {
        return websites;
    }

}
