package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.UserService;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public Item fromDto(int ownerID, ItemDto itemDto) {
        return Item.builder()
                .owner(userService.findById(ownerID))
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public ItemDtoWithTime toDtoWithTime(Item item) {
        return ItemDtoWithTime.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

}