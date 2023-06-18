package ru.practicum.shareit.user.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
@Data
public class UserInMemoryStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User saveNew(User user) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }

    @Override
    public User updateById(int id, User user) {
        return null;
    }
}