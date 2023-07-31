package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public class ItemClient extends BaseClient {

    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> save(int ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> findAllByUserID(int ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> findById(int id, int ownerId) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return get("/{id}", ownerId, parameters);
    }

    public ResponseEntity<Object> findByName(int ownerId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> deleteById(int userId, int id) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return delete("/{id}", userId, parameters);
    }

    public ResponseEntity<Object> updateById(int ownerId, int id, ItemDto itemDto) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return patch("/{id}", ownerId, parameters, itemDto);
    }

    public ResponseEntity<Object> saveComment(int userId, int itemId, CommentDto dto) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return post("/{itemId}/comment", userId, parameters, dto);
    }

}