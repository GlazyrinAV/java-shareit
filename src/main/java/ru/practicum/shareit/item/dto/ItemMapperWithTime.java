package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;

@Component
@RequiredArgsConstructor
public class ItemMapperWithTime {

    CommentRepository commentRepository;
    CommentMapper commentMapper;

    public ItemDtoWithTime toDtoWithTime(Item item) {
        return ItemDtoWithTime.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

}
