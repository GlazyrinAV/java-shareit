package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithTime;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemMvcTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewItem() throws Exception {
        ItemDto fromDto = ItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDto toDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        when(itemService.save(anyInt(), any()))
                .thenReturn(toDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.name", is(toDto.getName())))
                .andExpect(jsonPath("$.description", is(toDto.getDescription())))
                .andExpect(jsonPath("$.available", is(toDto.getAvailable())));
    }

    @Test
    void saveNewItemWrongName() throws Exception {
        ItemDto fromDto = ItemDto.builder()
                .name("")
                .description("Description1")
                .available(true)
                .build();
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveNewItemWrongDescription() throws Exception {
        ItemDto fromDto = ItemDto.builder()
                .name("name1")
                .description("")
                .available(true)
                .build();
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByUserID() throws Exception {
        ItemDtoWithTime toDto = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        List<ItemDtoWithTime> items = List.of(toDto);
        when(itemService.findAllByUserId(anyInt(), anyInt(), anyInt()))
                .thenReturn(items);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(items.size())))
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(items.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(items.get(0).getAvailable())));
    }

    @Test
    void findById() throws Exception {
        ItemDtoWithTime toDto = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        when(itemService.findById(anyInt(), anyInt()))
                .thenReturn(toDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.name", is(toDto.getName())))
                .andExpect(jsonPath("$.description", is(toDto.getDescription())))
                .andExpect(jsonPath("$.available", is(toDto.getAvailable())));
    }

    @Test
    void findByName() throws Exception {
        ItemDto toDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        List<ItemDto> items = List.of(toDto);
        when(itemService.findByName(anyString(), anyInt(), anyInt()))
                .thenReturn(items);
        mvc.perform(get("/items/search")
                        .param("text", "iTeM")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(items.size())))
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(items.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(items.get(0).getAvailable())));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateById() throws Exception {
        ItemDto fromDto = ItemDto.builder()
                .description("NewDescription1")
                .build();
        ItemDto toDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("NewDescription1")
                .available(true)
                .build();
        when(itemService.updateById(anyInt(), anyInt(), any()))
                .thenReturn(toDto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(fromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.name", is(toDto.getName())))
                .andExpect(jsonPath("$.description", is(toDto.getDescription())))
                .andExpect(jsonPath("$.available", is(toDto.getAvailable())));
    }

    @Test
    void saveComment() throws Exception {
        CommentDto commentFromDto = CommentDto.builder()
                .text("text")
                .build();
        CommentDto commentToDto = CommentDto.builder()
                .id(1)
                .text("text")
                .created(LocalDateTime.of(2022, 11, 11, 11,11,11))
                .authorName("name")
                .build();
        when(itemService.saveComment(anyInt(), anyInt(), any()))
                .thenReturn(commentToDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentFromDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentToDto.getId())))
                .andExpect(jsonPath("$.text", is(commentToDto.getText())))
                .andExpect(jsonPath("$.created", is(commentToDto.getCreated().toString())))
                .andExpect(jsonPath("$.authorName", is(commentToDto.getAuthorName())));
    }

}