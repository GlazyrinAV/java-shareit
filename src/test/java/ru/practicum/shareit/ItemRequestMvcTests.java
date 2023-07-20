package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestMvcTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewItemRequest() throws Exception {
        ItemRequest fromDto = ItemRequest.builder()
                .description("request")
                .build();
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11))
                .build();
        when(itemRequestService.save(any(), anyInt()))
                .thenReturn(toDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.description", is(toDto.getDescription())))
                .andExpect(jsonPath("$.created", is(toDto.getCreated().toString())));
    }

    @Test
    void saveNewItemRequestWrongDescription() throws Exception {
        ItemRequest fromDto = ItemRequest.builder()
                .description("")
                .build();
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll() throws Exception {
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11))
                .build();
        List<ItemRequestDto> requests = List.of(toDto);
        when(itemRequestService.findAll(anyInt()))
                .thenReturn(requests);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(requests.size())))
                .andExpect(jsonPath("$[0].id", is(requests.get(0).getId())))
                .andExpect(jsonPath("$[0].description", is(requests.get(0).getDescription())))
                .andExpect(jsonPath("$[0].created", is(requests.get(0).getCreated().toString())));
    }

    @Test
    void findAllByUserId() throws Exception {
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11))
                .build();
        List<ItemRequestDto> requests = List.of(toDto);
        when(itemRequestService.findOthersRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(requests);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(requests.size())))
                .andExpect(jsonPath("$[0].id", is(requests.get(0).getId())))
                .andExpect(jsonPath("$[0].description", is(requests.get(0).getDescription())))
                .andExpect(jsonPath("$[0].created", is(requests.get(0).getCreated().toString())));
    }

    @Test
    void findById() throws Exception {
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11))
                .build();
        when(itemRequestService.findById(anyInt(), anyInt()))
                .thenReturn(toDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.description", is(toDto.getDescription())))
                .andExpect(jsonPath("$.created", is(toDto.getCreated().toString())));
    }

}