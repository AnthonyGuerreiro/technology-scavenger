package com.tscavenger.core;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.match.MatchDetails;
import com.tscavenger.core.match.MatchLocation;
import com.tscavenger.data.ScavengerData;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class Visitor implements IVisitor {

    private static Logger logger = LogManager.getInstance(Visitor.class);

    private Pattern pattern;

    public Visitor() {
        List<String> technologies = Configuration.getInstance().getTechnologies();
        StringBuilder sb = new StringBuilder("(");
        Iterator<String> it = technologies.iterator();
        while (it.hasNext()) {
            sb.append(escapeRegexSpecialCharacters(it.next()));
            if (it.hasNext()) {
                sb.append("|");
            }
        }
        sb.append(")");
        pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    private String escapeRegexSpecialCharacters(String technology) {
        // TODO implement
        return technology;
    }

    @Override
    public void visit(Page page, HtmlParseData htmlParseData, ScavengerData data, IVisitDecider visitDecider,
            String parentThread) {

        if (visitDecider.skipsDomain(page.getWebURL().getDomain())) {
            return;
        }

        MatchDetails details = getMatchDetails(page, htmlParseData);
        boolean matched = details != null;

        if (matched) {
            visitDecider.stopVisit(page);
            addData(page, details, data);
            stopController(parentThread);
        }
    }

    private void stopController(String parentThread) {
        CrawlController controller = new CrawlControllerManager().getExistingCrawlController(parentThread);
        if (controller != null) {
            controller.shutdown();
        }
    }

    private MatchDetails getMatchDetails(Page page, HtmlParseData htmlParseData) {

        MatchDetails details = getHeaderMatch(page, htmlParseData);
        if (details != null) {
            return details;
        }

        return getHTMLMatch(page, htmlParseData);
    }

    private MatchDetails getHTMLMatch(Page page, HtmlParseData htmlParseData) {
        Matcher matcher = pattern.matcher(htmlParseData.getHtml());
        if (!matcher.find()) {
            return null;
        }

        return new MatchDetails(MatchLocation.HTML, matcher.group(1));
    }

    private MatchDetails getHeaderMatch(Page page, HtmlParseData htmlParseData) {

        Header[] headers = page.getFetchResponseHeaders();
        for (Header header : headers) {
            Matcher matcher = pattern.matcher(header.getName());
            if (matcher.find()) {
                return new MatchDetails(MatchLocation.HEADER, matcher.group(1));
            }
        }
        return null;
    }

    private void addData(Page page, MatchDetails details, ScavengerData data) {
        data.addPage(page, details);
    }

}
