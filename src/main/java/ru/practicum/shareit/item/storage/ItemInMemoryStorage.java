package ru.practicum.shareit.item.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
@Data
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();

    @Override
    public Item saveNew(int ownerId, Item item) {
        return null;
    }

    @Override
    public Collection<Item> findAllByUserID(int ownerId) {
        return null;
    }

    @Override
    public Item findById(int id) {
        return null;
    }

    @Override
    public Collection<Item> findByName(String text) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }

    @Override
    public Item updateById(int ownerId, Item itemDto) {
        return null;
    }

}