package com.tscavenger.core;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tscavenger.conf.Configuration;
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

        Matcher matcher = pattern.matcher(htmlParseData.getHtml());
        if (matcher.find()) {
            visitDecider.stopVisit(page);
            logger.info(page.getWebURL().getDomain() + " uses given technologies");
            String detail = "Found " + matcher.group(1) + " in HTML";
            addData(page, detail, data);

            CrawlController controller = new CrawlControllerManager()
                    .getExistingCrawlController(parentThread);
            if (controller != null) {
                controller.shutdown();
            }
        }

    }

    private void addData(Page page, String detail, ScavengerData data) {
        data.addPage(page, detail);
    }

}
