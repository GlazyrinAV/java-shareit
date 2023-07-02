//package ru.practicum.shareit.item;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemMapper;
//import ru.practicum.shareit.item.storage.ItemStorage;
//import ru.practicum.shareit.user.UserServiceImpl;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class ItemServiceImpl implements ItemService {
//
//    private final ItemStorage itemStorage;
//    private final UserServiceImpl userService;
//    private final ItemMapper itemMapper;
//
//    public ItemDto saveNew(int ownerID, ItemDto itemDto) {
//        checkUserExistence(ownerID);
//        return itemMapper.toDto(itemStorage.save(itemMapper.fromDto(ownerID, itemDto)));
//    }
//
//    public Collection<ItemDto> findAllByUserId(Integer ownerId) {
//        checkUserExistence(ownerId);
//        return itemStorage.findAllById(ownerId).stream().map(itemMapper::toDto).collect(Collectors.toList());
//    }
//
//    public ItemDto findById(int id) {
//        return itemMapper.toDto(itemStorage.findById(id));
//    }
//
//    public Collection<ItemDto> findByName(String text) {
//        Collection<ItemDto> dtoList = new ArrayList<>();
//        if (text != null) {
//            dtoList = itemStorage.findByName(text).stream().map(itemMapper::toDto).collect(Collectors.toList());
//        }
//        return dtoList;
//    }
//
//    public void removeById(int id) {
//        itemStorage.deleteById(id);
//    }
//
//    public ItemDto updateById(int ownerID, int itemId, ItemDto itemDto) {
//        checkUserExistence(ownerID);
//        return itemMapper.toDto(itemStorage.updateById(ownerID, itemId, itemMapper.fromDto(ownerID, itemDto)));
//    }
//
//    private void checkUserExistence(int userId) {
//        if (!userService.isExists(userId)) {
//            throw new UserNotFound("Пользователь с ID " + userId + " не найден.");
//        }
//    }
//
//}