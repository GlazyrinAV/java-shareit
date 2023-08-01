package ru.practicum.shareit.booking.strategies.by.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FindByStatePast implements StrategyByState {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<BookingDto> findByBookingState(int userId) {
        return bookingMapper.toDto(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
    }

    @Override
    public BookingState getBookingState() {
        return BookingState.PAST;
    }

}