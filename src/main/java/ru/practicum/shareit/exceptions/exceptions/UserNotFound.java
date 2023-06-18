package ru.practicum.shareit.exceptions.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String msg) {
        super(msg);
    }

}
