package org.java.scrapper.handler;

import java.util.Arrays;
import org.java.scrapper.dto.ApiErrorResponse;
import org.java.scrapper.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiErrorResponse> handleBotException(ApiException exception) {
        ApiErrorResponse error = buildErrorResponse(exception.getMessage(), exception.getCode(), exception);
        return ResponseEntity.status(exception.getStatus()).body(error);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception) {
        ApiErrorResponse error = buildErrorResponse(
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ApiErrorResponse buildErrorResponse(String description, Integer code, Exception exception) {
        return new ApiErrorResponse()
            .setDescription(description)
            .setCode(code)
            .setName(exception.getClass().getSimpleName())
            .setMessage(exception.getMessage())
            .setStacktrace(Arrays.asList(exception.getStackTrace()));
    }
}
