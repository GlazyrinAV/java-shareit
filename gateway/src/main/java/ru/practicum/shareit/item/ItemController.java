package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                                       @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, ownerId);
        return itemClient.save(ownerId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserID(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items userId={}", ownerId);
        return itemClient.findAllByUserID(ownerId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id,
                                           @RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        log.info("Get item={}, userId={}", id, ownerId);
        return itemClient.findById(id, ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByName(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                                             @RequestParam String text,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items with {}", text);
        return itemClient.findByName(ownerId, text, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@RequestHeader(Constants.OWNER_HEADER) int userId,
                                             @PathVariable int id) {
        log.info("Delete item {}", id);
        return itemClient.deleteById(userId, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateById(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
                                             @PathVariable int id,
                                             @RequestBody ItemDto itemDto) {
        if (itemDto.getName() != null && itemDto.getName().isBlank()) {
            throw new IllegalArgumentException("Название предмета не может быть пустым.");
        }
        if (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание предмета не может быть пустым.");
        }
        log.info("Updating item {}", id);
        return itemClient.updateById(ownerId, id, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(Constants.OWNER_HEADER) int userId,
                                              @PathVariable int itemId,
                                              @RequestBody @Valid CommentDto dto) {
        log.info("Creating comment for item {}, userId={}",itemId, userId);
        return itemClient.saveComment(userId, itemId, dto);
    }

}