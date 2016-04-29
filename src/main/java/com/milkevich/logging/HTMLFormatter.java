package com.milkevich.logging;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by aksenov on 27.04.2016.
 */
public class HTMLFormatter extends Formatter {

    @Override
    public String getHeader() {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>\n").append("<head>\n").append("<title>").append("HTMLlog").append("</title>\n");
        sb.append("<style type=\"text/css\">").append("td{\n white-space: pre-line\n}").append("</style\n>");
        sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n").append("</head>\n");

        sb.append("<body>\n");
        sb.append("<table border=1>\n");
        sb.append("<tr>\n").append("<th>").append("Date").append("</th>\n");
        sb.append("<th>").append("Level").append("</th>\n");
        sb.append("<th>").append("Source").append("</th>\n");
        sb.append("<th>").append("Text").append("</th>\n");
        sb.append("<th>").append("Thrown exception").append("</th>\n").append("</tr>\n");

        return sb.toString();
    }

    @Override
    public String getFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</table>").append("</body>").append("</html>");

        return sb.toString();
    }


    @Override
    public String format(Message message) {

        StringBuilder sb = new StringBuilder();

        Level level = message.getLevel();
        if (level == Level.FATAL || level == Level.ERROR) {
            sb.append("<tr bgcolor=red>\n");
        } else if (level == level.WARN) {
            sb.append("<tr bgcolor=gray>\n");
        } else {
            sb.append("<tr bgcolor=white>\n");
        }

        sb.append("<td>").append(millisToDate(message.getMillisDate())).append("</td>\n");
        sb.append("<td>").append(message.getLevel()).append("</td>\n");

        String className = message.getClassName();
        String methodName = message.getMethodName();
        if (className != null && methodName != null) {
            sb.append("<td>").append(className).append(": ").append(methodName).append("</td>\n");
        }

        sb.append("<td>").append(formatText(message)).append("</td>\n");

        Throwable thrown = message.getThrownException();
        if (thrown != null) {
            String stackTrace = ExceptionUtils.getStackTrace(thrown);
            sb.append("<td>").append(stackTrace).append("</td>\n");
        } else {
            sb.append("<td>").append("").append("</td>\n");
        }
        sb.append("</tr>\n");

        return sb.toString();
    }
}
