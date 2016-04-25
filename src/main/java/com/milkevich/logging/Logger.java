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

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void log(Level level, String msg) {
        if (level.getIntLevel() > intLevel || this.level == Level.OFF) {
            return;
        }

        Message message = new Message(msg);
        message.setLevel(level);
        message.setLoggerName(name);

        log(message);

    }

    private void log(Message message) {
        if (filter != null && !filter.isMessageLoggable(message)) {
            return;
        }

        for (Handler handler : handlers) {
            handler.writeMessage(message);
        }
    }
}
