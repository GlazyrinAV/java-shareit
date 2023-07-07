package ru.practicum.shareit.booking.strategies.by.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FindByOwnerRejected implements StrategyByOwner {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<BookingDto> findByBookingState(int userId) {
        return bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED));
    }

    @Override
    public BookingState getBookingState() {
        return BookingState.REJECTED;
    }
}