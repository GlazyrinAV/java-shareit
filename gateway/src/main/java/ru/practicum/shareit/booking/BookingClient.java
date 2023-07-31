package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

public class BookingClient extends BaseClient {

    public BookingClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getBookings(int userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(int userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> bookItem(int userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateItem(int userId, boolean status, Integer bookingId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", status
        );
        return patch("/{bookingId}?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(int userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

}