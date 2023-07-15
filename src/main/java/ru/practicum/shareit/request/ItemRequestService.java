package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto save(ItemRequestDto dto, int ownerId);

    Collection<ItemRequestDto> findAll(int ownerId);

    Collection<ItemRequestDto> findOthersRequests(int userId, Integer start, Integer size);

    ItemRequestDto findById(int userId, int requestId);

}