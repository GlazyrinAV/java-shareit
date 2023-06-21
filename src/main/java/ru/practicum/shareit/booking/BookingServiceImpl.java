package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public final BookingMapper bookingMapper;

    @Override
    public BookingDto save(BookingDto bookingDto, int userId) {
        return bookingMapper.toDto(bookingRepository.save(bookingMapper.fromDto(bookingDto, userId)));
    }

    @Override
    public BookingDto update(BookingDto bookingDto, int bookingId, int ownerId) {
        return null;
    }

    @Override
    public BookingDto findById(int bookingId, int ownerId) {
        return null;
    }

    @Override
    public Collection<BookingDto> findByState(String state, int userId) {
        return null;
    }

    @Override
    public Collection<BookingDto> findByOwner(String state, int userId) {
        return null;
    }
}