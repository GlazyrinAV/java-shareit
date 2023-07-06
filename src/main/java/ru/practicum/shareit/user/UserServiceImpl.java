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
        return userRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateById(int id, User user) {
        User userFromDB = userRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
        Optional.ofNullable(user.getName()).ifPresent(name -> {
            if (!name.isBlank()) userFromDB.setName(name);
        });
        Optional.ofNullable(user.getEmail()).ifPresent(email -> {
            if (!email.isBlank()) userFromDB.setEmail(email);
        });
        return userMapper.toDto(userRepository.save(userFromDB));
    }

}