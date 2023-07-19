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
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
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
    void findByOwnerAllWithPage() {
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
    void findByOwnerCurrentWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("CURRENT", 1, 0, 1));
    }

    @Test
    void findByOwnerFutureWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("FUTURE", 1, 0, 1));
    }

    @Test
    void findByOwnerPastWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("PAST", 2, 0, 1));
    }

    @Test
    void findByOwnerRejectedWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("REJECTED", 2, 0, 1));
    }

    @Test
    void findByOwnerWaitingWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("WAITING", 2, 0, 1));
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

    @Test
    void findByOwnerCurrentWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("CURRENT", 1, null, 1));
    }

    @Test
    void findByOwnerFutureWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("FUTURE", 1, null, 1));
    }

    @Test
    void findByOwnerPastWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("PAST", 2, null, 1));
    }

    @Test
    void findByOwnerRejectedWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("REJECTED", 2, null, 1));
    }

    @Test
    void findByOwnerWaitingWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner("WAITING", 2, null, 1));
    }

    @Test
    void findByStateAllWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("ALL", 1, 0, 1));
    }

    @Test
    void findByStateCurrentWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("CURRENT", 2, 0, 1));
    }

    @Test
    void findByStateFutureWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("FUTURE", 2, 0, 1));
    }

    @Test
    void findByStatePastWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("PAST", 1, 0, 1));
    }

    @Test
    void findByStateRejectedWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("REJECTED", 1, 0, 1));
    }

    @Test
    void findByStateWaitingWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("WAITING", 1, 0, 1));
    }

    @Test
    void findByStateAllWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("ALL", 1, null, 1));
    }

    @Test
    void findByStateCurrentWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("CURRENT", 2, null, 1));
    }

    @Test
    void findByStateFutureWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("FUTURE", 2, null, 1));
    }

    @Test
    void findByStatePastWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("PAST", 1, null, 1));
    }

    @Test
    void findByStateRejectedWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("REJECTED", 1, null, 1));
    }

    @Test
    void findByStateWaitingWithoutPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState("WAITING", 1, null, 1));
    }

    @Test
    void save() {
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
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2023, 12, 18, 9, 0))
                .end(LocalDateTime.of(2023, 12, 20, 9, 0))
                .build();
        BookingDto dto7 = BookingDto.builder()
                .id(7)
                .item(itemDto1)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.of(2023, 12, 18, 9, 0))
                .end(LocalDateTime.of(2023, 12, 20, 9, 0))
                .build();
        Assertions.assertEquals(dto7, bookingService.save(fromDto, 3));
    }

    @Test
    void saveWrongStart() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 12, 18, 9, 0))
                .end(LocalDateTime.of(2023, 12, 20, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        Assertions.assertEquals("Конец не может быть раньше начала.", exception.getMessage());
    }

    @Test
    void saveWrongEnd() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 12, 18, 9, 0))
                .end(LocalDateTime.of(2024, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        Assertions.assertEquals("Конец не может быть равен началу.", exception.getMessage());
    }

    @Test
    void saveWrongDuration() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(1980, 12, 18, 9, 0))
                .end(LocalDateTime.of(1981, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        Assertions.assertEquals("Промежуток не может быть в прошлом.", exception.getMessage());
    }

    @Test
    void saveWrongStartNull() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(null)
                .end(LocalDateTime.of(1981, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        Assertions.assertEquals("Не указан временной промежуток.", exception.getMessage());
    }


}