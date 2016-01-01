package com.tscavenger.core;

import com.tscavenger.conf.Configuration;
import com.tscavenger.data.ScavengerData;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Scavenger extends WebCrawler implements IScavenger {

    private final static Logger logger = LogManager.getInstance(Scavenger.class);

    private ScavengerData data = new ScavengerData();

    private IVisitDecider visitDecider;
    private IVisitor visitor;
    private String parentThread;

    public Scavenger() {
        Configuration configuration = Configuration.getInstance();
        visitDecider = configuration.getVisitDecider();
        visitor = configuration.getVisitor();
        parentThread = Thread.currentThread().getName();
    }

    /**
     * This method receives two parameters. The first parameter is the page in
     * which we have discovered this new url and the second parameter is the new
     * url. You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic). In this example,
     * we are instructing the crawler to ignore urls that have css, js, git, ...
     * extensions and to only accept urls that start with
     * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
     * parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        return visitDecider == null ? true : visitDecider.shouldVisit(referringPage, url);
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.trace(Thread.currentThread().getName() + " URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            if (visitor != null) {
                visitor.visit(page, htmlParseData, data, visitDecider, parentThread);
            }
        }
    }

    /**
     * This function is called by controller to get the local data of this
     * crawler when job is finished
     */
    @Override
    public Object getMyLocalData() {
        return data;
    }
}
