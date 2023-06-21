package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongOwner;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto save(int ownerID, ItemDto itemDto) {
        checkUserExistence(ownerID);
        return itemMapper.toDto(itemRepository.save(itemMapper.fromDto(ownerID, itemDto)));
    }

    @Override
    public Collection<ItemDto> findAllByUserId(Integer ownerId) {
        checkUserExistence(ownerId);
        return itemRepository.findAllWhereOwnerIdIn(ownerId).stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + id + " не найдена.");
        }
        return itemMapper.toDto(item.get());
    }

    @Override
    public Collection<ItemDto> findByName(String text) {
        Collection<ItemDto> dtoList = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            dtoList = itemRepository.findByName(text).stream()
                    .map(itemMapper::toDto)
                    .collect(Collectors.toList());
        }
        return dtoList;
    }

    @Override
    public void deleteById(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDto updateById(int ownerID, int itemId, ItemDto itemDto) {
        checkUserExistence(ownerID);
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + itemId + " не найдена.");
        }
        if (item.get().getOwner().getId() != ownerID) {
            throw new WrongOwner("Указанный пользователь не является собственником вещи.");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.get().setAvailable(itemDto.getAvailable());
        }
        return itemMapper.toDto(itemRepository.save(item.get()));
    }

    private void checkUserExistence(int userId) {
        if (!userService.isExists(userId)) {
            throw new UserNotFound("Пользователь с ID " + userId + " не найден.");
        }
    }

}