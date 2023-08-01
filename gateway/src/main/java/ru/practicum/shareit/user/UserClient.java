package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

public class UserClient extends BaseClient {

    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> save(UserDto user) {
        return post("", user);
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(int id) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return get("/{id}", id, parameters);
    }

    public ResponseEntity<Object> removeById(int id) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return delete("/{id}", id, parameters);
    }

    public ResponseEntity<Object> updateById(int id, UserDto user) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return  patch("/{id}", id, parameters, user);
    }

}