package org.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class DuplicateChatRegistrationException extends ApiException {
    public DuplicateChatRegistrationException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
