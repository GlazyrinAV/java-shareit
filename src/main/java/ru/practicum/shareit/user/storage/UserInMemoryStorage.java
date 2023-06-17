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

    private int id = 0;

    @Override
    public User saveNew(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public void removeById(int id) {
        users.remove(id);
    }

    @Override
    public User updateById(int id, User user) {
        User oldUser = users.get(id);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        users.replace(id, oldUser);
        return oldUser;
    }
}