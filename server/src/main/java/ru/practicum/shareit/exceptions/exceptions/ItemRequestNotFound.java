package ru.practicum.shareit.exceptions.exceptions;

public class ItemRequestNotFound extends RuntimeException {

    public ItemRequestNotFound(String msg) {
        super(msg);
    }

    public ItemRequestNotFound(int itemRequestId) {
        super("Запрос с ID " + itemRequestId + " не найден.");
    }

}
