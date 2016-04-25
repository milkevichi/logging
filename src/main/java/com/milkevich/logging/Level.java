package com.milkevich.logging;

/**
 * @author IMilkevich on 22/04/16.
 */
public enum Level {
    OFF(0),
    FATAL(100),
    ERROR(200),
    WARN(300),
    INFO(400),
    DEBUG(500),
    TRACE(600),
    ALL(Integer.MAX_VALUE);

    private int intLevel;

    private Level(int i) {
        intLevel = i;
    }

    public int getIntLevel() {
        return intLevel;
    }
}
