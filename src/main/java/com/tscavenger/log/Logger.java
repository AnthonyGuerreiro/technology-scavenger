package com.tscavenger.log;

public class Logger {

    public void info(String msg) {
        System.out.println(msg);
    }

    public void error(String msg, Exception e) {
        System.err.println(msg);
        e.printStackTrace();
    }

    public void warn(String msg) {
        System.out.println(msg);
    }

    public void trace(String string) {
        // nop
    }
}
