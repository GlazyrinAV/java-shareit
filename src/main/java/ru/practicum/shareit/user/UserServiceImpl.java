package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User saveNew(User user) {
        return userStorage.saveNew(user);
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(int id) {
        return userStorage.findById(id);
    }

    @Override
    public void removeById(int id) {
        userStorage.removeById(id);
    }

    @Override
    public User updateById(int id, User user) {
        return userStorage.updateById(id, user);
    }

    @Override
    public boolean isExists(int userId) {
        return userStorage.isExists(userId);
    }

}