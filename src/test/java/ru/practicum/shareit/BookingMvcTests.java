package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingMvcTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewBooking() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto)
                .build();
        when(bookingService.save(any(), anyInt()))
                .thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(newBookingDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    void update() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .status(BookingStatus.APPROVED)
                .booker(userDto)
                .item(itemDto)
                .build();
        when(bookingService.update(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    void findById() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .status(BookingStatus.APPROVED)
                .booker(userDto)
                .item(itemDto)
                .build();
        when(bookingService.findById(anyInt(), anyInt()))
                .thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    void findByState() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .status(BookingStatus.APPROVED)
                .booker(userDto)
                .item(itemDto)
                .build();
        List<BookingDto> bookings = List.of(bookingDto);
        when(bookingService.findByState(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(bookings);
        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", 1)
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bookings.size())))
                .andExpect(jsonPath("$.[0].id", is(bookings.get(0).getId())))
                .andExpect(jsonPath("$.[0].start", is(bookings.get(0).getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookings.get(0).getEnd().toString())))
                .andExpect(jsonPath("$.[0].status", is(bookings.get(0).getStatus().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookings.get(0).getBooker().getId())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookings.get(0).getBooker().getName())))
                .andExpect(jsonPath("$.[0].booker.email", is(bookings.get(0).getBooker().getEmail())))
                .andExpect(jsonPath("$.[0].item.id", is(bookings.get(0).getItem().getId())))
                .andExpect(jsonPath("$.[0].item.name", is(bookings.get(0).getItem().getName())))
                .andExpect(jsonPath("$.[0].item.description", is(bookings.get(0).getItem().getDescription())))
                .andExpect(jsonPath("$.[0].item.available", is(bookings.get(0).getItem().getAvailable())));
    }

    @Test
    void findByOwner() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2011, 11, 11, 11, 11, 11))
                .end(LocalDateTime.of(2011, 12, 12, 12, 12, 12))
                .status(BookingStatus.APPROVED)
                .booker(userDto)
                .item(itemDto)
                .build();
        List<BookingDto> bookings = List.of(bookingDto);
        when(bookingService.findByOwner(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(bookings);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bookings.size())))
                .andExpect(jsonPath("$.[0].id", is(bookings.get(0).getId())))
                .andExpect(jsonPath("$.[0].start", is(bookings.get(0).getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookings.get(0).getEnd().toString())))
                .andExpect(jsonPath("$.[0].status", is(bookings.get(0).getStatus().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookings.get(0).getBooker().getId())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookings.get(0).getBooker().getName())))
                .andExpect(jsonPath("$.[0].booker.email", is(bookings.get(0).getBooker().getEmail())))
                .andExpect(jsonPath("$.[0].item.id", is(bookings.get(0).getItem().getId())))
                .andExpect(jsonPath("$.[0].item.name", is(bookings.get(0).getItem().getName())))
                .andExpect(jsonPath("$.[0].item.description", is(bookings.get(0).getItem().getDescription())))
                .andExpect(jsonPath("$.[0].item.available", is(bookings.get(0).getItem().getAvailable())));
    }

}