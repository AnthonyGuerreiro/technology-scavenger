package com.tscavenger.test;

public class AbstractTest {
    protected String getDefaultCrawlStorageFolder() {
        // TODO return folder based on OS
        return "C:/tmp";
    }

    protected int getDefaultNumberOfCrawlers() {
        return 7;
    }
}
