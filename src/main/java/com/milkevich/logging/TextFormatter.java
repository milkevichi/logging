package com.milkevich.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aksenov on 27.04.2016.
 */
public class TextFormatter extends Formatter{

    @Override
    public String format(Message message) {

        StringBuilder sb = new StringBuilder();

        sb.append(millisToDate(message.getMillisDate())).append(" ").append(message.getLevel());

        String className = message.getClassName();
        String methodName = message.getMethodName();
        if (className != null && methodName != null) {
            sb.append(" [").append(methodName).append("]").append(" (").append(className).append(") ");
        }

        sb.append(" - ").append(formatText(message)).append(" (").append(message.getLoggerName()).append(")");

        Throwable thrown = message.getThrownException();
        if (thrown != null) {
            try (PrintWriter pw = new PrintWriter(new StringWriter())) {
               thrown.printStackTrace(pw);
                sb.append(pw.toString());
            }
        }

        return sb.toString();
    }

}
