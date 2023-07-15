package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.Collection;

public interface BookingService {

    BookingDto save(NewBookingDto dto, int userId);

    BookingDto update(int bookingId, int ownerId, boolean status);

    BookingDto findById(int bookingId, int ownerId);

    Collection<BookingDto> findByState(String state, int userId, Integer from, Integer size);

    Collection<BookingDto> findByOwner(String state, int userId, Integer from, Integer size);

}