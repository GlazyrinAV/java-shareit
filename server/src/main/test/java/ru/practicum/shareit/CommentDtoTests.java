package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonTest
class CommentDtoTests {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void commentDto() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.of(1994, 1, 1, 11, 11, 11))
                .build();

        JsonContent<CommentDto> result = json.write(commentDto);

        Assertions.assertEquals("{\"id\":1,\"authorName\":\"name\",\"text\":\"text\",\"created\":\"1994-01-01T11:11:11\"}",
                result.getJson(), "Ошибка при обработке CommentDto.");
    }

}