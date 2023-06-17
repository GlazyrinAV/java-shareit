package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @PostMapping
    public Item saveNew(@RequestAttribute("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {

    }

    @GetMapping
    public Collection<ItemDto> findAll() {

    }

    @GetMapping("/{id}")
    public ItemDto findById(int id) {

    }

    @DeleteMapping("/{id}")
    public void removebyId(int id) {

    }

    @PatchMapping
    public Item updateById(@RequestAttribute("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {

    }

}