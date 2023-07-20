package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

@JsonTest
class UserDtoTests {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void userDto() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();

        JsonContent<UserDto> result = json.write(userDto);

        Assertions.assertEquals("{\"id\":1,\"name\":\"User1\",\"email\":\"email@email.com\"}", result.getJson(),
                "Ошибка при конвертации userDto.");
    }

}