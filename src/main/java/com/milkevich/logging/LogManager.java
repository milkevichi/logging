package com.milkevich.logging;

import com.sun.org.apache.bcel.internal.generic.LoadClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private Logger rootLogger;
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

    public static LogManager getLogManager() {
        return logManager;
    }

    public static Logger getLogger() {
        return getLogger("RootLogger");
    }

    public static Logger getLogger(String name) {
        Logger currentLogger = loggers.get(name);
        if (currentLogger == null) {
            currentLogger = createLogger(name);
            logManager.loadParentLoggers(currentLogger, name);
        }

        return currentLogger;
    }

    private static Logger createLogger(String name) {
        Logger currentLogger = new Logger(name);
        currentLogger.setLevel(logManager.getLevelProperty(name + ".level", defaultLevel));
        logManager.loadHandlers(currentLogger, name + ".handlers");
        addLogger(currentLogger);
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
                System.out.println("File " + filename + " isn't found, try to load logging.xml");
//                return;
                String filexml = "logging.xml";
                try (InputStream inputStreamXML = LogManager.class.getClassLoader().getResourceAsStream(filexml)) {
                    if (inputStreamXML == null) {
                        System.out.println("File " + filexml + "isn't found");
                        return;
                    }

                    loadXMLtoProperties(inputStreamXML);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                properties.load(inputStream);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadXMLtoProperties(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            doc.getDocumentElement().normalize();

            NodeList listLoggers = doc.getElementsByTagName("logger");
            for (int i = 0; i < listLoggers.getLength(); i++) {
                Node node = listLoggers.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String loggerName = element.getAttribute("name");

                    NodeList listLevel = element.getElementsByTagName("level");
                    if (listLevel.getLength()>0) {
                        properties.setProperty(loggerName + ".level", listLevel.item(0).getTextContent());
                    }

                    NodeList listHandler = element.getElementsByTagName("handler-ref");
                    StringBuilder handlers = new StringBuilder();
                    boolean isFirst = true;
                    for (int i1 = 0; i1 < listHandler.getLength(); i1++) {
                        if (isFirst) {
                            handlers.append(listHandler.item(i1).getTextContent());
                            isFirst = false;
                        } else {
                            handlers.append(", ").append(listHandler.item(i1).getTextContent());
                        }
                    }

                    if (handlers.length() > 0) {
                        properties.setProperty(loggerName + ".handlers", handlers.toString());
                    }
                }
            }

            NodeList listHandlers = doc.getElementsByTagName("handler");
            for (int i = 0; i < listHandlers.getLength(); i++) {
                Node node = listHandlers.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String handlerName = element.getAttribute("name");

                    NodeList listFormatter = element.getElementsByTagName("formatter");
                    if (listFormatter.getLength()>0) {
                        properties.setProperty(handlerName + ".formatter", listFormatter.item(0).getTextContent());
                    }

                    NodeList listSendto = element.getElementsByTagName("sendto");
                    StringBuilder sendTo = new StringBuilder();
                    boolean isFirst = true;
                    for (int i1 = 0; i1 < listSendto.getLength(); i1++) {
                        if (isFirst) {
                            sendTo.append(listSendto.item(i1).getTextContent());
                            isFirst = false;
                        } else {
                            sendTo.append(", ").append(listSendto.item(i1).getTextContent());
                        }
                    }

                    if (sendTo.length() > 0) {
                        properties.setProperty(handlerName + ".sendTo", sendTo.toString());
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadParentLoggers(Logger logger, String name) {
        int i = name.length();
        Logger currentLogger = logger;
        for (; ; ) {
            int i2 = name.lastIndexOf(".", i);
            if (i2 < 0) {
                break;
            }
            String parentName = name.substring(0, i2);
            if (getProperty(parentName + ".level") != null || getProperty(parentName + ".handlers") != null) {

                Logger parentLogger = loggers.get(parentName);
                if (parentLogger == null) {
                    parentLogger = createLogger(parentName);
                }
                currentLogger.setParent(parentLogger);
                currentLogger = parentLogger;
            }
            i = i2 - 1;

        }
    }

    private void loadHandlers(Logger logger, String name) {
        String stringHandlers = getProperty(name);

        if (stringHandlers != null) {
            String[] handlersName = stringHandlers.split("\\s*(,|\\s)\\s*");
            for (int i = 0; i < handlersName.length; i++) {
                try {
                    String nameOfHandler = handlersName[i].trim();
                    Class handlerClass = Class.forName(nameOfHandler);
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

    public Formatter getFormatterProperty(String name, Formatter defaultValue) {
        String stringProperty = getProperty(name);
        if (stringProperty == null) {
            return defaultValue;
        }

        try {
            Class formatterClass = Class.forName(stringProperty.trim());
            return (Formatter) formatterClass.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return defaultValue;
        }

    }

    public String getStringProperty(String name, String defaultValue) {
        String stringProperty = getProperty(name);
        if (stringProperty == null) {
            return defaultValue;
        }

        return stringProperty.trim();
    }

}
