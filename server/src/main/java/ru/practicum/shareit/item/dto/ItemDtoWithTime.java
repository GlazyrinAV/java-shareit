package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoWithTime {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoShort lastBooking;

    private BookingDtoShort nextBooking;

    private Collection<CommentDto> comments;

    private Integer requestId;

}