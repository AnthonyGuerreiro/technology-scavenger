package com.tscavenger.log;

public class LogManager {
    public static Logger getInstance(Class<?> klass) {
        return new Logger();
    }

}
