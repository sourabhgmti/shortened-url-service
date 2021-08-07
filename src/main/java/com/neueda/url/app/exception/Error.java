package com.neueda.url.app.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class Error {

    private Date timestamp = new Date();
    private String message;
    private String details;

    /**
     * 
     * @param message
     * @param details
     */
    public Error(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
