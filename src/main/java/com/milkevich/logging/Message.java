package com.milkevich.logging;

/**
 * @author IMilkevich on 23/04/16.
 */
public class Message {

    private String loggerName;
    private Level level;
    private String className;
    private String methodName;
    private String text;
    private long millisDate;
    private Throwable thrownException;

    public Message(String text) {
        this.text = text;
        millisDate = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public long getMillisDate() {
        return millisDate;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Throwable getThrownException() {
        return thrownException;
    }

    public void setThrownException(Throwable thrownException) {
        this.thrownException = thrownException;
    }
}

