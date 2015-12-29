package com.tscavenger.core;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tscavenger.conf.Configuration;
import com.tscavenger.data.ScavengerData;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class Visitor implements IVisitor {

    private static Logger logger = LogManager.getInstance(Visitor.class);

    private Pattern pattern;

    public Visitor() {
        List<String> technologies = Configuration.getInstance().getTechnologies();
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = technologies.iterator();
        while (it.hasNext()) {
            sb.append(escapeRegexSpecialCharacters(it.next()));
            if (it.hasNext()) {
                sb.append("|");
            }
        }
        pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    private String escapeRegexSpecialCharacters(String technology) {
        // TODO implement
        return technology;
    }

    @Override
    public void visit(Page page, HtmlParseData htmlParseData, ScavengerData data,
            IVisitDecider visitDecider) {

        if (visitDecider.skipsDomain(page.getWebURL().getDomain())) {
            return;
        }

        Matcher matcher = pattern.matcher(htmlParseData.getHtml());
        if (matcher.find()) {
            visitDecider.stopVisit(page);
            logger.info(page.getWebURL().getDomain() + " uses given technologies");
            addData(page, data);
        }

    }

    private void addData(Page page, ScavengerData data) {
        data.addPage(page);
    }

}
