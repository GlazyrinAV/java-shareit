package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapperToDto {

    public static ItemDto mapperToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

}