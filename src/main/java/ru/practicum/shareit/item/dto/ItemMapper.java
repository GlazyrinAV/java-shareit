package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    public Item fromDto(User user, ItemDto itemDto) {
        return Item.builder()
                .owner(user)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public Item fromDto(User user, ItemDto itemDto, ItemRequest request) {
        return Item.builder()
                .owner(user)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .itemRequest(request)
                .build();
    }

    public ItemDto toDto(Item item) {
        ItemDto dto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getItemRequest() != null) {
            dto.setRequestId(item.getItemRequest().getId());
        }
        return dto;
    }

    public Collection<ItemDto> toDto(Collection<Item> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ItemDtoWithTime toDtoWithTime(Item item) {
        ItemDtoWithTime dto = ItemDtoWithTime.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(new ArrayList<>())
                .build();
        if (item.getItemRequest() != null) {
            dto.setRequestId(item.getItemRequest().getId());
        }
        return dto;
    }

    public ItemDtoWithTime toDtoWithTimeWithComments(Item item) {
        ItemDtoWithTime dto = ItemDtoWithTime.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(commentMapper.toDto(item.getComments()))
                .build();
        if (item.getItemRequest() != null) {
            dto.setRequestId(item.getItemRequest().getId());
        }
        return dto;
    }

}