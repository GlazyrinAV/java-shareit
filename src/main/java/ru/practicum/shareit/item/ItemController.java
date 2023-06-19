package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto saveNew(@RequestHeader("X-Sharer-User-Id") int ownerId,
                           @RequestBody ItemDto itemDto) {
        return itemService.saveNew(ownerId, itemDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> findAllByUserID(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.findAllByUserID(ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findById(int id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> findByName(@RequestParam Optional<String> text) {
        return itemService.findByName(text.get());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable int id) {
        itemService.removeById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateById(@RequestHeader("X-Sharer-User-Id") int ownerId,
                              @PathVariable int id,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateById(ownerId, id, itemDto);
    }

}