package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserServiceImpl userService;
    private final ItemMapper itemMapper;

    public ItemDto saveNew(int ownerID, ItemDto itemDto) {
        userService.findById(ownerID);
        return itemMapper.toDto(itemStorage.saveNew(itemMapper.fromDto(ownerID, itemDto)));
    }

    public Collection<ItemDto> findAllByUserId(int ownerId) {
        userService.findById(ownerId);
        Collection<ItemDto> dtoList = new ArrayList<>();
        Collection<Item> itemList = itemStorage.findAllByUserID(ownerId);
        for (Item item : itemList) {
            dtoList.add(itemMapper.toDto(item));
        }
        return dtoList;
    }

    public ItemDto findById(int id) {
        return itemMapper.toDto(itemStorage.findById(id));
    }

    public Collection<ItemDto> findByName(String text) {
        Collection<ItemDto> dtoList = new ArrayList<>();
        if (text != null) {
            Collection<Item> itemList = itemStorage.findByName(text);
            for (Item item : itemList) {
                dtoList.add(itemMapper.toDto(item));
            }
        }
        return dtoList;
    }

    public void removeById(int id) {
        itemStorage.removeById(id);
    }

    public ItemDto updateById(int ownerID, int itemId, ItemDto itemDto) {
        if (!userService.isExists(ownerID)) {
            throw new UserNotFound("Пользователь с ID " + ownerID + " не найден.");
        }
        return itemMapper.toDto(itemStorage.updateById(ownerID, itemId, itemMapper.fromDto(ownerID, itemDto)));
    }

}