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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Data
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();

    private static int id = 1;

    @Override
    public Item saveNew(Item item) {
        if (item.getAvailable() == null) {
            throw new WrongParameter("не указаны данные о доступности вещи.");
        }
        item.setId(setId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findAllByUserID(int ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .collect(Collectors.toList());
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
        return items.values().stream()
                .filter(item -> item.getAvailable() && !text.isBlank() && (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeById(int id) {
        items.remove(ItemInMemoryStorage.id);
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

    private static int setId() {
        return id++;
    }

}