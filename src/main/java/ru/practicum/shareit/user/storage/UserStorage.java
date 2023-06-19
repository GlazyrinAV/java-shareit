package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {

    User saveNew(User user);

    Collection<User> findAll();

    User findById(int id);

    void removeById(int id);

    User updateById(int id, User user);

    boolean isExists(int userId);

}