package com.tscavenger.log;

import java.sql.SQLException;

public class Logger {

    private String getMsg(String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return "";
        }
        return Thread.currentThread().getName() + " " + msg;
    }

    public void info(String msg) {
        System.out.println(getMsg(msg));
    }

    public void error(String msg, Exception e) {
        System.err.println(getMsg(msg));
        e.printStackTrace();
    }

    public void warn(String msg) {
        System.out.println(getMsg(msg));
    }

    public void trace(String msg) {
        // nop
    }

    public void error(Exception e) {
        error("", e);
    }

    public void info(String msg, Exception e) {
        System.out.println(getMsg(msg));
        e.printStackTrace();
    }

    public void warn(String msg, SQLException e) {
        System.err.println(msg);
        e.printStackTrace();
    }
}
