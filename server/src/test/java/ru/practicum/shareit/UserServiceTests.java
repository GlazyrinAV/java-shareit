package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository mockRepository;

    @Mock
    private UserMapper mockMapper;

    @InjectMocks
    private UserServiceImpl userService;

    static Stream<Integer> wrongIdParameters() {
        return Stream.of(-1, 0, 99);
    }

    @Test
    void saveUserNormal() {
        UserDto startDto = UserDto.builder()
                .name("User1")
                .email("user@email.com")
                .build();
        User user = new User(1, "User1", "user@email.com");
        User fromDto = User.builder()
                .name("User1")
                .email("user@email.com")
                .build();
        UserDto toDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("user@email.com")
                .build();
        Mockito
                .when(mockRepository.save(Mockito.any()))
                .thenReturn(user);

        Mockito
                .when(mockMapper.fromDto(Mockito.any()))
                .thenReturn(fromDto);

        Mockito
                .when(mockMapper.toDto(Mockito.any()))
                .thenReturn(toDto);
        assertEquals(toDto, userService.save(startDto),
                "Ошибка при нормальном сохранении нового пользователя.");
        verify(mockRepository, times(1)).save(Mockito.any());
    }

    @Test
    void findAllEmpty() {
        List<User> listOfUsers = new ArrayList<>();
        Mockito
                .when(mockRepository.findAll())
                .thenReturn(new ArrayList<>());
        assertEquals(listOfUsers, userService.findAll(),
                "Ошибка при получении пустого списка всех пользователей.");
    }

    @Test
    void findAllNormal() {
        List<User> listOfUsers = List.of(
                new User(1, "User1", "Email1@Email.com"),
                new User(2, "User2", "Email2@Email.com")
        );
        Mockito
                .when(mockRepository.findAll())
                .thenReturn(listOfUsers);
        assertEquals(listOfUsers, userService.findAll(),
                "Ошибка при нормальном получении списка всех пользователей.");
    }

    @Test
    void findByIdNormal() {
        User user = new User(1, "User1", "Email1@Email.com");
        Mockito
                .when(mockRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        assertEquals(user, userService.findById(1),
                "Ошибка при нормальном писке пользователя по ID.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongUser(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.findById(userId));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage());
    }

    @Test
    void deleteByIdNormal() {
        userService.deleteById(1);
        verify(mockRepository, Mockito.times(1))
                .deleteById(1);
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void deleteByIdWrongId(int userId) {
        userService.deleteById(userId);
        verify(mockRepository, Mockito.times(1))
                .deleteById(userId);
    }

    @Test
    void updateByIdNormal() {
        User user = new User(null, null, "new@email.com");
        User userFromDb = new User(1, "User1", "old@email.com");
        User userUpdated = new User(1, "User1", "new@email.com");
        UserDto toDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("new@email.com")
                .build();
        Mockito
                .when(mockRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(userFromDb));
        Mockito
                .when(mockRepository.save(Mockito.any()))
                .thenReturn(userUpdated);
        Mockito
                .when(mockMapper.toDto(Mockito.any()))
                .thenReturn(toDto);
        assertEquals(toDto, userService.updateById(1, user),
                "Ошибка при нормальном обновлении пользователя.");
        verify(mockRepository, times(1)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateByIdWrongId(int userId) {
        User user = new User(1, "User1", "user@email.com");
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.updateById(userId, user));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage());
        verify(mockRepository, never()).save(Mockito.any());
    }

    @Test
    void updateByIdBlankUser() {
        User user = new User(null, null, null);
        User userFromDb = new User(1, "User1", "old@email.com");
        User userUpdated = new User(1, "User1", "old@email.com");
        UserDto toDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("old@email.com")
                .build();
        Mockito
                .when(mockRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(userFromDb));
        Mockito
                .when(mockRepository.save(Mockito.any()))
                .thenReturn(userUpdated);
        Mockito
                .when(mockMapper.toDto(Mockito.any()))
                .thenReturn(toDto);
        assertEquals(toDto, userService.updateById(1, user),
                "Ошибка при обновлении пользователя при отсутствии полей для обновления.");
        verify(mockRepository, times(1)).save(Mockito.any());
    }

}