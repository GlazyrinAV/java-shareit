package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;

import java.util.Collection;

public interface ItemService {

    ItemDto save(int ownerID, ItemDto itemDto);

    Collection<ItemDtoWithTime> findAllByUserId(Integer ownerId);

    ItemDtoWithTime findById(int id, int ownerId);

    Collection<ItemDto> findByName(String text);

    void deleteById(int id);

    ItemDto updateById(int ownerID, int itemId, ItemDto itemDto);

    boolean isExists(int itemId);

    CommentDto saveComment(int userId, int itemId, CommentDto dto);

}
