package com.tscavenger.core;

import com.tscavenger.data.ScavengerData;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public interface IVisitor {

    void visit(Page page, HtmlParseData htmlParseData, ScavengerData data, IVisitDecider visitDecider);

}
