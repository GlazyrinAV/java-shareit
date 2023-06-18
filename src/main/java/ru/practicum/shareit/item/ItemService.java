package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;

    public ItemDto saveNew(int ownerID, ItemDto itemDto) {

    }

    public Collection<ItemDto> findAllByUserID(int ownerId) {

    }

    public ItemDto findById(int id) {

    }

    public Collection<ItemDto> findByName(String text) {

    }

    public void removeById(int id) {

    }

    public ItemDto updateById(int ownerID, ItemDto itemDto) {

    }

}