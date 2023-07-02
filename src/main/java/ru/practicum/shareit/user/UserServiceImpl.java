package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final UserDtoMapper userDtoMapper;

    public UserServiceImpl(UserStorage userStorage, UserDtoMapper userDtoMapper) {
        this.userStorage = userStorage;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserDto save(User user) {
        return userDtoMapper.mapToDto(userStorage.save(user));
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(int id) {
        return userStorage.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        userStorage.deleteById(id);
    }

    @Override
    public UserDto updateById(int id, User user) {
        User userFromDB = userStorage.findById(id).get();
        if (user.getName() != null && !user.getName().isEmpty()) {
            userFromDB.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userFromDB.setEmail(user.getEmail());
        }
        return userDtoMapper.mapToDto(userStorage.save(userFromDB));
    }

    @Override
    public boolean isExists(int userId) {
        return userStorage.existsById(userId);
    }

}