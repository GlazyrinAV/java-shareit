package ru.practicum.shareit.booking.strategies.by.state;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface StrategyByState {

    Collection<BookingDto> findByBookingState(int userId, Integer from, Integer size);

    BookingState getBookingState();

}