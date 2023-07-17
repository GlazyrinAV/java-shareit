package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class ItemIntergrationalTest {

    private final ItemService itemService;

    @Test

    void findByIdByOtherUser() {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .authorName("User2")
                .text("comment")
                .created(LocalDateTime.of(2022, 8, 18, 9, 0))
                .build();
        ItemDtoWithTime dto = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("description1")
                .available(true)
                .comments(List.of(commentDto))
                .build();
        Assertions.assertEquals(dto, itemService.findById(1, 2),
                "Ошибка при поиске вещи не собственником.");
    }

    @Test
    void findByIdByOwner() {
        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .id(5)
                .bookerId(2)
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .authorName("User2")
                .text("comment")
                .created(LocalDateTime.of(2022, 8, 18, 9, 0))
                .build();
        ItemDtoWithTime dto = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("description1")
                .available(true)
                .comments(List.of(commentDto))
                .lastBooking(bookingDtoShort)
                .build();
        Assertions.assertEquals(dto, itemService.findById(1, 1),
                "Ошибка при поиске вещи собственником.");
    }

}