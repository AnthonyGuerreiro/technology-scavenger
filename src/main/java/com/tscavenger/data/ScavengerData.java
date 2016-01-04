package com.tscavenger.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tscavenger.core.match.WebsiteMatchDetails;

import edu.uci.ics.crawler4j.crawler.Page;

public class ScavengerData {

    private Set<String> pages = new HashSet<>();
    private Map<String, WebsiteMatchDetails> detailMap = new HashMap<>();
    private Map<String, String> urlMap = new HashMap<>();

    public void addPage(Page page, WebsiteMatchDetails details) {
        String domain = page.getWebURL().getDomain();
        pages.add(domain);
        detailMap.put(domain, details);
        urlMap.put(domain, page.getWebURL().getURL());
    }

    public Set<String> getPages() {
        return pages;
    }

    public WebsiteMatchDetails getDetail(String domain) {
        return detailMap.get(domain);
    }

    public String getUrl(String domain) {
        return urlMap.get(domain);
    }

}
