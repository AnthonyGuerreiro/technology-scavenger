package com.tscavenger.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tscavenger.conf.Configuration;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class ThreadDataManager {

    private static Map<String, Integer> folderMap = new HashMap<>();

    // TODO clear values from controller map as they stop being used
    private static Map<String, CrawlController> controllerMap = new HashMap<>();
    private static int counter;

    private static String crawlStorageFolder = "C:/tmp/";

    static {

        Properties properties = Configuration.getInstance().getProperties();
        String crawlStorageFolder = properties.getProperty("crawl.storage.folder");
        if (crawlStorageFolder != null) {
            ThreadDataManager.crawlStorageFolder = crawlStorageFolder;
        }

        if (!crawlStorageFolder.endsWith("/")) {
            crawlStorageFolder += "/";
        }

    }

    public CrawlController getNewCrawlController(String threadName) throws Exception {
        int index = getCrawlControllerIndex(threadName);
        CrawlController controller = getNewCrawlController(index);
        controllerMap.put(Thread.currentThread().getName(), controller);
        return controller;
    }

    public CrawlController getExistingCrawlController(String threadName) {
        return controllerMap.get(threadName);
    }

    private int getCrawlControllerIndex(String threadName) {
        if (folderMap.containsKey(threadName)) {
            return folderMap.get(threadName);
        }
        int index = counter++;
        folderMap.put(threadName, index);
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
