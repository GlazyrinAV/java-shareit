package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
@ExtendWith(MockitoExtension.class)
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
        assertEquals(List.of(dto1), bookingService.findByOwner(BookingState.ALL, 1, 0, 1),
                "Ошибка при поиске бронирования по создателю с пагинацией.");
    }

    @Test
    void findByOwnerCurrentWithPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.CURRENT, 1, 0, 1));
    }

    @Test
    void findByOwnerFutureWithPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.FUTURE, 1, 0, 1));
    }

    @Test
    void findByOwnerPastWithPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.PAST, 2, 0, 1));
    }

    @Test
    void findByOwnerRejectedWithPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.REJECTED, 2, 0, 1));
    }

    @Test
    void findByOwnerWaitingWithPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.WAITING, 2, 0, 1));
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
        assertEquals(List.of(dto1, dto2, dto3, dto4, dto5, dto6), bookingService.findByOwner(BookingState.PAST, 1, null, 1),
                "Ошибка при поиске бронирования по создателю с пагинацией.");
    }

    @Test
    void findByOwnerCurrentWithoutPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.CURRENT, 1, null, 1));
    }

    @Test
    void findByOwnerFutureWithoutPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.FUTURE, 1, null, 1));
    }

    @Test
    void findByOwnerPastWithoutPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.PAST, 2, null, 1));
    }

    @Test
    void findByOwnerRejectedWithoutPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.REJECTED, 2, null, 1));
    }

    @Test
    void findByOwnerWaitingWithoutPage() {
        assertEquals(List.of(), bookingService.findByOwner(BookingState.WAITING, 2, null, 1));
    }

    @Test
    void findByStateAllWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.ALL, 1, 0, 1));
    }

    @Test
    void findByStateCurrentWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.CURRENT, 2, 0, 1));
    }

    @Test
    void findByStateFutureWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.FUTURE, 2, 0, 1));
    }

    @Test
    void findByStatePastWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.PAST, 1, 0, 1));
    }

    @Test
    void findByStateRejectedWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.REJECTED, 1, 0, 1));
    }

    @Test
    void findByStateWaitingWithPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.WAITING, 1, 0, 1));
    }

    @Test
    void findByStateAllWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.ALL, 1, null, 1));
    }

    @Test
    void findByStateCurrentWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.CURRENT, 2, null, 1));
    }

    @Test
    void findByStateFutureWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.FUTURE, 2, null, 1));
    }

    @Test
    void findByStatePastWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.PAST, 1, null, 1));
    }

    @Test
    void findByStateRejectedWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.REJECTED, 1, null, 1));
    }

    @Test
    void findByStateWaitingWithoutPage() {
        assertEquals(List.of(), bookingService.findByState(BookingState.WAITING, 1, null, 1));
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
        assertEquals(dto7, bookingService.save(fromDto, 3));
    }

    @Test
    void saveWrongStart() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 12, 18, 9, 0))
                .end(LocalDateTime.of(2023, 12, 20, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        assertEquals("Конец не может быть раньше начала.", exception.getMessage());
    }

    @Test
    void saveWrongEnd() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 12, 18, 9, 0))
                .end(LocalDateTime.of(2024, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        assertEquals("Конец не может быть равен началу.", exception.getMessage());
    }

    @Test
    void saveWrongDuration() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(1980, 12, 18, 9, 0))
                .end(LocalDateTime.of(1981, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        assertEquals("Промежуток не может быть в прошлом.", exception.getMessage());
    }

    @Test
    void saveWrongStartNull() {
        NewBookingDto fromDto = NewBookingDto.builder()
                .itemId(1)
                .start(null)
                .end(LocalDateTime.of(1981, 12, 18, 9, 0))
                .build();
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> bookingService.save(fromDto, 3));
        assertEquals("Не указан временной промежуток.", exception.getMessage());
    }


}