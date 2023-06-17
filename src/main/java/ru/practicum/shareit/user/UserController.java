package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @PostMapping
    public User saveNew(@Valid @RequestBody User user) {

    }

    @GetMapping
    public Collection<User> findAll() {

    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {

    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {

    }

    @PatchMapping("/{id}")
    public User updateById(@PathVariable int id, @RequestBody User user) {

    }

}