package ru.practicum.shareit.exceptions.exceptions;

public class ItemNotFound extends RuntimeException {

    public ItemNotFound(String msg) {
        super(msg);
    }

    public ItemNotFound(int itemId) {
        super("Предмет с ID " + itemId + " не найден.");
    }

}
