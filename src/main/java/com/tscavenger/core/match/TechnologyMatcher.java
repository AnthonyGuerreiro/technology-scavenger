package com.tscavenger.core.match;

public class TechnologyMatcher {
    private String technology;
    private String headerMatcher;
    private String htmlMatcher;

    public String getHeaderMatcher() {
        return headerMatcher;
    }

    public void setHeaderMatcher(String headerMatcher) {
        this.headerMatcher = headerMatcher;
    }

    public String getHtmlMatcher() {
        return htmlMatcher;
    }

    public void setHtmlMatcher(String htmlMatcher) {
        this.htmlMatcher = htmlMatcher;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
