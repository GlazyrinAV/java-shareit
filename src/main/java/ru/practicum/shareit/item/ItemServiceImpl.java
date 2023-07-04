package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongOwner;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;
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
    private final ItemMapperWithTime itemMapperWithTime;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto save(int ownerID, ItemDto itemDto) {
        checkUserExistence(ownerID);
        return itemMapper.toDto(itemRepository.save(itemMapper.fromDto(ownerID, itemDto)));
    }

    @Override
    public Collection<ItemDtoWithTime> findAllByUserId(Integer ownerId) {
        checkUserExistence(ownerId);
        Collection<ItemDtoWithTime> items = itemRepository.findAllWhereOwnerIdIn(ownerId).stream()
                .map(itemMapperWithTime::toDtoWithTime)
                .collect(Collectors.toList());
        for (ItemDtoWithTime item : items) {
            Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStart(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
            if (nextBooking != null) {
                item.setNextBooking(bookingMapper.toDtoShort(nextBooking));
            }
            Booking lastBooking = bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
            if (lastBooking != null) {
                item.setLastBooking(bookingMapper.toDtoShort(lastBooking));
            }
        }
        return items;
    }

    @Override
    public ItemDtoWithTime findById(int id, int ownerId) {
        Optional<Item> item = itemRepository.findById(id);
        checkItemExistence(item, id);
        ItemDtoWithTime itemDtoWithTime = itemMapperWithTime.toDtoWithTime(item.get());
        Collection<CommentDto> comments = commentRepository.findByItem_Id(id).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        if (!comments.isEmpty()) {
            itemDtoWithTime.setComments(comments);
        } else {
            itemDtoWithTime.setComments(new ArrayList<>());
        }
        if (item.get().getOwner().getId() != ownerId) {
            return itemDtoWithTime;
        }
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStart(item.get().getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        if (nextBooking != null) {
            itemDtoWithTime.setNextBooking(bookingMapper.toDtoShort(nextBooking));
        }
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(item.get().getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        if (lastBooking != null) {
            itemDtoWithTime.setLastBooking(bookingMapper.toDtoShort(lastBooking));
        }
        return itemDtoWithTime;
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
        checkItemExistence(item, itemId);
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

    @Override
    public boolean isExists(int itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public CommentDto saveComment(int userId, int itemId, CommentDto dto) {
        checkUserExistence(userId);
        Optional<Item> item = itemRepository.findById(itemId);
        checkItemExistence(item, itemId);
        if (!isAvailableForComments(userId, itemId)) {
            throw new WrongParameter("Данный пользователь не может оставлять отзыв данной вещи.");
        }
        Comment newComment = commentMapper.fromDto(dto, userId, itemId);
        newComment.setCreated(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(newComment));
    }

    private void checkUserExistence(int userId) {
        if (!userService.isExists(userId)) {
            throw new UserNotFound("Пользователь с ID " + userId + " не найден.");
        }
    }

    private void checkItemExistence(Optional<Item> item, int itemId) {
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + itemId + " не найден.");
        }
    }

    private boolean isAvailableForComments(int userId, int itemId) {
        Collection<BookingDto> bookings = bookingMapper.toDto(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
        for (BookingDto booking : bookings) {
            if (booking.getItem().getId() == itemId) {
                return true;
            }
        }
        return false;
    }

}