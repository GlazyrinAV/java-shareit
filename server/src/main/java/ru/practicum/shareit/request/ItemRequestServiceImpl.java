package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ItemRequestNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;

    private final ItemRequestMapper itemRequestMapper;

    private final UserRepository userRepository;

    @Override
    public ItemRequestDto save(ItemRequestDto dto, int ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        return itemRequestMapper.toDto(repository.save(itemRequestMapper.fromDto(user, dto)));
    }

    @Override
    public Collection<ItemRequestDto> findAll(int ownerId) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFound(ownerId));
        Collection<ItemRequest> requests = repository.findAll(ownerId);
        if (requests.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRequestMapper.toDto(requests);
    }

    @Override
    public Collection<ItemRequestDto> findOthersRequests(int userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        Pageable page = PageRequest.of(from == 0 ? 0 : from / size, size, Sort.by("created").descending());
        Collection<ItemRequest> requests = repository.findOthersRequests(userId, page).getContent();
        if (requests.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRequestMapper.toDto(requests);
    }

    @Override
    public ItemRequestDto findById(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() -> new ItemRequestNotFound(requestId));
        return itemRequestMapper.toDto(itemRequest);
    }
}