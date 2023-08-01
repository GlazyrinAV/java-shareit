package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UserDto user) {
        return userClient.save(user);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return userClient.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeById(@PathVariable int id) {
        return userClient.removeById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateById(@PathVariable int id,
                                             @RequestBody UserDto user) {
        if (user.getName() != null && user.getName().isBlank()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым.");
        }
        if (user.getEmail() != null && user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Почта пользователя не может быть пустой.");
        }
        return userClient.updateById(id, user);
    }

}