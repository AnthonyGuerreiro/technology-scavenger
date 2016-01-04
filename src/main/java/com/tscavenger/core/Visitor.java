package com.tscavenger.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.match.TechnologyMatcher;
import com.tscavenger.core.match.WebsiteMatchDetails;
import com.tscavenger.core.match.WebsiteMatchLocation;
import com.tscavenger.data.ScavengerData;
import com.tscavenger.data.ThreadDataManager;
import com.tscavenger.db.IDAO;
import com.tscavenger.db.Status;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class Visitor implements IVisitor {

    private static Logger logger = LogManager.getInstance(Visitor.class);

    private Pattern htmlPattern;
    private Pattern headerPattern;
    private boolean matchHeader;
    private boolean matchHTML = true;

    public Visitor() {
        Configuration configuration = Configuration.getInstance();

        initGlobalMatchConfig(configuration);
        initMatcherPatterns(configuration);
    }

    private void initGlobalMatchConfig(Configuration configuration) {
        matchHeader = configuration.getBooleanFromProperty("global.match.header", false);
        matchHTML = configuration.getBooleanFromProperty("global.match.html", true);

    }

    private void initMatcherPatterns(Configuration configuration) {
        List<String> technologies = configuration.getTechnologies();
        List<TechnologyMatcher> technologyMatchers = getTechnologyMatchers(configuration, technologies);
        htmlPattern = getPattern(technologyMatchers, WebsiteMatchLocation.HTML);
        headerPattern = getPattern(technologyMatchers, WebsiteMatchLocation.HEADER);
    }

    private List<TechnologyMatcher> getTechnologyMatchers(Configuration configuration,
            List<String> technologies) {
        List<TechnologyMatcher> technologyMatchers = new ArrayList<>(technologies.size());
        IDAO dao = configuration.getDAOFactory().getDAO();

        try {
            for (String technology : technologies) {
                TechnologyMatcher matcher = dao.getTechnologyMatcher(technology);
                technologyMatchers.add(matcher);
            }
        } catch (SQLException e) {
            logger.warn("Could not obtain technology matchers from DB", e);
        }

        return technologyMatchers;
    }

    private Pattern getPattern(List<TechnologyMatcher> technologyMatchers, WebsiteMatchLocation location) {
        StringBuilder sb = new StringBuilder("(");
        Iterator<TechnologyMatcher> it = technologyMatchers.iterator();
        while (it.hasNext()) {
            TechnologyMatcher matcher = it.next();
            String matcherString = getMatcherString(matcher, location);
            if (matcherString != null) {
                sb.append(escapeRegexSpecialCharacters(matcherString));
                if (it.hasNext()) {
                    sb.append("|");
                }
            }
        }
        if (sb.charAt(1) == '|') {
            sb.replace(1, 2, "");
        }
        if (sb.charAt(sb.length() - 1) == '|') {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        sb.append(")");
        return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    private String getMatcherString(TechnologyMatcher matcher, WebsiteMatchLocation location) {

        String matcherString = null;

        switch (location) {
            case HEADER:
                matcherString = matcher.getHeaderMatcher();
                break;
            case HTML:
                matcherString = matcher.getHtmlMatcher();
                break;
            default:
                logger.warn("Unknown MatchLocation: " + location.value() + ". Using default matcher.");
                return matcher.getTechnology();
        }

        if (matcherString != null) {
            return matcherString;
        }
        switch (location) {
            case HEADER:
                if (matchHeader) {
                    return matcher.getTechnology();
                }
            case HTML:
                if (matchHTML) {
                    return matcher.getTechnology();
                }
        }
        return null;
    }

    private String escapeRegexSpecialCharacters(String technology) {
        // TODO implement
        return technology;
    }

    @Override
    public void visit(Page page, HtmlParseData htmlParseData, ScavengerData data,
            IURLFollowDecider visitDecider, String parentThread) {

        if (visitDecider.skipsDomain(page.getWebURL().getDomain())) {
            return;
        }

        WebsiteMatchDetails matchDetails = getMatchDetails(page, htmlParseData);
        boolean matched = matchDetails.getStatus() == Status.USES_TECHNOLOGY;

        if (matched) {
            visitDecider.stopVisit(page.getWebURL().getDomain());
            data.setMatchDetails(matchDetails);
            stopController(parentThread);
        }
    }

    private void stopController(String parentThread) {
        CrawlController controller = new ThreadDataManager().getExistingCrawlController(parentThread);
        if (controller != null) {
            controller.shutdown();
        }
    }

    private WebsiteMatchDetails getMatchDetails(Page page, HtmlParseData htmlParseData) {

        WebsiteMatchDetails details = new WebsiteMatchDetails();

        if (matchHeader) {
            details = getHeaderMatch(page, htmlParseData);
            if (details.getStatus() != Status.DOES_NOT_USE_TECHNOLOGY) {
                return details;
            }
        }

        if (matchHTML) {
            details = getHTMLMatch(page, htmlParseData);
        }

        return details;
    }

    private WebsiteMatchDetails getHTMLMatch(Page page, HtmlParseData htmlParseData) {
        Matcher matcher = htmlPattern.matcher(htmlParseData.getHtml());
        if (!matcher.find()) {
            return new WebsiteMatchDetails();
        }

        return getWebsiteMatchDetails(WebsiteMatchLocation.HTML, matcher.group(1), page);
    }

    private WebsiteMatchDetails getHeaderMatch(Page page, HtmlParseData htmlParseData) {

        Header[] headers = page.getFetchResponseHeaders();
        for (Header header : headers) {
            Matcher matcher = headerPattern.matcher(header.getName());
            if (matcher.find()) {
                return getWebsiteMatchDetails(WebsiteMatchLocation.HEADER, matcher.group(1), page);
            }
        }
        return new WebsiteMatchDetails();
    }

    private WebsiteMatchDetails getWebsiteMatchDetails(WebsiteMatchLocation location, String matched,
            Page page) {

        WebsiteMatchDetails matchDetails = new WebsiteMatchDetails();
        matchDetails.setLocation(location);
        matchDetails.setMatched(matched);
        matchDetails.setStatus(Status.USES_TECHNOLOGY);
        matchDetails.setUrl(page.getWebURL().getURL());
        return matchDetails;
    }

}
