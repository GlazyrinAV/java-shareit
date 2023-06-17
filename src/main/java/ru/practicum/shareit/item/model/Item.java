package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private int id;

    private String name;

    private String description;

    private boolean available;

    private final int ownerId;

}