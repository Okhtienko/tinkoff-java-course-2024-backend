package org.java.bot.exception;

import org.springframework.http.HttpStatus;

public class IllegalArgumentException extends ApiException {
    public IllegalArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
