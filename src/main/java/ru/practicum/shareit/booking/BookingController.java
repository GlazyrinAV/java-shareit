package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto save(@RequestBody BookingDto bookingDto,
                           @RequestHeader(Constants.OWNER_HEADER) int userId) {
        return bookingService.save(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@RequestBody BookingDto bookingDto,
                             @PathVariable int bookingId,
                             @RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        return bookingService.update(bookingDto, bookingId, ownerId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public  BookingDto findById(@PathVariable int bookingId,
                                @RequestHeader(Constants.OWNER_HEADER) int userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findByState(@RequestParam String state,
                                              @RequestHeader(Constants.OWNER_HEADER) int userId) {
        return bookingService.findByState(state, userId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findByOwner(@RequestParam String state,
                                              @RequestHeader(Constants.OWNER_HEADER) int userId)  {
        return bookingService.findByOwner(state, userId);
    }

}