package com.tscavenger.core;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface IVisitDecider {

    boolean shouldVisit(Page referringPage, WebURL url);

    void stopVisit(Page referringPage);

    boolean skipsDomain(String domain);

}
