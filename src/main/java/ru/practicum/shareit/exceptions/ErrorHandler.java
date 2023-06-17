package ru.practicum.shareit.exceptions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {



    @Data
    public static class ErrorResponse {
        private final ErrorType error;
        private final String description;
    }

}