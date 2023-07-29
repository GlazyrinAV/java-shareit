package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto save(@RequestBody NewBookingDto dto,
                           @RequestHeader(Constants.OWNER_HEADER) int userId) {
        return bookingService.save(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@PathVariable int bookingId,
                             @RequestHeader(Constants.OWNER_HEADER) int ownerId,
                             @RequestParam(name = "approved") boolean status) {
        return bookingService.update(bookingId, ownerId, status);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public  BookingDto findById(@PathVariable int bookingId,
                                @RequestHeader(Constants.OWNER_HEADER) int userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findByState(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                              @RequestHeader(Constants.OWNER_HEADER) int userId,
                                              @RequestParam(required = false) Integer from,
                                              @RequestParam(required = false) Integer size) {
        return bookingService.findByState(state, userId, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findByOwner(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                              @RequestHeader(Constants.OWNER_HEADER) int userId,
                                              @RequestParam(required = false) Integer from,
                                              @RequestParam(required = false) Integer size)  {
        return bookingService.findByOwner(state, userId, from, size);
    }

}