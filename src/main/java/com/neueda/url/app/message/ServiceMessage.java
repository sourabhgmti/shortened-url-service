package com.neueda.url.app.message;

public interface ServiceMessage {

    interface ValidationMessageSourceCode {
        String URL_NOT_EMPTY = "url.not.empty";
        String URL_NOT_VALID = "url.not.valid";
    }

    interface BusinessMessageSourceCode {
        String URL_SERVICE_EXCEPTION = "url.service.exception";
        String URL_NOT_FOUND_EXCEPTION = "url.not.found.exception";
    }
}
