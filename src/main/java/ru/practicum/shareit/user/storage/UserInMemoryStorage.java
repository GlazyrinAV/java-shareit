package ru.practicum.shareit.user.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.exceptions.UserAlreadyExists;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
@Data
public class UserInMemoryStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();

    private static int id = 1;

    @Override
    public User saveNew(User user) {
        for (User userInMemory : users.values()) {
            if (userInMemory.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new UserAlreadyExists("Указана почта существующего пользователя.");
            }
        }
        user.setId(setId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        if (users.get(id) == null) {
            throw new UserNotFound("Пользователь с ID " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public void removeById(int id) {
        users.remove(id);
    }

    @Override
    public User updateById(int id, User user) {
        User oldUser = users.get(id);
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            HashMap<Integer, User> usersToCheck = new HashMap<>(users);
            usersToCheck.remove(id);
            for (User userInMemory : usersToCheck.values()) {
                if (userInMemory.getEmail().equalsIgnoreCase(user.getEmail())) {
                    throw new UserAlreadyExists("Указана почта существующего пользователя.");
                }
            }
            oldUser.setEmail(user.getEmail());
        }
        users.replace(id, oldUser);
        return oldUser;
    }

    private static int setId() {
        return id++;
    }

}