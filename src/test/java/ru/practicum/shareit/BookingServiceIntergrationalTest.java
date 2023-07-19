package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class BookingServiceIntergrationalTest {

    private final BookingService bookingService;

    @Test
    void findByOwnerWithPage() {
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description1")
                .available(true)
                .build();
        UserDto userDto = UserDto.builder()
                .id(3)
                .name("User3")
                .email("email3@email.com")
                .build();
        BookingDto dto1 = BookingDto.builder()
                .id(4)
                .item(itemDto1)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2022, 12, 18, 9, 0))
                .end(LocalDateTime.of(2022, 12, 20, 9, 0))
                .build();
        Assertions.assertEquals(List.of(dto1), bookingService.findByOwner("ALL", 1, 0, 1),
                "Ошибка при поиске бронирования по создателю с пагинацией.");
    }

    @Test
    void findByOwnerWithoutPage() {
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description1")
                .available(true)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Item2")
                .description("description2")
                .available(true)
                .build();
        UserDto userDto = UserDto.builder()
                .id(3)
                .name("User3")
                .email("email3@email.com")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2)
                .name("User2")
                .email("email2@email.com")
                .build();
        BookingDto dto1 = BookingDto.builder()
                .id(4)
                .item(itemDto1)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2022, 12, 18, 9, 0))
                .end(LocalDateTime.of(2022, 12, 20, 9, 0))
                .build();
        BookingDto dto2 = BookingDto.builder()
                .id(2)
                .item(itemDto1)
                .booker(userDto2)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2022, 10, 18, 9, 0))
                .end(LocalDateTime.of(2022, 11, 18, 9, 0))
                .build();
        BookingDto dto3 = BookingDto.builder()
                .id(3)
                .item(itemDto2)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2022, 10, 18, 9, 0))
                .end(LocalDateTime.of(2022, 11, 18, 9, 0))
                .build();
        BookingDto dto4 = BookingDto.builder()
                .id(1)
                .item(itemDto1)
                .booker(userDto2)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2022, 8, 18, 9, 0))
                .end(LocalDateTime.of(2022, 9, 18, 9, 0))
                .build();
        BookingDto dto5 = BookingDto.builder()
                .id(5)
                .item(itemDto1)
                .booker(userDto2)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.of(2021, 8, 18, 9, 0))
                .end(LocalDateTime.of(2021, 9, 18, 9, 0))
                .build();
        BookingDto dto6 = BookingDto.builder()
                .id(6)
                .item(itemDto1)
                .booker(userDto2)
                .status(BookingStatus.REJECTED)
                .start(LocalDateTime.of(2020, 8, 18, 9, 0))
                .end(LocalDateTime.of(2020, 9, 18, 9, 0))
                .build();
        Assertions.assertEquals(List.of(dto1, dto2, dto3, dto4, dto5, dto6), bookingService.findByOwner("PAST", 1, null, 1),
                "Ошибка при поиске бронирования по создателю с пагинацией.");
    }

}