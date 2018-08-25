package com.map.exception;

import java.io.IOException;

/**
 * Application Exception
 */
public class MapException extends Exception {
    public MapException(String message, IOException ioe) {
        super(message, ioe);
    }
}
