package com.tscavenger.core;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlControllerManager {
    private static Map<Thread, Integer> folderMap = new HashMap<>();
    private static Map<String, CrawlController> controllerMap = new HashMap<>();
    private static int counter;

    private static String crawlStorageFolder = "C:/tmp/";

    static {
        // TODO read crawlStorageFolder from property file
        if (!crawlStorageFolder.endsWith("/")) {
            crawlStorageFolder += "/";
        }

    }

    public CrawlController getCrawlController() throws Exception {
        int index = getCrawlControllerIndex();
        CrawlController controller = getNewCrawlController(index);
        controllerMap.put(Thread.currentThread().getName(), controller);
        return controller;
    }

    public CrawlController getExistingCrawlController(String threadName) {
        return controllerMap.get(threadName);
    }

    private int getCrawlControllerIndex() {
        Thread thread = Thread.currentThread();
        if (folderMap.containsKey(thread)) {
            return folderMap.get(thread);
        }
        int index = counter++;
        folderMap.put(thread, index);
        return index;
    }

    private CrawlController getNewCrawlController(int index) throws Exception {

        String folder = crawlStorageFolder + index;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(folder);
        config.setMaxDepthOfCrawling(1);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        return new CrawlController(config, pageFetcher, robotstxtServer);
    }
}
