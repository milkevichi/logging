package com.milkevich.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aksenov on 27.04.2016.
 */
public abstract class Formatter {

    protected Formatter() {
    }

    public abstract String format(Message message);

    public String getHeader() {
        return "";
    }

    public String getFooter() {
        return "";
    }

    protected String millisToDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(millis);
        return sdf.format(cl.getTime());
    }

    protected String formatText(Message msg) {
        String text = msg.getText();

        Object[] parameters = msg.getParameters();
        if (parameters != null && parameters.length != 0) {
            return java.text.MessageFormat.format(text, parameters);
        } else {
            return text;
        }
    }
}
