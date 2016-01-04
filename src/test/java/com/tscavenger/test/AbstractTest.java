package com.tscavenger.test;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.URLFollowDecider;
import com.tscavenger.core.Visitor;

public class AbstractTest {
    protected String getDefaultCrawlStorageFolder() {
        // TODO return folder based on OS
        return "C:/tmp";
    }

    protected int getDefaultNumberOfCrawlers() {
        return 7;
    }

    protected void setup() {
        Configuration configuration = Configuration.getInstance();
        configuration.setVisitDecider(new URLFollowDecider());
        configuration.setVisitor(new Visitor());
    }
}
