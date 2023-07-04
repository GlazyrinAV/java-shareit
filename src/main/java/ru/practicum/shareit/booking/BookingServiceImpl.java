package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exceptions.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;

    private final ItemRepository itemRepository;

    private final ItemService itemService;

    private final BookingRepository bookingRepository;

    public final BookingMapper bookingMapper;

    @Override
    public BookingDto save(NewBookingDto dto, int userId) {
        checkUserExistence(userId);
        checkItemExistence(dto.getItemId());
        checkBookingTime(dto);
        int ownerId = itemRepository.findById(dto.getItemId()).get().getOwner().getId();
        if (ownerId == userId) {
            throw new UserNotFound("Собственник не может создавать запрос на свою вещь.");
        }
        Booking newBooking = bookingMapper.fromDto(dto, userId);
        if (Boolean.FALSE.equals(newBooking.getItem().getAvailable())) {
            throw new WrongParameter("Предмет с ID " + newBooking.getItem().getId() + " не доступен.");
        }
        newBooking.setStatus(BookingStatus.WAITING);
        return bookingMapper.toDto(bookingRepository.save(newBooking));
    }

    @Override
    public BookingDto update(int bookingId, int ownerId, boolean status) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new BookingNotFound("Бронь с ID " + bookingId + " не найдена.");
        }
        Integer ownerIdInDb = booking.get().getItem().getOwner().getId();
        if (ownerIdInDb != ownerId) {
            throw new UserNotFound("Данный запрос может сделать только собственник предмета.");
        }
        if (booking.get().getStatus().equals(BookingStatus.APPROVED) || booking.get().getStatus().equals(BookingStatus.REJECTED)) {
            throw new WrongParameter("Данная заявка уже подтверждена.");
        }
        if (status) {
            booking.get().setStatus(BookingStatus.APPROVED);
        } else {
            booking.get().setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking.get());
        return bookingMapper.toDto(booking.get());
    }

    @Override
    public BookingDto findById(int bookingId, int userId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new BookingNotFound("Бронь с ID " + bookingId + " не найдена.");
        }
        Integer ownerId = booking.get().getItem().getOwner().getId();
        Integer bookerId = booking.get().getBooker().getId();
        if (userId != ownerId && userId != bookerId) {
            throw new UserNotFound("Данный запрос может сделать только собственник предмета или бронирующий.");
        }
        return bookingMapper.toDto(booking.get());
    }

    @Override
    public Collection<BookingDto> findByState(String state, int userId) {
        checkUserExistence(userId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException exception) {
            throw new WrongEnumParameter("Unknown state: " + state);
        }
        Collection<BookingDto> bookings;
        switch (bookingState) {
            case ALL:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdEqualsOrderByStartDesc(userId));
                break;
            case FUTURE:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case PAST:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case CURRENT:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterAndStatusEqualsOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.APPROVED));
                break;
            case WAITING:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED));
                break;
            default:
                throw new WrongParameter("Недопустимый вариант статуса.");
        }
        return bookings;
    }

    @Override
    public Collection<BookingDto> findByOwner(String state, int userId) {
        checkUserExistence(userId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException exception) {
            throw new WrongEnumParameter("Unknown state: " + state);
        }
        Collection<BookingDto> bookings;
        switch (bookingState) {
            case ALL:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId));
                break;
            case FUTURE:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case PAST:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case CURRENT:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterAndStatusEqualsOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.APPROVED));
                break;
            case WAITING:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookings = bookingMapper.toDto(bookingRepository.findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED));
                break;
            default:
                throw new WrongParameter("Недопустимый вариант статуса.");
        }
        return bookings;
    }

    private void checkUserExistence(int userId) {
        if (!userService.isExists(userId)) {
            throw new UserNotFound("Пользователь с ID " + userId + " не найден.");
        }
    }

    private void checkItemExistence(int itemId) {
        if (!itemService.isExists(itemId)) {
            throw new ItemNotFound("Предмет с ID " + itemId + " не найден.");
        }
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