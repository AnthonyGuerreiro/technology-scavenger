package com.tscavenger.core;

import com.tscavenger.data.ScavengerData;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public interface IVisitor {

    /**
     * Visits and processes the {@code page}.<br />
     *
     * @param page
     * @param htmlParseData
     * @param data
     * @param visitDecider
     * @param parentThread
     */
    void visit(Page page, HtmlParseData htmlParseData, ScavengerData data, IURLFollowDecider visitDecider,
            String parentThread);

}
