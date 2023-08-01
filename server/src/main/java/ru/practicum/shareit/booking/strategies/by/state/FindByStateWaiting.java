package ru.practicum.shareit.booking.strategies.by.state;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FindByStateWaiting implements StrategyByState {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<BookingDto> findByBookingState(int userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from == 0 ? 0 : from / size, size, Sort.by("start").descending());
        return bookingMapper.toDto(bookingRepository.findByBooker_IdAndStatusEquals(userId, BookingStatus.WAITING, page).getContent());
    }

    @Override
    public BookingState getBookingState() {
        return BookingState.WAITING;
    }
}
