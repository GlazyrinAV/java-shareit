package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingDto {

    @NotNull
    private Integer itemId;

    @FutureOrPresent
    private  LocalDateTime start;

    @Future
    private LocalDateTime end;

}