package com.tscavenger.core;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class VisitDecider implements IVisitDecider {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && hasSameDomain(referringPage.getWebURL(), url);
    }

    private boolean hasSameDomain(WebURL url1, WebURL url2) {
        return url1.getDomain().equalsIgnoreCase(url2.getDomain());
    }

}
