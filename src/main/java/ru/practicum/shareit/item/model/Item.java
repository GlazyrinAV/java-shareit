package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Item {

    private int id;

    private String name;

    private String description;

    private Boolean available;

    private final int ownerId;

}