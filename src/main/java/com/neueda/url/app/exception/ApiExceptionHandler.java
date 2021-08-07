package com.neueda.url.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *
     * @param badUrlException
     * @param webRequest
     * @return
     */
    @ExceptionHandler({BadUrlException.class})
    public final ResponseEntity<Error> prepareAndReturnBadUrlExceptionResponse(BadUrlException badUrlException, WebRequest webRequest) {
        Error error = new Error(badUrlException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param urlServiceException
     * @param webRequest
     * @return
     */
    @ExceptionHandler({ApiServiceException.class})
    public final ResponseEntity<Error> prepareAndReturnInvalidUrlExceptionResponse(ApiServiceException urlServiceException, WebRequest webRequest) {
        Error error = new Error(urlServiceException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param urlNotFoundException
     * @param webRequest
     * @return
     */
    @ExceptionHandler({UrlNotFoundException.class})
    public final ResponseEntity<Error> prepareAndReturnUrlNotFoundExceptionResponse(UrlNotFoundException urlNotFoundException, WebRequest webRequest) {
        Error error = new Error(urlNotFoundException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
