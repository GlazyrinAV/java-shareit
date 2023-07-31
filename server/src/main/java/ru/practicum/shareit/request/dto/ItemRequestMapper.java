package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    private final ItemMapper itemMapper;

    public ItemRequest fromDto(User user, ItemRequestDto dto) {
        return ItemRequest.builder()
                .owner(user)
                .description(dto.getDescription())
                .build();

    }

    public ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
        Collection<Item> items = request.getItems();
        if (items != null) {
            dto.setItems(itemMapper.toDto(items));
        }
        return dto;
    }

    public Collection<ItemRequestDto> toDto(Collection<ItemRequest> requests) {
        return requests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}