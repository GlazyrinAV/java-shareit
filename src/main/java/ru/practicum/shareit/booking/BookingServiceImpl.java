package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.strategies.by.owner.StrategyByOwnerFactory;
import ru.practicum.shareit.booking.strategies.by.state.StrategyByStateFactory;
import ru.practicum.shareit.exceptions.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final StrategyByOwnerFactory strategyByOwnerFactory;

    private final StrategyByStateFactory strategyByStateFactory;

    @Override
    public BookingDto save(NewBookingDto dto, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new ItemNotFound(dto.getItemId()));
        checkBookingTime(dto);
        if (item.getOwner().getId() == userId) {
            throw new UserNotFound("Собственник не может создавать запрос на свою вещь.");
        }
        Booking newBooking = bookingMapper.fromDto(dto, user);
        if (Boolean.FALSE.equals(newBooking.getItem().getAvailable())) {
            throw new WrongParameter("Предмет с ID " + newBooking.getItem().getId() + " не доступен.");
        }
        newBooking.setStatus(BookingStatus.WAITING);
        return bookingMapper.toDto(bookingRepository.save(newBooking));
    }

    @Override
    public BookingDto update(int bookingId, int ownerId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFound(bookingId));
        Integer ownerIdInDb = booking.getItem().getOwner().getId();
        if (ownerIdInDb != ownerId) {
            throw new UserNotFound("Данный запрос может сделать только собственник предмета.");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED) || booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new WrongParameter("Данная заявка уже подтверждена.");
        }
        if (status) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto findById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFound(bookingId));
        Integer ownerId = booking.getItem().getOwner().getId();
        Integer bookerId = booking.getBooker().getId();
        if (userId != ownerId && userId != bookerId) {
            throw new UserNotFound("Данный запрос может сделать только собственник предмета или бронирующий.");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> findByState(String state, int userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException exception) {
            throw new WrongEnumParameter("Unknown state: " + state);
        }
        return strategyByStateFactory.findStrategy(bookingState).findByBookingState(userId, from, size);
    }

    @Override
    public Collection<BookingDto> findByOwner(String state, int userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException exception) {
            throw new WrongEnumParameter("Unknown state: " + state);
        }
        return strategyByOwnerFactory.findStrategy(bookingState).findByBookingState(userId, from, size);
    }

    private void checkBookingTime(NewBookingDto dto) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new WrongParameter("Не указан временной промежуток.");
        }
        if (dto.getEnd().isBefore(dto.getStart())) {
            throw new WrongParameter("Конец не может быть раньше начала.");
        }
        if (dto.getStart().isEqual(dto.getEnd())) {
            throw new WrongParameter("Конец не может быть равен началу.");
        }
        if (dto.getStart().isBefore(LocalDateTime.now()) || dto.getEnd().isBefore(LocalDateTime.now())) {
            throw new WrongParameter("Промежуток не может быть в прошлом.");
        }
    }
}