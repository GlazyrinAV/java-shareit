//package ru.practicum.shareit.item;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.utils.Constants;
//
//import javax.validation.Valid;
//import java.util.Collection;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/items")
//public class ItemController {
//
//    private final ItemService itemService;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ItemDto saveNew(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
//                           @Valid @RequestBody ItemDto itemDto) {
//        return itemService.saveNew(ownerId, itemDto);
//    }
//
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public Collection<ItemDto> findAllByUserID(@RequestHeader(Constants.OWNER_HEADER) int ownerId) {
//        return itemService.findAllByUserId(ownerId);
//    }
//
//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ItemDto findById(@PathVariable int id) {
//        return itemService.findById(id);
//    }
//
//    @GetMapping("/search")
//    @ResponseStatus(HttpStatus.OK)
//    public Collection<ItemDto> findByName(@RequestParam String text) {
//        return itemService.findByName(text);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void removeById(@PathVariable int id) {
//        itemService.removeById(id);
//    }
//
//    @PatchMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ItemDto updateById(@RequestHeader(Constants.OWNER_HEADER) int ownerId,
//                              @PathVariable int id,
//                              @RequestBody ItemDto itemDto) {
//        return itemService.updateById(ownerId, id, itemDto);
//    }
//
//}