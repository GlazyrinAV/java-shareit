package ru.practicum.shareit.item.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
@Data
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();

    private static int itemId = 1;

    @Override
    public Item saveNew(Item item) {
        if (item.getName() == null || item.getName().isBlank() || item.getDescription() == null || item.getDescription().isBlank()) {
            throw  new WrongParameter("Неправильно указаны параметры нового предмета.");
        }
        if (item.getAvailable() == null) {
            throw new WrongParameter("не указаны данные о доступности.");
        }
        item.setId(itemId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findAllByUserID(int ownerId) {
        Collection<Item> listOfItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == ownerId) {
                listOfItems.add(item);
            }
        }
        return listOfItems;
    }

    @Override
    public Item findById(int id) {
        return items.get(id);
    }

    @Override
    public Collection<Item> findByName(String text) {
        Collection<Item> listOfItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                listOfItems.add(item);
            }
        }
        return listOfItems;
    }

    @Override
    public void removeById(int id) {
        items.remove(itemId);
    }

    @Override
    public Item updateById(int itemId, Item item) {
        Item oldItem = items.get(itemId);
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        items.remove(oldItem.getId(), oldItem);
        return oldItem;
    }

}