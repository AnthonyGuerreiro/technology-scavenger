package com.tscavenger.core;

import java.util.List;

public interface IScavengerController {

    /**
     * Start the crawl.<br />
     * This is a blocking operation.
     *
     * @param numberOfCrawlers
     */
    void start(int numberOfCrawlers);

    /**
     * Start the crawl.<br />
     * This is a non-blocking operation.
     *
     * @param numberOfCrawlers
     */
    void startNonBlocking(int numberOfCrawlers);

    /**
     * Add the {@code pageUrl} seed as starting point for the crawler.
     *
     * @param pageUrl
     */
    void addSeed(String pageUrl);

    /**
     * Add the {@code technology} to scavenge.
     *
     * @param technology
     */
    void addTechnology(String technology);

    /**
     * Returns the list of websites that use the technologies added with
     * {@link #addTechnology(String)}.
     *
     * @return the list of websites that use the technologies added with
     *         {@link #addTechnology(String)}
     */
    List<String> getWebsitesUsingTechnologies();

}
