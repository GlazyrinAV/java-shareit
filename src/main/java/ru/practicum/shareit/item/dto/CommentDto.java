package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {

    private Integer id;

    private String authorName;

    @NotBlank
    private String text;

    private LocalDateTime created;

}