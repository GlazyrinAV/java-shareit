package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.exceptions.exceptions.ItemRequestNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class ItemRequestServiceTests {

    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);

    private final ItemRequestMapper mockItemRequestMapper = Mockito.mock(ItemRequestMapper.class);

    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);

    private final ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository,
            mockItemRequestMapper, mockUserRepository);

    static Stream<Integer> wrongIdParameters() {
        return Stream.of(-1, 0, 99);
    }

    @Test
    void saveNormal() {
        User user = new User(1, "User1", "Email@Email.com");
        ItemRequest request = new ItemRequest(1, user, "request", LocalDateTime.now(), null);
        ItemRequest fromDto = ItemRequest.builder()
                .owner(user)
                .description("request")
                .created(request.getCreated())
                .build();
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(request.getCreated())
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestMapper.fromDto(Mockito.any(), Mockito.any()))
                .thenReturn(fromDto);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.any(ItemRequest.class)))
                .thenReturn(toDto);
        Mockito
                .when(mockItemRequestRepository.save(Mockito.any()))
                .thenReturn(request);
        Assertions.assertEquals(toDto, itemRequestService.save(ItemRequestDto.builder().description("description").build(), 1),
                "Ошибка при нормальном сохранении нового запроса.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveWrongOwnerId(int ownerId) {
        ItemRequestDto dto = ItemRequestDto.builder().description("description").build();
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemRequestService.save(dto, ownerId));
        Assertions.assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении запроса с неправильным указанием пользователя.");
    }

    @Test
    void findAllEmpty() {
        Collection<ItemRequest> requests = new ArrayList<>();
        Collection<ItemRequestDto> toDto = new ArrayList<>();
        User user = new User(1, "User1", "Email@Email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestRepository.findAll(Mockito.anyInt()))
                .thenReturn(requests);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.anyCollection()))
                .thenReturn(toDto);
        Assertions.assertEquals(toDto, itemRequestService.findAll(1),
                "Ошибка при получении пустого списка всех запросов.");
    }

    @Test
    void findAllNormal() {
        User user = new User(1, "User1", "Email@Email.com");
        ItemRequest itemRequest = new ItemRequest(1, user, "description", LocalDateTime.now(), null);
        Collection<ItemRequest> requests = List.of(itemRequest);
        Collection<ItemRequestDto> toDto = List.of(ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(itemRequest.getCreated())
                .build()
        );
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestRepository.findAll(Mockito.anyInt()))
                .thenReturn(requests);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.anyCollection()))
                .thenReturn(toDto);
        Assertions.assertEquals(toDto, itemRequestService.findAll(1),
                "Ошибка при нормальном получении всех запросов.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findAllWrongOwnerId(int ownerId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemRequestService.findAll(ownerId));
        Assertions.assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при поиске запросов с неправильным указанием пользователя.");
    }

    @Test
    void findOthersRequestsNormalWithPages() {
        User user = new User(1, "User1", "Email@Email.com");
        ItemRequest itemRequest2 = new ItemRequest(2, user, "description2", LocalDateTime.now().minusDays(1), null);
        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
                .id(2)
                .description("description2")
                .created(itemRequest2.getCreated())
                .build();
        Page<ItemRequest> requests = new PageImpl<>(List.of(itemRequest2));
        Collection<ItemRequestDto> toDto = List.of(itemRequestDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestRepository.findOthersRequests(Mockito.anyInt(), Mockito.any()))
                .thenReturn(requests);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.anyCollection()))
                .thenReturn(toDto);
        Assertions.assertEquals(List.of(itemRequestDto2), itemRequestService.findOthersRequests(1, 1, 1),
                "Ошибка при нормальном получении чужих запросов с пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findOthersRequestsWrongIdWithPages(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemRequestService.findOthersRequests(userId, 1, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске чужих запросов с неправильным указанием пользователя с пагинацией.");
    }

    @Test
    void findOthersRequestsNormalWithoutPages() {
        User user = new User(1, "User1", "Email@Email.com");
        ItemRequest itemRequest = new ItemRequest(1, user, "description", LocalDateTime.now(), null);
        ItemRequest itemRequest2 = new ItemRequest(2, user, "description2", LocalDateTime.now().minusDays(1), null);
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(itemRequest.getCreated())
                .build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
                .id(2)
                .description("description2")
                .created(itemRequest2.getCreated())
                .build();
        List<ItemRequest> requests = List.of(itemRequest, itemRequest2);
        Collection<ItemRequestDto> toDto = List.of(itemRequestDto1, itemRequestDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestRepository.findOthersRequests(Mockito.anyInt()))
                .thenReturn(requests);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.anyCollection()))
                .thenReturn(toDto);
        Assertions.assertEquals(toDto, itemRequestService.findOthersRequests(1, null, 1),
                "Ошибка при нормальном получении чужих запросов без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findOthersRequestsWrongIdWithoutPages(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemRequestService.findOthersRequests(userId, null, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске чужих запросов с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findOthersRequestsFromNegative() {
        User user = new User(1, "User1", "Email@Email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemRequestService.findOthersRequests(1, -1, 1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске чужих запросов с неправильным указанием From.");
    }

    @Test
    void findOthersRequestsSizeNegative() {
        User user = new User(1, "User1", "Email@Email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemRequestService.findOthersRequests(1, 0, -1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске чужих запросов с неправильным указанием Size.");
    }

    @Test
    void findOthersRequestsSizeZero() {
        User user = new User(1, "User1", "Email@Email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemRequestService.findOthersRequests(1, 0, 0));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске чужих запросов с неправильным указанием Size.");
    }

    @Test
    void findByIdNormal() {
        User user = new User(1, "User1", "Email@Email.com");
        ItemRequest request = new ItemRequest(1, user, "request", LocalDateTime.now(), null);
        ItemRequest fromDto = ItemRequest.builder()
                .owner(user)
                .description("request")
                .created(request.getCreated())
                .build();
        ItemRequestDto toDto = ItemRequestDto.builder()
                .id(1)
                .description("request")
                .created(request.getCreated())
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRequestMapper.fromDto(Mockito.any(), Mockito.any()))
                .thenReturn(fromDto);
        Mockito
                .when(mockItemRequestMapper.toDto(Mockito.any(ItemRequest.class)))
                .thenReturn(toDto);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(request));
        Assertions.assertEquals(toDto, itemRequestService.findById(1, 1),
                "Ошибка при нормальном поиске запросов по номеру.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongUserId(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemRequestService.findById(userId, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске запросов по номеру с неправильным указанием пользователя.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongRequestId(int requestId) {
        User user = new User(1, "User1", "Email@Email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        ItemRequestNotFound exception = Assertions.assertThrows(ItemRequestNotFound.class, () -> itemRequestService.findById(1, requestId));
        Assertions.assertEquals("Запрос с ID " + requestId + " не найден.", exception.getMessage(),
                "Ошибка при поиске запросов по номеру с неправильным указанием запроса.");
    }

}