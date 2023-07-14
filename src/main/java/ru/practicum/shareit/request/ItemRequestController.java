package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto save(@Valid @RequestBody ItemRequestDto dto,
                               @RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        return requestService.save(dto, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findAll(@RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        return requestService.findAll(ownerId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findAllByUserId(@RequestParam Integer start,
                                                      @RequestParam Integer end) {
        return requestService.findAllByUserId(start, end);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto findById(@PathVariable Integer requestId) {
        return requestService.findById(requestId);
    }

}