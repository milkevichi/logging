package com.milkevich.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IMilkevich on 22/04/16.
 */
public class Logger {

    private String name;
    private Level level;
    private List<Handler> handlers = new ArrayList<>();
    private int intLevel;
    private Filter filter;
    private Logger parent;

    Logger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        intLevel = level.getIntLevel();
    }

    public Logger getParent() {
        return parent;
    }

    public void setParent(Logger parent) {
        this.parent = parent;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    public void log(Level level, String msg) {
        if (level.getIntLevel() > intLevel || this.level == Level.OFF || level == Level.OFF) {
            return;
        }

        Message message = new Message(msg);
        message.setLevel(level);
        message.setLoggerName(name);

        Logger currentLogger = this;
        while (currentLogger != null) {
            currentLogger.log(message);
            currentLogger = currentLogger.getParent();
            if (currentLogger != null) {
                message.setLoggerName(currentLogger.getName());
            }
        }
    }

    public void log(Level level, String msg, Throwable thrown) {
        if (level.getIntLevel() > intLevel || this.level == Level.OFF || level == Level.OFF) {
            return;
        }

        Message message = new Message(msg);
        message.setLevel(level);
        message.setLoggerName(name);
        message.setThrownException(thrown);

        Logger currentLogger = this;
        while (currentLogger != null) {
            currentLogger.log(message);
            currentLogger = currentLogger.getParent();
            if (currentLogger != null) {
                message.setLoggerName(currentLogger.getName());
            }
        }
    }

    public void log(Level level, String msg, Object... parameters) {
        if (level.getIntLevel() > intLevel || this.level == Level.OFF || level == Level.OFF) {
            return;
        }

        Message message = new Message(msg);
        message.setLevel(level);
        message.setLoggerName(name);
        message.setParameters(parameters);

        Logger currentLogger = this;
        while (currentLogger != null) {
            currentLogger.log(message);
            currentLogger = currentLogger.getParent();
            if (currentLogger != null) {
                message.setLoggerName(currentLogger.getName());
            }
        }
    }

    private void log(Message message) {
        Level level = message.getLevel();
        if (level.getIntLevel() > intLevel || this.level == Level.OFF || level == Level.OFF) {
            return;
        }

        if (filter != null && !filter.isMessageLoggable(message)) {
            return;
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stackTraceElements[3];

        message.setClassName(ste.getClassName());
        message.setMethodName(ste.getMethodName());

        for (Handler handler : handlers) {
            handler.writeMessage(message);
        }

    }

    public void fatal(String msg) {
        if (Level.FATAL.getIntLevel() > intLevel) {
            return;
        }

        log(Level.FATAL, msg);
    }

    public void error(String msg) {
        if (Level.ERROR.getIntLevel() > intLevel) {
            return;
        }

        log(Level.ERROR, msg);
    }

    public void warn(String msg) {
        if (Level.WARN.getIntLevel() > intLevel) {
            return;
        }

        log(Level.WARN, msg);
    }

    public void info(String msg) {
        if (Level.INFO.getIntLevel() > intLevel) {
            return;
        }

        log(Level.INFO, msg);
    }

    public void debug(String msg) {
        if (Level.DEBUG.getIntLevel() > intLevel) {
            return;
        }

        log(Level.DEBUG, msg);
    }

    public void trace(String msg) {
        if (Level.TRACE.getIntLevel() > intLevel) {
            return;
        }

        log(Level.TRACE, msg);
    }

}
