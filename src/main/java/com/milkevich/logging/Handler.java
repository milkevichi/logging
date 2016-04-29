package com.milkevich.logging;

/**
 * @author IMilkevich on 22/04/16.
 */
public abstract class Handler {

    private Formatter formatter;

    protected Handler() {
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public abstract void writeMessage(Message msg);
}
