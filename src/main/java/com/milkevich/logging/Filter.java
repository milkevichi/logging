package com.milkevich.logging;

/**
 * @author IMilkevich on 24/04/16.
 */
public interface Filter {

    boolean isMessageLoggable(Message message);

}
