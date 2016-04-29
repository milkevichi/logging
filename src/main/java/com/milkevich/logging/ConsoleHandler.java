package com.milkevich.logging;

import java.text.Normalizer;
import java.text.SimpleDateFormat;

/**
 * @author IMilkevich on 23/04/16.
 */
public class ConsoleHandler extends Handler {
    LogManager logManager = LogManager.getLogManager();

    public ConsoleHandler() {
       setFormatter(logManager.getFormatterProperty(getClass().getName() + ".formatter", new TextFormatter()));
    }

    @Override
    public void writeMessage(Message msg) {

        Formatter formatter = getFormatter();
        String text = formatter.format(msg);

        System.err.println(text);
    }
}
