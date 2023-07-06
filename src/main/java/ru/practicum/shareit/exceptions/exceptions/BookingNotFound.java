package ru.practicum.shareit.exceptions.exceptions;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(String msg) {
        super(msg);
    }

    public BookingNotFound(int bookingId) {
        super("Бронь с ID " + bookingId + " не найдена.");
    }

}
