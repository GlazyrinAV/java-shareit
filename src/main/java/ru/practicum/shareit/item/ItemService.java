package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto saveNew(int ownerID, ItemDto itemDto);

    Collection<ItemDto> findAllByUserId(int ownerId);

    ItemDto findById(int id);

    Collection<ItemDto> findByName(String text);

    void removeById(int id);

    ItemDto updateById(int ownerID, int itemId, ItemDto itemDto);

}
