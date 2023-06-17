package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemStorage {

    ItemDto saveNew(int userId, ItemDto itemDto);

    Collection<ItemDto> findAllByUserID();

    ItemDto findById(int id);

    Collection<ItemDto> findByName(String text);

    void removeById(int id);

    ItemDto updateById(int userId, ItemDto itemDto);

}