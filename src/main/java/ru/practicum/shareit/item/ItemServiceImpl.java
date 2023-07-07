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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto save(int ownerId, ItemDto itemDto) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        return itemMapper.toDto(itemRepository.save(itemMapper.fromDto(user, itemDto)));
    }

    @Override
    public Collection<ItemDtoWithTime> findAllByUserId(Integer ownerId) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        Collection<ItemDtoWithTime> items = itemRepository.findAllWhereOwnerIdIn(ownerId).stream()
                .map(itemMapper::toDtoWithTime)
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
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFound(id));
        ItemDtoWithTime itemDtoWithTime = itemMapper.toDtoWithTime(item);
        Collection<CommentDto> comments = commentMapper.toDto(commentRepository.findByItem_Id(id));
        if (!comments.isEmpty()) {
            itemDtoWithTime.setComments(comments);
        } else {
            itemDtoWithTime.setComments(new ArrayList<>());
        }
        if (item.getOwner().getId() != ownerId) {
            return itemDtoWithTime;
        }
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStart(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        if (nextBooking != null) {
            itemDtoWithTime.setNextBooking(bookingMapper.toDtoShort(nextBooking));
        }
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        if (lastBooking != null) {
            itemDtoWithTime.setLastBooking(bookingMapper.toDtoShort(lastBooking));
        }
        return itemDtoWithTime;
    }

    @Override
    public Collection<ItemDto> findByName(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemMapper.toDto(itemRepository.findByName(text));
    }

    @Override
    public void deleteById(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDto updateById(int ownerID, int itemId, ItemDto itemDto) {
        userRepository.findById(ownerID).orElseThrow(() -> new UserNotFound(ownerID));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        if (item.getOwner().getId() != ownerID) {
            throw new WrongOwner("Указанный пользователь не является собственником вещи.");
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(name -> {
            if (!name.isBlank()) item.setName(name);
        });
        Optional.ofNullable(itemDto.getDescription()).ifPresent(desc -> {
            if (!desc.isBlank()) item.setDescription(desc);
        });
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public boolean isExists(int itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public CommentDto saveComment(int userId, int itemId, CommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        if (!isAvailableForComments(userId, itemId)) {
            throw new WrongParameter("Данный пользователь не может оставлять отзыв данной вещи.");
        }
        Comment newComment = commentMapper.fromDto(dto, user, item);
        newComment.setCreated(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(newComment));
    }

    private boolean isAvailableForComments(int userId, int itemId) {
        return bookingRepository.existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc(userId, itemId, LocalDateTime.now());
    }

}