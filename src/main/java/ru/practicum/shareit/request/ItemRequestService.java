package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto save(ItemRequestDto dto, int ownerId);

    Collection<ItemRequestDto> findAll(int ownerId);

    Collection<ItemRequestDto> findAllByUserId(Integer start, Integer end);

    ItemRequestDto findById(Integer requestId);

}