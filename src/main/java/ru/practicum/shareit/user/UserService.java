package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto save(User user);

    Collection<User> findAll();

    User findById(int id);

    void deleteById(int id);

    UserDto updateById(int id, User user);

    boolean isExists(int userId);

}
