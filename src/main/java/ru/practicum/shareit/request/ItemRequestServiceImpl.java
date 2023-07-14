package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;

    @Override
    public ItemRequestDto save(ItemRequestDto dto, int ownerId) {
        return null;
    }

    @Override
    public Collection<ItemRequestDto> findAll(int ownerId) {
        return null;
    }

    @Override
    public Collection<ItemRequestDto> findAllByUserId(Integer start, Integer end) {
        return null;
    }

    @Override
    public ItemRequestDto findById(Integer requestId) {
        return null;
    }
}