package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User saveNew(User user) {

    }

    public Collection<User> findAll() {

    }

    public User findById(int id) {

    }

    public void removeById(int id) {

    }

    public User updateById(int id, User user) {

    }

}