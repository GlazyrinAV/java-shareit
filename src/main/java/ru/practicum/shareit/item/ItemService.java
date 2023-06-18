package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MapperFromDto;
import ru.practicum.shareit.item.dto.MapperToDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto saveNew(int ownerID, ItemDto itemDto) {
        userService.findById(ownerID);
        return MapperToDto.mapperToDto(itemStorage.saveNew(MapperFromDto.mapperFromDto(ownerID, itemDto)));
    }

    public Collection<ItemDto> findAllByUserID(int ownerId) {
        userService.findById(ownerId);
        Collection<ItemDto> dtoList = new ArrayList<>();
        Collection<Item> itemList = itemStorage.findAllByUserID(ownerId);
        for (Item item : itemList) {
            dtoList.add(MapperToDto.mapperToDto(item));
        }
        return dtoList;
    }

    public ItemDto findById(int id) {
        return MapperToDto.mapperToDto(itemStorage.findById(id));
    }

    public Collection<ItemDto> findByName(String text) {
        Collection<ItemDto> dtoList = new ArrayList<>();
        Collection<Item> itemList = itemStorage.findByName(text);
        for (Item item : itemList) {
            dtoList.add(MapperToDto.mapperToDto(item));
        }
        return dtoList;
    }

    public void removeById(int id) {
        itemStorage.removeById(id);
    }

    public ItemDto updateById(int ownerID, int itemId, ItemDto itemDto) {
        userService.findById(ownerID);
        return MapperToDto.mapperToDto(itemStorage.updateById(itemId, MapperFromDto.mapperFromDto(ownerID, itemDto)));
    }

}