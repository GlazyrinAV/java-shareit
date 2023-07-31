package ru.practicum.shareit.exceptions.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String msg) {
        super(msg);
    }

    public UserNotFound(int userId) {
        super("Пользователь с ID " + userId + " не найден.");
    }

}
