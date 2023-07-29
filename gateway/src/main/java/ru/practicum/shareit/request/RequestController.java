package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ItemRequestDto dto,
                                       @RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        log.info("Creating request {}, userId={}", dto, ownerId);
        return requestClient.save(dto, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(Constants.OWNER_HEADER) int ownerId) {
        log.info("Get requests userId={}", ownerId);
        return requestClient.findAll(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(Constants.OWNER_HEADER) int userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get requests userId={}", userId);
        return requestClient.findAllByUserId(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(Constants.OWNER_HEADER) int userId,
                                           @PathVariable Integer requestId) {
        log.info("Get request={} userId={}", requestId, userId);
        return requestClient.findById(userId, requestId);
    }

}