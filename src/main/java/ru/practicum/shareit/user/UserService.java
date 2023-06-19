package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {

    User saveNew(User user);

    Collection<User> findAll();

    User findById(int id);

    void removeById(int id);

    User updateById(int id, User user);

}
