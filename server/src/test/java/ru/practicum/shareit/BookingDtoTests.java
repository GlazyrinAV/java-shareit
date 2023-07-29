package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@JsonTest
class BookingDtoTests {

    @Autowired
    private JacksonTester<BookingDto> json1;

    @Autowired
    private JacksonTester<BookingDtoShort> json2;

    @Autowired
    private JacksonTester<NewBookingDto> json3;

    @Test
    void bookingDto() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .item(itemDto)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(1994, 1, 1, 11, 11, 11))
                .end(LocalDateTime.of(1994, 1, 1, 12, 11, 11))
                .build();
        JsonContent<BookingDto> resul = json1.write(bookingDto);
        assertEquals("{\"id\":1,\"item\":{\"id\":1,\"name\":\"name\",\"description\":\"description\"," +
                "\"available\":true,\"requestId\":1},\"booker\":{\"id\":1,\"name\":\"User1\",\"email\":\"email@email.com\"}," +
                "\"status\":\"WAITING\",\"start\":\"1994-01-01T11:11:11\",\"end\":\"1994-01-01T12:11:11\"}",
                resul.getJson(), "Ошибка при обработке bookingDto.");
    }

    @Test
    void bookingDtoShort() throws IOException {
        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .id(1)
                .bookerId(1)
                .build();
        JsonContent<BookingDtoShort> result = json2.write(bookingDtoShort);
        assertEquals("{\"id\":1,\"bookerId\":1}", result.getJson(),
                "Ошибка при обработке bookingDtoShort.");
    }

    @Test
    void newBookingDto() throws IOException {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(1994, 1, 1, 11, 11, 11))
                .end(LocalDateTime.of(1994, 1, 1, 12, 11, 11))
                .build();
        JsonContent<NewBookingDto> result = json3.write(newBookingDto);
        assertEquals("{\"itemId\":1,\"start\":\"1994-01-01T11:11:11\",\"end\":\"1994-01-01T12:11:11\"}",
                result.getJson(), "Ошибка при обработке newBookingDto.");
    }

}