package ru.practicum.shareit.exceptions.exceptions;

public class ItemAlreadyExists extends RuntimeException {

    public ItemAlreadyExists(String msg) {
        super(msg);
    }

}
