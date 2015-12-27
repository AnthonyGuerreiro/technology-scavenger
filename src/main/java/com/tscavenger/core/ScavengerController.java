package com.tscavenger.core;

import java.util.ArrayList;
import java.util.List;

import com.tscavenger.data.ScavengerData;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class ScavengerController implements IScavengerController {

    private CrawlController controller;
    private List<String> websitesUsingTechnologies;

    public ScavengerController(String crawlStorageFolder) throws Exception {

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(1);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        controller = new CrawlController(config, pageFetcher, robotstxtServer);
    }

    @Override
    public void addSeed(String pageUrl) {
        if (!pageUrl.startsWith("http://")) {
            pageUrl = "http://" + pageUrl;
        }
        controller.addSeed(pageUrl);
    }

    @Override
    public void start(int numberOfCrawlers) {
        controller.start(Scavenger.class, numberOfCrawlers);
    }

    @Override
    public void startNonBlocking(int numberOfCrawlers) {
        controller.startNonBlocking(Scavenger.class, numberOfCrawlers);
    }

    @Override
    public List<String> getWebsitesUsingTechnologies() {
        if (websitesUsingTechnologies == null) {
            websitesUsingTechnologies = new ArrayList<>();
            List<Object> dataList = controller.getCrawlersLocalData();

            for (Object data : dataList) {
                if (data instanceof ScavengerData) {
                    websitesUsingTechnologies.addAll(((ScavengerData) data).getPages());
                }
            }
        }

        return websitesUsingTechnologies;
    }

}
