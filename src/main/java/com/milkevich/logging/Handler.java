package com.milkevich.logging;

/**
 * @author IMilkevich on 22/04/16.
 */
public abstract class Handler {

    protected Handler() {
    }

    public abstract void writeMessage(Message msg);
}
