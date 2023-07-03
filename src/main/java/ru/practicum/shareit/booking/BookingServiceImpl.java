package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;

    private final ItemService itemService;

    private final BookingRepository bookingRepository;

    public final BookingMapper bookingMapper;

    @Override
    public BookingDto save(NewBookingDto dto, int userId) {
        checkUserExistence(userId);
        checkItemExistence(dto.getItemId());
        checkBookingTime(dto);
        Booking newBooking = bookingMapper.fromDto(dto, userId);
        if (Boolean.FALSE.equals(newBooking.getItem().getAvailable())) {
            throw new WrongParameter("Предмет с ID " + newBooking.getItem().getId() + " не доступен.");
        }
        newBooking.setStatus(BookingStatus.WAITING);
        return bookingMapper.toDto(bookingRepository.save(newBooking));
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
    }
}