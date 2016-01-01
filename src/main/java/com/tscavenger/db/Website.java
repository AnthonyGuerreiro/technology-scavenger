package com.tscavenger.db;

public class Website {
    private String name;
    private Status status;

    public Website(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

}
