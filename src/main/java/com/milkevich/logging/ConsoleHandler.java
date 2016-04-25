package com.milkevich.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author IMilkevich on 23/04/16.
 */
public class ConsoleHandler extends Handler {

    public ConsoleHandler() {
    }

    @Override
    public void writeMessage(Message msg) {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(msg.getMillisDate());
        sb.append(sdf.format(cl.getTime())).append(" ").append(msg.getLevel());
        //sb.append(" (by ").append(msg.getLoggerName());

        String className = msg.getClassName();
        String methodName = msg.getMethodName();
        if (className != null && methodName != null) {
            sb.append("[").append(methodName).append("]").append(" (").append(className).append(") ");
        }


        sb.append(" - ").append(msg.getText());

        System.err.println(sb);
    }
}
