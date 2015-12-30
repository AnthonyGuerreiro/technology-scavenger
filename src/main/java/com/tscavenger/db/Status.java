package com.tscavenger.db;

public enum Status {
    NOT_PROCESSED(0), USES_TECHNOLOGY(1), DOES_NOT_USE_TECHNOLOGY(2), CANNOT_PROCESS(3);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}