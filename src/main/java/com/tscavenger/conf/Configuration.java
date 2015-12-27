package com.tscavenger.conf;

import java.util.ArrayList;
import java.util.List;

import com.tscavenger.core.IVisitDecider;
import com.tscavenger.core.IVisitor;

public class Configuration {

    private final static Configuration configuration = new Configuration();

    private IVisitor visitor;
    private IVisitDecider visitDecider;

    private List<String> technologies;

    private Configuration() {
        technologies = initTechnologies();
    }

    private List<String> initTechnologies() {
        List<String> technologies = new ArrayList<>();
        // TODO implement
        technologies.add("Shopify");
        return technologies;
    }

    public static Configuration getInstance() {
        return configuration;
    }

    public IVisitor getVisitor() {
        return visitor;
    }

    public void setVisitor(IVisitor visitor) {
        this.visitor = visitor;
    }

    public IVisitDecider getVisitDecider() {
        return visitDecider;
    }

    public void setVisitDecider(IVisitDecider visitDecider) {
        this.visitDecider = visitDecider;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

}
