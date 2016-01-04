package com.tscavenger.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tscavenger.conf.Configuration;
import com.tscavenger.db.IDAO;
import com.tscavenger.db.Status;
import com.tscavenger.entity.Website;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class ScavengerController implements IScavengerController {

    private static Logger logger = LogManager.getInstance(ScavengerController.class);

    private ExecutorService threadpool;
    private int NR_CRAWLERS_PER_CONTROLLER = 10;

    public ScavengerController() {
        Properties properties = Configuration.getInstance().getProperties();
        int threadpoolSize = getThreadpoolSize(properties);
        threadpool = Executors.newFixedThreadPool(threadpoolSize);
        NR_CRAWLERS_PER_CONTROLLER = getNrCrawlersPerController(properties);
    }

    private int getNrCrawlersPerController(Properties properties) {

        String nrCrawlersPerController = properties.getProperty("nr.crawlers.per.controller");

        try {
            int nr = Integer.parseInt(nrCrawlersPerController);
            if (nr > 0) {
                return nr;
            }
            return 10;
        } catch (NumberFormatException e) {
            logger.warn("Invalid nr.crawlers.per.controller value: expected int. Assuming default value");
            return 10;
        }
    }

    private int getThreadpoolSize(Properties properties) {
        String threadpoolSize = properties.getProperty("threadpool.size");

        try {
            int size = Integer.parseInt(threadpoolSize);
            if (size > 0) {
                return size;
            }
            return 20;
        } catch (NumberFormatException e) {
            logger.warn("Invalid threadpool.size value: expected int. Assuming default value");
            return 20;
        }
    }

    @Override
    public void start() throws SQLException {

        Configuration configuration = Configuration.getInstance();
        List<String> websites = configuration.getWebsites();

        IDAO dao = configuration.getDAOFactory().getDAO();

        for (String website : websites) {
            Website site = dao.getWebsiteByName(website);
            if (isPreprocessed(site)) {
                String msg = "Skipping " + website + ": previously processed with status "
                        + site.getStatus().name();
                logger.info(msg);
                continue;
            }
            boolean websiteIsAdded = site != null;
            if (!websiteIsAdded) {
                int nrAddedWebsites = dao.addWebsite(website);
                boolean addedWebsite = nrAddedWebsites != 0;
            }

            try {
                threadpool.execute(new ScavengerDataCollector(NR_CRAWLERS_PER_CONTROLLER, website));
            } catch (Exception e) {
                logger.info("Failed to process website " + website, e);
            }
        }
    }

    private boolean isPreprocessed(Website website) {
        return website != null && website.getStatus() != Status.NOT_PROCESSED;
    }

    @Override
    public List<String> getWebsitesUsingTechnologies() {
        // TODO implement
        return null;
    }

}
