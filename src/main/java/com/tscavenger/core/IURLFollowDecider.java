package com.tscavenger.core;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface IURLFollowDecider {

    /**
     * @param referringPage
     * @param url
     * @return true if the crawler should visit the {@code url} found in the
     *         {@code referringPage}, false otherwise
     */
    boolean shouldVisit(Page referringPage, WebURL url);

    /**
     * Adds the {@code domain} to the list of domains we should not visit
     * anymore
     * 
     * @param domain
     */
    void stopVisit(String domain);

    /**
     *
     * @param domain
     * @return true if the {@code domain} was previously added to the list of
     *         domains we shuold not visit anymore
     * @see #stopVisit(String)
     */
    boolean skipsDomain(String domain);

}
