package com.tscavenger.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.match.WebsiteMatchDetails;
import com.tscavenger.data.ScavengerData;
import com.tscavenger.data.ThreadDataManager;
import com.tscavenger.db.IDAO;
import com.tscavenger.db.Status;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class ScavengerDataCollector implements Runnable {

    private static Logger logger = LogManager.getInstance(ScavengerDataCollector.class);

    private int numberOfCrawlers;
    private String website;
    private String websiteInput;

    public ScavengerDataCollector(int numberOfCrawlers, String website) {
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

    private List<Object> crawlAndGetLocalData() throws Exception {
        CrawlController controller = new ThreadDataManager()
                .getNewCrawlController(Thread.currentThread().getName());

        controller.addSeed(website);
        controller.start(Scavenger.class, numberOfCrawlers);
        controller.shutdown();
        controller.waitUntilFinish();
        return controller.getCrawlersLocalData();
    }

    @Override
    public void run() {
        logger.info("Processing website " + websiteInput);
        List<Object> localDataList = new ArrayList<>();
        WebsiteMatchDetails websiteMatchDetails = null;
        try {
            localDataList = crawlAndGetLocalData();
            websiteMatchDetails = getMatchDetails(localDataList);
        } catch (Exception e) {
            logger.error("Couldn't initialize controller", e);
            websiteMatchDetails = getWebsiteMatchDetailsWithStatus(Status.NOT_PROCESSED);
        }
        updateDB(websiteMatchDetails);
    }

    private WebsiteMatchDetails getMatchDetails(List<Object> localDataList) {

        for (Object localData : localDataList) {
            if (localData instanceof ScavengerData) {
                ScavengerData data = (ScavengerData) localData;
                boolean matched = data.getMatchDetails() != null;

                if (!matched) {
                    continue;
                }
                return data.getMatchDetails();
            }
        }

        return getWebsiteMatchDetailsWithStatus(Status.DOES_NOT_USE_TECHNOLOGY);
    }

    private WebsiteMatchDetails getWebsiteMatchDetailsWithStatus(Status status) {
        WebsiteMatchDetails matchDetails = new WebsiteMatchDetails();
        matchDetails.setStatus(status);
        return matchDetails;
    }

    private void updateDB(WebsiteMatchDetails matchDetails) {
        Status status = matchDetails.getStatus();
        String log = matchDetails.toString();
        String url = matchDetails.getUrl();

        try {
            String msg = "Updating website " + websiteInput + " with status " + status.name() + " in db";
            logger.info(msg);
            int updated = getDAO().updateWebsiteWithStatus(websiteInput, status, log, url);
            if (updated == 0) {
                logger.warn("Failed to update website " + websiteInput + " with status " + status.name()
                        + " in db: no rows were affected");
            }
        } catch (SQLException e) {
            String msg = "Could not update website " + websiteInput + " with status " + status.name()
                    + " in db";
            logger.error(msg, e);
        }
    }

    private IDAO getDAO() {
        return Configuration.getInstance().getDAOFactory().getDAO();
    }
}
