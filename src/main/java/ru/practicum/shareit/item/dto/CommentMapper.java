package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public Comment fromDto(CommentDto dto, int userId, int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFound("Предмет с ID " + itemId + " не найден.");
        }
        return Comment.builder()
                .item(item.get())
                .author(userService.findById(userId))
                .text(dto.getText())
                .build();
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

}