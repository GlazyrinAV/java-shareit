package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(dto, itemService.findById(1, 2),
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
        assertEquals(dto, itemService.findById(1, 1),
                "Ошибка при поиске вещи собственником.");
    }

    @Test
    void saveWithoutRequest() {
        ItemDto fromDto = ItemDto.builder()
                .name("Item5")
                .description("description5")
                .available(true)
                .requestId(null)
                .build();
        ItemDto dto = ItemDto.builder()
                .id(5)
                .name(fromDto.getName())
                .description(fromDto.getDescription())
                .available(fromDto.getAvailable())
                .requestId(null)
                .build();
        assertEquals(dto, itemService.save(3, fromDto));
    }

    @Test
    void saveWithRequest() {
        ItemDto fromDto = ItemDto.builder()
                .name("Item5")
                .description("description5")
                .available(true)
                .requestId(3)
                .build();
        ItemDto dto = ItemDto.builder()
                .id(5)
                .name(fromDto.getName())
                .description(fromDto.getDescription())
                .available(fromDto.getAvailable())
                .requestId(3)
                .build();
        assertEquals(dto, itemService.save(3, fromDto));
    }

    @Test
    void  saveComment() {
        CommentDto fromDto = CommentDto.builder()
                .text("text")
                .build();
        CommentDto comment = itemService.saveComment(2, 1, fromDto);
        CommentDto commentDto = CommentDto.builder()
                .id(2)
                .text("text")
                .created(comment.getCreated())
                .authorName("User2")
                .build();
        assertEquals(commentDto, comment);
    }

}