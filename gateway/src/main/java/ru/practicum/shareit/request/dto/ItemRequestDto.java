package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank
    private String description;

}