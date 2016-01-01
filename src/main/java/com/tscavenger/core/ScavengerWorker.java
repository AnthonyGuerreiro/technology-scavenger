package com.tscavenger.core;

import java.sql.SQLException;
import java.util.List;

import com.tscavenger.data.ScavengerData;
import com.tscavenger.db.DAO;
import com.tscavenger.db.Status;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class ScavengerWorker implements Runnable {

    private static Logger logger = LogManager.getInstance(ScavengerWorker.class);

    private int numberOfCrawlers;
    private String website;
    private String websiteInput;

    public ScavengerWorker(int numberOfCrawlers, String website) {
        this.numberOfCrawlers = numberOfCrawlers;
        websiteInput = website;
        this.website = getWebsite(website);
    }

    private String getWebsite(String website) {
        if (website == null) {
            return "http://google.com";
        }
        if (!website.startsWith("http://")) {
            return "http://" + website;
        }
        return website;
    }

    @Override
    public void run() {
        logger.info("Processing website " + websiteInput);
        Status newStatus = Status.DOES_NOT_USE_TECHNOLOGY;
        try {
            CrawlController controller = new CrawlControllerManager().getCrawlController();
            controller.addSeed(website);
            controller.start(Scavenger.class, numberOfCrawlers);
            controller.shutdown();
            controller.waitUntilFinish();

            List<Object> localDataList = controller.getCrawlersLocalData();

            for (Object localData : localDataList) {
                if (localData instanceof ScavengerData) {
                    ScavengerData data = (ScavengerData) localData;
                    if (!data.getPages().isEmpty()) {
                        newStatus = Status.USES_TECHNOLOGY;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Couldn't initialize controller", e);
            newStatus = Status.NOT_PROCESSED;
        }
        try {
            String msg = "Updating website " + websiteInput + " with status " + newStatus.name() + " in db";
            logger.info(msg);
            int updated = new DAO().updateWebsiteWithStatus(websiteInput, newStatus);
        } catch (SQLException e) {
            String msg = "Could not update website " + websiteInput + " with status " + newStatus.name()
                    + " in db";
            logger.error(msg, e);
        }
    }
}