package com.tscavenger.core;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tscavenger.conf.Configuration;
import com.tscavenger.db.DAO;
import com.tscavenger.db.Status;
import com.tscavenger.db.Website;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class ScavengerController implements IScavengerController {

    private static Logger logger = LogManager.getInstance(ScavengerController.class);

    // TODO read threadpool size from properties
    private ExecutorService threadpool = Executors.newFixedThreadPool(20);

    // TODO read from properties
    private static int NR_CRAWLERS_PER_CONTROLLER = 10;

    @Override
    public void start() throws SQLException {

        Configuration configuration = Configuration.getInstance();
        List<String> websites = configuration.getWebsites();

        DAO dao = new DAO();

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
                threadpool.execute(new ScavengerWorker(NR_CRAWLERS_PER_CONTROLLER, website));
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
