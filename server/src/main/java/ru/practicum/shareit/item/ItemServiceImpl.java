package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.exceptions.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.PageCheck;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto save(int ownerId, ItemDto itemDto) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ItemRequestNotFound(itemDto.getRequestId()));
            return itemMapper.toDto(itemRepository.save(itemMapper.fromDto(user, itemDto, request)));
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.fromDto(user, itemDto)));
    }

    @Override
    public Collection<ItemDtoWithTime> findAllByUserId(Integer ownerId, Integer from, Integer size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        Collection<ItemDtoWithTime> items;
        if (from == null || size == null) {
            items = itemMapper.toDtoWithTime(itemRepository.findAllWhereOwnerIdIn(ownerId));
        } else {
            if (from < 0 || size < 1) {
                throw new WrongParameter("Указаны неправильные параметры.");
            }
            Pageable page = PageRequest.of(from == 0 ? 0 : from / size, size);
            items = itemMapper.toDtoWithTime(itemRepository.findAllWhereOwnerIdIn(ownerId, page).getContent());
        }
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
    public Collection<ItemDto> findByName(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        if (PageCheck.isWithoutPage(from, size)) {
            return itemMapper.toDto(itemRepository.findByName(text));
        }
        Pageable page = PageRequest.of(from == 0 ? 0 : from / size, size);
        return itemMapper.toDto(itemRepository.findByName(text, page).getContent());
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
        return commentMapper.toDto(commentRepository.save(newComment));
    }

    private boolean isAvailableForComments(int userId, int itemId) {
        return !bookingRepository.exists(userId, itemId, LocalDateTime.now()).isEmpty();
    }

}