package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto saveNew(@RequestHeader("X-Sharer-User-Id") int ownerId, @Valid @RequestBody ItemDto itemDto) {

    }

    @GetMapping
    public Collection<ItemDto> findAllByUserID(@RequestHeader("X-Sharer-User-Id") int ownerId) {

    }

    @GetMapping("/{id}")
    public ItemDto findById(int id) {

    }

    @GetMapping("/search")
    public Collection<ItemDto> findByName(@RequestParam Optional<String> text) {

    }

    @DeleteMapping("/{id}")
    public void removeById(int id) {

    }

    @PatchMapping
    public ItemDto updateById(@RequestHeader("X-Sharer-User-Id") int ownerId, @Valid @RequestBody ItemDto itemDto) {

    }

}