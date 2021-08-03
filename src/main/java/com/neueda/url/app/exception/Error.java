package com.neueda.url.app.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class Error {
    private Date timestamp = new Date();
    private String message;
    private String details;

    public Error(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
