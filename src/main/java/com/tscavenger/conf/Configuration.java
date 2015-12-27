package com.tscavenger.conf;

import com.tscavenger.core.IVisitDecider;
import com.tscavenger.core.IVisitor;

public class Configuration {

    private final static Configuration configuration = new Configuration();

    private IVisitor visitor;
    private IVisitDecider visitDecider;

    private Configuration() {
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

}
