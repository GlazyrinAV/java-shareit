package ru.practicum.shareit.item.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
@Data
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto saveNew(int userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public Collection<ItemDto> findAllByUserID() {
        return null;
    }

    @Override
    public ItemDto findById(int id) {
        return null;
    }

    @Override
    public Collection<ItemDto> findByName(String text) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }

    @Override
    public ItemDto updateById(int userId, ItemDto itemDto) {
        return null;
    }
}