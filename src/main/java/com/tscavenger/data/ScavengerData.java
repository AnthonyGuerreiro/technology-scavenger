package com.tscavenger.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.Page;

public class ScavengerData {

    private Set<String> pages = new HashSet<>();
    private Map<String, String> detailMap = new HashMap<>();
    private Map<String, String> urlMap = new HashMap<>();

    public void addPage(Page page, String detail) {
        String domain = page.getWebURL().getDomain();
        pages.add(domain);
        detailMap.put(domain, detail);
        urlMap.put(domain, page.getWebURL().getURL());
    }

    public Set<String> getPages() {
        return pages;
    }

    public String getDetail(String domain) {
        return detailMap.get(domain);
    }

    public String getUrl(String domain) {
        return urlMap.get(domain);
    }

}
