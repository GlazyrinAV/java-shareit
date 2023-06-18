package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapperFromDto {

    public static Item mapperFromDto(int ownerID, ItemDto itemDto) {
        return Item.builder()
                .ownerId(ownerID)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

}