package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserMvcTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewUser() throws Exception {
        UserDto fromDto = UserDto.builder()
                .name("user1")
                .email("email@email.com")
                .build();
        UserDto toDto = UserDto.builder()
                .id(1)
                .name("user1")
                .email("email@email.com")
                .build();
        when(userService.save(any()))
                .thenReturn(toDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(fromDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.name", is(toDto.getName())))
                .andExpect(jsonPath("$.email", is(toDto.getEmail())));
    }

    @Test
    void saveNewUserWrongEmail() throws Exception {
        UserDto fromDto = UserDto.builder()
                .name("user1")
                .email("email")
                .build();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(fromDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveNewUserWrongName() throws Exception {
        UserDto fromDto = UserDto.builder()
                .name("")
                .email("email")
                .build();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(fromDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll() throws Exception {
        User user = User.builder()
                .id(1)
                .name("user1")
                .email("email@email.com")
                .build();
        List<User> users = List.of(user);
        when(userService.findAll())
                .thenReturn(users);
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(users.size())))
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())));
    }

    @Test
    void findById() throws Exception {
        User user = User.builder()
                .id(1)
                .name("user1")
                .email("email@email.com")
                .build();
        when(userService.findById(anyInt()))
                .thenReturn(user);
        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void removeById() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateById() throws Exception {
        UserDto fromDto = UserDto.builder()
                .email("newemail@email.com")
                .build();
        UserDto toDto = UserDto.builder()
                .id(1)
                .name("user1")
                .email("newemail@email.com")
                .build();
        when(userService.updateById(anyInt(), any()))
                .thenReturn(toDto);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(fromDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toDto.getId())))
                .andExpect(jsonPath("$.name", is(toDto.getName())))
                .andExpect(jsonPath("$.email", is(toDto.getEmail())));
    }

}