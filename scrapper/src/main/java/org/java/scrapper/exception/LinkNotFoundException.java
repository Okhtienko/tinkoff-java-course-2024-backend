package org.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ApiException {
    public LinkNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
