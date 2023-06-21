package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto save(UserDto user) {
        return userMapper.toDto(userRepository.save(userMapper.fromDto(user)));
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFound("Пользователь с ID " + id + " не найден.");
        }
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateById(int id, User user) {
        Optional<User> userFromDB = userRepository.findById(id);
        if (userFromDB.isEmpty()) {
            throw new UserNotFound("Пользователь с ID " + id + " не найден.");
        }

        if (user.getName() != null && !user.getName().isEmpty()) {
            userFromDB.get().setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userFromDB.get().setEmail(user.getEmail());
        }
        return userMapper.toDto(userRepository.save(userFromDB.get()));
    }

    @Override
    public boolean isExists(int userId) {
        return userRepository.existsById(userId);
    }

}