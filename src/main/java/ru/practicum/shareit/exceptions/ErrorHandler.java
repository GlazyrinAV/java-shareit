package ru.practicum.shareit.exceptions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.exceptions.UserAlreadyExists;
import ru.practicum.shareit.exceptions.exceptions.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse validationException(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    @ExceptionHandler({UserAlreadyExists.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse userAlreadyExists(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    private ErrorResponse sendErrorResponse(ErrorType errorType, String description) {
        log.info(description);
        return new ErrorResponse(errorType, description);
    }

    @Data
    public static class ErrorResponse {
        private final ErrorType error;
        private final String description;
    }

}