package org.java.bot.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRegistrationException extends ApiException {
    public DuplicateRegistrationException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
