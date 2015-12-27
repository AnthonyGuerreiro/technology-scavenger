package com.tscavenger.data;

import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.Page;

public class ScavengerData {

    private Set<String> pages = new HashSet<>();

    public void addPage(Page page) {
        pages.add(page.getWebURL().getDomain());
    }

    public Set<String> getPages() {
        return pages;
    }

}
