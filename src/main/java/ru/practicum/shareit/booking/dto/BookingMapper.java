package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ItemRepository itemRepository;

    private final UserService userService;

    public Booking fromDto(BookingDto bookingDto, int userId) {
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + bookingDto.getItemId() + " не найден.");
        }
        return Booking.builder()
                .id(bookingDto.getId())
                .booker(userService.findById(userId))
                .item(item.get())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

}