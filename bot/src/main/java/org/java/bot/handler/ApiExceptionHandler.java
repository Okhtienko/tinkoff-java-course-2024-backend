package org.java.bot.handler;

import java.util.Arrays;
import org.java.bot.dto.ApiErrorResponse;
import org.java.bot.exception.ApiException;
import org.java.bot.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(ApiException exception) {
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
