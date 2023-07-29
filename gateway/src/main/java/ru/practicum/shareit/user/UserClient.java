package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    private UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
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