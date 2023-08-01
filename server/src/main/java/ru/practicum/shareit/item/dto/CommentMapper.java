package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment fromDto(CommentDto dto, User user, Item item) {
        return Comment.builder()
                .item(item)
                .author(user)
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

    public Collection<CommentDto> toDto(Collection<Comment> comments) {
        return comments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}