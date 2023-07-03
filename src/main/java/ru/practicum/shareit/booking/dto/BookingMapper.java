package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final UserService userService;

    private final UserMapper userMapper;

    public Booking fromDto(NewBookingDto dto, int userId) {
        Optional<Item> item = itemRepository.findById(dto.getItemId());
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + dto.getItemId() + " не найден.");
        }
        return Booking.builder()
                .booker(userService.findById(userId))
                .item(item.get())
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(userMapper.toDto(booking.getBooker()))
                .item(itemMapper.toDto(booking.getItem()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public Collection<BookingDto> toDto(Collection<Booking> bookings) {
        return bookings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}