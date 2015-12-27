package com.tscavenger.core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class VisitDecider implements IVisitDecider {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    private Set<Page> skippedPages = new HashSet<>();

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        if (skippedPages.contains(referringPage)) {
            return false;
        }
        String href = url.getURL().toLowerCase();
        return hasSameDomain(referringPage.getWebURL(), url) && !FILTERS.matcher(href).matches();
    }

    private boolean hasSameDomain(WebURL url1, WebURL url2) {
        return url1.getDomain().equalsIgnoreCase(url2.getDomain());
    }

    @Override
    public void stopVisit(Page referringPage) {
        skippedPages.add(referringPage);
    }

}
