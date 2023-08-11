package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
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
        Assertions.assertEquals(List.of(dto1), bookingService.findByOwner(BookingState.valueOf("ALL"), 1, 0, 1),
                "Ошибка при поиске бронирования по создателю с пагинацией.");
    }

    @Test
    void findByOwnerCurrentWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner(BookingState.valueOf("CURRENT"), 1, 0, 1));
    }

    @Test
    void findByOwnerFutureWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner(BookingState.valueOf("FUTURE"), 1, 0, 1));
    }

    @Test
    void findByOwnerPastWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner(BookingState.valueOf("PAST"), 2, 0, 1));
    }

    @Test
    void findByOwnerRejectedWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner(BookingState.valueOf("REJECTED"), 2, 0, 1));
    }

    @Test
    void findByOwnerWaitingWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByOwner(BookingState.valueOf("WAITING"), 2, 0, 1));
    }

    @Test
    void findByStateAllWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("ALL"), 1, 0, 1));
    }

    @Test
    void findByStateCurrentWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("CURRENT"), 2, 0, 1));
    }

    @Test
    void findByStateFutureWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("FUTURE"), 2, 0, 1));
    }

    @Test
    void findByStatePastWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("PAST"), 1, 0, 1));
    }

    @Test
    void findByStateRejectedWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("REJECTED"), 1, 0, 1));
    }

    @Test
    void findByStateWaitingWithPage() {
        Assertions.assertEquals(List.of(), bookingService.findByState(BookingState.valueOf("WAITING"), 1, 0, 1));
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