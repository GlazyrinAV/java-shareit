package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemDtoTests {

    @Autowired
    private JacksonTester<ItemDto> json1;

    @Autowired
    private JacksonTester<ItemDtoWithTime> json2;

    @Test
    void itemDto() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        JsonContent<ItemDto> result = json1.write(itemDto);
        assertEquals("{\"id\":1,\"name\":\"name\",\"description\":\"description\",\"available\":true,\"requestId\":1}",
                result.getJson(), "Ошибка при обработке itemDto.");
    }

    @Test
    void itemDtoWithTime() throws IOException {
        BookingDtoShort bookingDtoShort1 = BookingDtoShort.builder()
                .id(1)
                .bookerId(1)
                .build();
        BookingDtoShort bookingDtoShort2 = BookingDtoShort.builder()
                .id(2)
                .bookerId(1)
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.of(1994, 1, 1, 11, 11, 11))
                .build();
        Collection<CommentDto> comments = List.of(commentDto);
        ItemDtoWithTime itemDtoWithTime = ItemDtoWithTime.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .nextBooking(bookingDtoShort1)
                .lastBooking(bookingDtoShort2)
                .comments(comments)
                .build();
        JsonContent<ItemDtoWithTime> result = json2.write(itemDtoWithTime);
        assertEquals("{\"id\":1,\"name\":\"name\",\"description\":\"description\",\"available\":true," +
                "\"lastBooking\":{\"id\":2,\"bookerId\":1},\"nextBooking\":{\"id\":1,\"bookerId\":1}," +
                "\"comments\":[{\"id\":1,\"authorName\":\"name\",\"text\":\"text\",\"created\":\"1994-01-01T11:11:11\"}]," +
                "\"requestId\":1}", result.getJson(), "Ошибка при обработке itemDtoWithTime.");
    }

    @Test
    void itemDtoWithTimeWithoutBooking() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.of(1994, 1, 1, 11, 11, 11))
                .build();
        Collection<CommentDto> comments = List.of(commentDto);
        ItemDtoWithTime itemDtoWithTime = ItemDtoWithTime.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .comments(comments)
                .build();
        JsonContent<ItemDtoWithTime> result = json2.write(itemDtoWithTime);
        assertEquals("{\"id\":1,\"name\":\"name\",\"description\":\"description\",\"available\":true," +
                "\"lastBooking\":null,\"nextBooking\":null,\"comments\":[{\"id\":1,\"authorName\":\"name\",\"text\":\"text\"," +
                "\"created\":\"1994-01-01T11:11:11\"}],\"requestId\":1}", result.getJson(),
                "Ошибка при обработке itemDtoWithTime без брони.");
    }

    @Test
    void itemDtoWithTimeWithoutComments() throws IOException {
        BookingDtoShort bookingDtoShort1 = BookingDtoShort.builder()
                .id(1)
                .bookerId(1)
                .build();
        BookingDtoShort bookingDtoShort2 = BookingDtoShort.builder()
                .id(2)
                .bookerId(1)
                .build();
        ItemDtoWithTime itemDtoWithTime = ItemDtoWithTime.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .nextBooking(bookingDtoShort1)
                .lastBooking(bookingDtoShort2)
                .build();
        JsonContent<ItemDtoWithTime> result = json2.write(itemDtoWithTime);
        assertEquals("{\"id\":1,\"name\":\"name\",\"description\":\"description\",\"available\":true," +
                "\"lastBooking\":{\"id\":2,\"bookerId\":1},\"nextBooking\":{\"id\":1,\"bookerId\":1},\"" +
                "comments\":null,\"requestId\":1}", result.getJson(), "Ошибка при обработке itemDtoWithTime.");
    }

}