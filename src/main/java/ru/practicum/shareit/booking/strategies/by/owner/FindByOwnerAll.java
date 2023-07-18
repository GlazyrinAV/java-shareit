package ru.practicum.shareit.booking.strategies.by.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FindByOwnerAll implements StrategyByOwner {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<BookingDto> findByBookingState(int userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return bookingMapper.toDto(bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId));
        }
        if (from < 0 || size < 1) {
            throw new WrongParameter("Указаны неправильные параметры.");
        }
        Pageable page = PageRequest.of(from == 0 ? 0 : from / size, size);
        return bookingMapper.toDto(bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId, page).getContent());
    }

    @Override
    public BookingState getBookingState() {
        return BookingState.ALL;
    }

}