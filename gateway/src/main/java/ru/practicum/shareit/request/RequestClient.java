package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

public class RequestClient extends BaseClient {

    public RequestClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> save(ItemRequestDto dto, int ownerId) {
        return post("", ownerId, dto);
    }

    public ResponseEntity<Object> findAll(int ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> findAllByUserId(int ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> findById(int ownerId, Integer requestId) {
        Map<String, Object> parameters = Map.of(
                "requestId", requestId
        );
        return get("/{requestId}", ownerId, parameters);
    }

}