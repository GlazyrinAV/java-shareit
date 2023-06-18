package ru.practicum.shareit.exceptions.exceptions;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists(String msg) {
        super(msg);
    }

}
