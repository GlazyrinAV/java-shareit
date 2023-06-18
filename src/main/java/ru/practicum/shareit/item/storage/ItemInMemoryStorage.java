package ru.practicum.shareit.item.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongOwner;
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
            throw  new WrongParameter("Неправильно указаны параметры нового вещи.");
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
        if (items.get(id) == null) {
            throw new ItemNotFound("Вещь с ID " + id + " не найдена.");
        }
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
    public Item updateById(int ownerId, int itemId, Item item) {
        Item oldItem = items.get(itemId);
        if (ownerId != oldItem.getOwnerId()) {
            throw new WrongOwner("Данный пользователь не является собственником вещи.");
        }
        if (item.getName() != null) {
            if (item.getName().isBlank()) {
                throw  new WrongParameter("Новое имя вещи не может быть пустым.");
            }
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            if (item.getDescription().isBlank()) {
                throw  new WrongParameter("Новое описание вещи не может быть пустым.");
            }
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        items.replace(oldItem.getId(), oldItem);
        return oldItem;
    }

}