package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class ItemRequestDto {

    private Integer id;

    @NotNull
    @NotBlank
    private String description;

    private LocalDateTime created;

    private Collection<ItemDto> items;

}