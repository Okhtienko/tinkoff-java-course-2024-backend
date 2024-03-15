package org.java.scrapper.handler;

import java.util.Arrays;
import org.java.scrapper.dto.ApiErrorResponse;
import org.java.scrapper.exception.BadRequestException;
import org.java.scrapper.exception.ConflictException;
import org.java.scrapper.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(BadRequestException exception) {
        ApiErrorResponse response = buildErrorResponse(exception.getMessage(), exception.getCode(), exception);
        return response;
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleConflictException(ConflictException exception) {
        ApiErrorResponse response = buildErrorResponse(exception.getMessage(), exception.getCode(), exception);
        return response;
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException exception) {
        ApiErrorResponse response = buildErrorResponse(exception.getMessage(), exception.getCode(), exception);
        return response;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception exception) {
        ApiErrorResponse response = buildErrorResponse(
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception
        );
        return response;
    }

    private ApiErrorResponse buildErrorResponse(String description, Integer code, Exception exception) {
        return new ApiErrorResponse()
            .setDescription(description)
            .setCode(code)
            .setName(exception.getClass().getSimpleName())
            .setMessage(exception.getMessage())
            .setStackTrace(Arrays.asList(exception.getStackTrace()));
    }
}
