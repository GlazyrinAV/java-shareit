package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto save(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                        @RequestBody ItemDto itemDto) {
        return itemService.save(ownerId, itemDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoWithTime> findAllByUserID(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                                                       @RequestParam(required = false) Integer from,
                                                       @RequestParam(required = false) Integer size) {
        return itemService.findAllByUserId(ownerId, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithTime findById(@PathVariable int id,
                                    @RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        return itemService.findById(id, ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> findByName(@RequestParam String text,
                                          @RequestParam(required = false) Integer from,
                                          @RequestParam(required = false) Integer size) {
        return itemService.findByName(text, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        itemService.deleteById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateById(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                              @PathVariable int id,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateById(ownerId, id, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto saveComment(@RequestHeader(Constants.OWNER_HEADER) int userId,
                                  @PathVariable int itemId,
                                  @RequestBody CommentDto dto) {
        return itemService.saveComment(userId, itemId, dto);
    }

}