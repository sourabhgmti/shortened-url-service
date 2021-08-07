package com.neueda.url.app.exception;

public class UrlNotFoundException extends RuntimeException {

    /**
     *
     * @param message
     */
    public UrlNotFoundException(String message) {
        super(message);
    }
}
