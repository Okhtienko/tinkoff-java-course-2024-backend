package org.java.scrapper.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final Integer code;
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.code = status.value();
        this.status = status;
    }
}
