package com.tscavenger.core.match;

import com.tscavenger.db.Status;

public class WebsiteMatchDetails {

    private WebsiteMatchLocation location;
    private String matched;

    private Status status = Status.DOES_NOT_USE_TECHNOLOGY;
    private String url;

    public WebsiteMatchLocation getLocation() {
        return location;
    }

    public void setLocation(WebsiteMatchLocation location) {
        this.location = location;
    }

    public String getMatched() {
        return matched;
    }

    public void setMatched(String matched) {
        this.matched = matched;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        if (status == Status.USES_TECHNOLOGY) {
            return "Found " + matched + " in " + location.value();
        }
        return null;
    }

}
