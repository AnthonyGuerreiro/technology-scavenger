package com.tscavenger.core.match;

public class TechnologyMatcher {
    private String technology;
    private String headerMatcher;
    private String htmlMatcher;

    /**
     *
     * @return the String to match this particular technology with, if searching
     *         in the header
     */
    public String getHeaderMatcher() {
        return headerMatcher;
    }

    public void setHeaderMatcher(String headerMatcher) {
        this.headerMatcher = headerMatcher;
    }

    /**
     *
     * @return the String to match this particular technology with, if searching
     *         in the HTML
     */
    public String getHtmlMatcher() {
        return htmlMatcher;
    }

    public void setHtmlMatcher(String htmlMatcher) {
        this.htmlMatcher = htmlMatcher;
    }

    /**
     *
     * @return the technology this matcher is binded with
     */
    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
