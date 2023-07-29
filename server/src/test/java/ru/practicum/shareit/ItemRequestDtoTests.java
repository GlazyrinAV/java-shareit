package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemRequestDtoTests {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequest() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();
        Collection<ItemDto> items = List.of(itemDto);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .items(items)
                .build();
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertEquals("{\"id\":1,\"description\":\"description\",\"created\":null," +
                "\"items\":[{\"id\":1,\"name\":\"name\",\"description\":\"description\",\"available\":true,\"requestId\":1}]}",
                result.getJson(), "Ошибка при обработке itemRequestDto.");
    }
}