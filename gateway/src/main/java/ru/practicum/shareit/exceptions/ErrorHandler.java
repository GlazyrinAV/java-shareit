package ru.practicum.shareit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.exceptions.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({WrongEnumParameter.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse wrongEnumParameter(RuntimeException exception) {
        return sendErrorResponse(exception.getMessage());
    }

    private ErrorResponse sendErrorResponse(String description) {
        log.info(description);
        return new ErrorResponse(description);
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String error;
    }

}