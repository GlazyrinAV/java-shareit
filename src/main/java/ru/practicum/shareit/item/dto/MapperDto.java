package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapperDto {

    public static Item fromDto(int ownerID, ItemDto itemDto) {
        return Item.builder()
                .ownerId(ownerID)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

}