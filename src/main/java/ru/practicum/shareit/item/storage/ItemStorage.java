package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item saveNew(Item item);

    Collection<Item> findAllByUserID(int ownerId);

    Item findById(int id);

    Collection<Item> findByName(String text);

    void removeById(int id);

    Item updateById(int ownerId, int itemId, Item itemDto);

}