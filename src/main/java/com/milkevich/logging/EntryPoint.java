package com.milkevich.logging;

/**
 * @author IMilkevich on 23/04/16.
 */
public class EntryPoint {

    public static void main(String[] args) {
//        Logger defaultLogger = LogManager.getLogger();
//
//        defaultLogger.setFilter(new Filter() {
//            @Override
//            public boolean isMessageLoggable(Message msg) {
//                return msg.getText().startsWith("TEST");
//            }
//        });
//
//        defaultLogger.log(Level.INFO, "TEST: Log info");
//        defaultLogger.log(Level.DEBUG, "Log debug");
//        defaultLogger.log(Level.TRACE, "Log trace");

//        Logger logger = LogManager.getLogger(EntryPoint.class.getName());
//        Handler consoleHandler = new FileHandler("logFile.xml");
//        logger.addHandler(consoleHandler);
//        logger.log(Level.INFO, "Log info");
//        logger.log(Level.TRACE, "Log trace");
//        logger.log(Level.WARN, "Log warn");

        Logger configLogger = LogManager.getLogger("ConfigLogger");
        Handler consoleHandler = new FileHandler("logFile.xml");
        configLogger.addHandler(consoleHandler);

        configLogger.log(Level.INFO, "Log info {0}", "ghfkf");

        configLogger.warn("Warn info");

        try {
            throw new RuntimeException("wrong");
        } catch (Exception ex) {
            configLogger.log(Level.ERROR, ex.getMessage(),ex);
        }

    }
}
