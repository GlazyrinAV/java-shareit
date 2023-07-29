package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceIntergrationalTest {

    private final UserService userService;

    @Test
    void saveNormal() {
        UserDto startDto = UserDto.builder()
                .name("User4")
                .email("user@email.com")
                .build();
        UserDto toDto = UserDto.builder()
                .id(4)
                .name("User4")
                .email("user@email.com")
                .build();
        assertEquals(toDto, userService.save(startDto),
                "Ошибка при нормальном сохранении нового пользователя.");
    }

}