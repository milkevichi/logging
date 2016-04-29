package com.milkevich.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aksenov on 27.04.2016.
 */
public class XMLFormatter extends Formatter{

    @Override
    public String getHeader() {

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<log>\n");

        return sb.toString();
    }

    @Override
    public String getFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</log>");

        return sb.toString();
    }

    @Override
    public String format(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("    <message>\n");

        sb.append("        <date>").append(millisToDate(message.getMillisDate())).append("</date>\n");
        sb.append("        <level>").append(message.getLevel()).append("</level>\n");

        String className = message.getClassName();
        String methodName = message.getMethodName();
        if (className != null && methodName != null) {
            sb.append("        <class>").append(className).append("</class>\n");
            sb.append("        <method>").append(methodName).append("</method>\n");
        }

        sb.append("        <text>").append(formatText(message)).append("</text>\n");

        Throwable thrown = message.getThrownException();
        if (thrown != null) {
            sb.append("        <exception>\n");
            StackTraceElement[] stackTraceElements = thrown.getStackTrace();
            for (StackTraceElement ste: stackTraceElements) {
                sb.append("            <class>").append(ste.getClassName()).append("</class>\n");
                sb.append("            <method>").append(ste.getMethodName()).append("</method>\n");
                sb.append("            <line>").append(ste.getLineNumber()).append("</line>\n");
            }
            sb.append("        </exception>\n");
        }

        sb.append("        <logger>").append(message.getLoggerName()).append("</logger>\n");

        sb.append("    </message>\n");

        return sb.toString();
    }
}
