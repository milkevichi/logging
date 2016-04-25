package com.milkevich.logging;

import com.sun.org.apache.bcel.internal.generic.LoadClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author IMilkevich on 22/04/16.
 */
public class LogManager {

    private static LogManager logManager;
    private static final Level defaultLevel = Level.INFO;
    private static Map<String, Logger> loggers = new HashMap<>();
    private RootLogger rootLogger;
    private Properties properties = new Properties();

    static {

        logManager = new LogManager();

        logManager.loadProperties();

        logManager.rootLogger = logManager.new RootLogger();

        Logger rootLogger = logManager.rootLogger;

        String nameRootLogger = rootLogger.getName();

        logManager.rootLogger.setLevel(logManager.getLevelProperty(nameRootLogger + ".level", defaultLevel));
        logManager.loadHandlers(rootLogger, nameRootLogger + ".handlers");

        addLogger(logManager.rootLogger);

    }

    private LogManager() {
    }

    public static Logger getLogger() {
        return getLogger("RootLogger");
    }

    public static Logger getLogger(String name) {
        Logger currentLogger = loggers.get(name);
        if (currentLogger == null) {
            currentLogger = new Logger(name);
            currentLogger.setLevel(logManager.getLevelProperty(name + ".level", defaultLevel));
            logManager.loadHandlers(currentLogger, name + ".handlers");
            addLogger(currentLogger);
        }

        return currentLogger;
    }

    private static void addLogger(Logger logger) {
        loggers.put(logger.getName(), logger);
    }

    private class RootLogger extends Logger {

        private RootLogger() {
            super("RootLogger");
            setLevel(defaultLevel);
        }

    }

    public void loadProperties() {

        String filename = "logging.properties";

        try (InputStream inputStream = LogManager.class.getClassLoader().getResourceAsStream(filename)) {

            if (inputStream == null) {
                System.out.println("File " + filename + "isn't found");
                return;
            }

            properties.load(inputStream);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadHandlers(Logger logger, String name) {
        String stringHandlers = getProperty(name);

        if (stringHandlers != null) {
            String[] handlersName = stringHandlers.split("\\s*(,|\\s)\\s*");
            for (int i = 0; i < handlersName.length; i++) {
                try {
                    Class handlerClass = Class.forName(handlersName[i].trim());
                    Handler currentHandler = (Handler) handlerClass.newInstance();
                    logger.addHandler(currentHandler);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

    }

    private String getProperty(String name) {
        return properties.getProperty(name);
    }

    private Level getLevelProperty(String name, Level defaultValue) {
        String stringProperty = getProperty(name);
        if (stringProperty == null) {
            return defaultValue;
        }

        try {
            return Level.valueOf(stringProperty);
        } catch (Exception ex) {
            ex.printStackTrace();
            return defaultValue;
        }

    }
}
