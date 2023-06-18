package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapperToDto {

    public ItemDto mapperToDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

}