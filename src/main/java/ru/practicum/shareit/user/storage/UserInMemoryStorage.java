package ru.practicum.shareit.user.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.exceptions.UserAlreadyExists;
import ru.practicum.shareit.exceptions.exceptions.ValidationException;
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
        if (user.getName() == null || user.getEmail() == null) {
            throw new ValidationException("Неправильно указаны параметры нового пользователя.");
        }
        for (User userInMemory : users.values()) {
            if (userInMemory.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new UserAlreadyExists("Указана почта существующего пользователя.");
            }
        }
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
}