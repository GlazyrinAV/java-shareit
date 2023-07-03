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

    @ExceptionHandler({UserAlreadyExists.class, ItemAlreadyExists.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityAlreadyExists(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    @ExceptionHandler({UserNotFound.class, ItemNotFound.class, BookingNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFound(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    @ExceptionHandler({WrongParameter.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse wrongParameter(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    @ExceptionHandler({WrongOwner.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse wrongOwner(RuntimeException exception) {
        return sendErrorResponse(ErrorType.ERROR, exception.getMessage());
    }

    private ErrorResponse sendErrorResponse(ErrorType errorType, String description) {
        log.info(description);
        return new ErrorResponse(errorType, description);
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private final ErrorType error;
        private final String description;
    }

}