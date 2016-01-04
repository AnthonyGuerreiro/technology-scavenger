package com.tscavenger.core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class URLFollowDecider implements IURLFollowDecider {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    private Set<String> skippedDomains = new HashSet<>();

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String domain = referringPage.getWebURL().getDomain();
        if (skippedDomains.contains(domain)) {
            return false;
        }
        String href = url.getURL().toLowerCase();
        return hasSameDomain(domain, url.getDomain()) && !FILTERS.matcher(href).matches();
    }

    private boolean hasSameDomain(String domain1, String domain2) {
        return domain1.equalsIgnoreCase(domain2);
    }

    @Override
    public void stopVisit(String domain) {
        skippedDomains.add(domain);
    }

    @Override
    public boolean skipsDomain(String domain) {
        return skippedDomains.contains(domain);
    }

}
