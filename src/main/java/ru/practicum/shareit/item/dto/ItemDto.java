package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

}