package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.strategies.by.owner.FindByOwnerAll;
import ru.practicum.shareit.booking.strategies.by.owner.StrategyByOwnerFactory;
import ru.practicum.shareit.booking.strategies.by.state.FindByStateAll;
import ru.practicum.shareit.booking.strategies.by.state.StrategyByStateFactory;
import ru.practicum.shareit.exceptions.exceptions.BookingNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongEnumParameter;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTests {

    static Stream<Integer> wrongIdParameters() {
        return Stream.of(-1, 0, 99);
    }

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ItemRepository mockItemRepository;

    @Mock
    private BookingRepository mockBookingRepository;

    @Mock
    private BookingMapper mockBookingMapper;

    @Mock
    private StrategyByOwnerFactory mockStrategyByOwnerFactory;

    @Mock
    private StrategyByStateFactory mockStrategyByStateFactory;

    @Mock
    private FindByStateAll mockFindByStateAll;

    @Mock
    private FindByOwnerAll mockFindByOwnerAll;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void saveNormal() {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        Item item = new Item(1, "Item1", "description", true, owner, null);
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, newBookingDto.getStart(), newBookingDto.getEnd());
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto)
                .build();
        Mockito
                .when(mockBookingMapper.fromDto(Mockito.any(), Mockito.any()))
                .thenReturn(newBooking);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(mockBookingRepository.save(Mockito.any()))
                .thenReturn(newBooking);
        Mockito
                .when(mockBookingMapper.toDto(Mockito.any(Booking.class)))
                .thenReturn(bookingDto);
        assertEquals(bookingDto, bookingService.save(newBookingDto, 1),
                "Ошибка при нормальном сохранении нового бронирования.");
        verify(mockBookingRepository, times(1)).save(Mockito.any());
    }

    @Test
    void saveNormalByOwner() {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        Item item = new Item(1, "Item1", "description", true, owner, null);
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.save(newBookingDto, 2));
        assertEquals("Собственник не может создавать запрос на свою вещь.", exception.getMessage());
        verify(mockBookingRepository, never()).save(Mockito.any());
    }

    @Test
    void saveNormalItemUnavailable() {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        Item item = new Item(1, "Item1", "description", false, owner, null);
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, newBookingDto.getStart(), newBookingDto.getEnd());
        Mockito
                .when(mockBookingMapper.fromDto(Mockito.any(), Mockito.any()))
                .thenReturn(newBooking);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        WrongParameter exception = assertThrows(WrongParameter.class, () -> bookingService.save(newBookingDto, 1));
        assertEquals("Предмет с ID 1 не доступен.", exception.getMessage());
        verify(mockBookingRepository, never()).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveWrongUserId(int userId) {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.save(newBookingDto, userId));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении нового бронирования с неправильным указанием пользователя.");
        verify(mockBookingRepository, never()).save(Mockito.any());
    }

    @Test
    void updateNormal() {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        Item item = new Item(1, "Item1", "description", true, owner, null);
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Booking newBookingAfterUpdate = new Booking(1, item, user, BookingStatus.APPROVED, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .status(BookingStatus.APPROVED)
                .booker(userDto)
                .item(itemDto)
                .build();
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(newBooking));
        Mockito
                .when(mockBookingRepository.save(Mockito.any()))
                .thenReturn(newBookingAfterUpdate);
        Mockito
                .when(mockBookingMapper.toDto(Mockito.any(Booking.class)))
                .thenReturn(bookingDto);
        assertEquals(bookingDto, bookingService.update(1, 2, true),
                "Ошибка при нормальном подтверждении бронирования.");
        verify(mockBookingRepository, times(1)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateWrongUserId(int ownerId) {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        Item item = new Item(1, "Item1", "description", true, owner, null);
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(newBooking));
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.update(1, ownerId, true));
        assertEquals("Данный запрос может сделать только собственник предмета.", exception.getMessage(),
                "Ошибка при подтверждении бронирования с неправильным указанием собственника.");
        verify(mockBookingRepository, never()).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateWrongBookingId(int bookingId) {
        BookingNotFound exception = assertThrows(BookingNotFound.class, () -> bookingService.update(bookingId, 2, true));
        assertEquals("Бронь с ID " + bookingId + " не найдена.", exception.getMessage(),
                "Ошибка при подтверждении бронирования с неправильным указанием бронирования.");
        verify(mockBookingRepository, never()).save(Mockito.any());
    }

    @Test
    void findByIdNormal() {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("User1")
                .email("email@email.com")
                .build();
        Item item = new Item(1, "Item1", "description", true, owner, null);
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, newBookingDto.getStart(), newBookingDto.getEnd());
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto)
                .build();
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(newBooking));
        Mockito
                .when(mockBookingMapper.toDto(Mockito.any(Booking.class)))
                .thenReturn(bookingDto);
        assertEquals(bookingDto, bookingService.findById(1, 2),
                "Ошибка при нормальном поиске брони.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongUserId(int ownerId) {
        User user = new User(1, "User1", "email@email.com");
        User owner = new User(2, "User2", "email2@email.com");
        Item item = new Item(1, "Item1", "description", true, owner, null);
        Booking newBooking = new Booking(1, item, user, BookingStatus.WAITING, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(newBooking));
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.findById(1, ownerId));
        assertEquals("Данный запрос может сделать только собственник предмета или бронирующий.", exception.getMessage(),
                "Ошибка при поиске бронирования с неправильным указанием собственника.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongBookingId(int bookingId) {
        BookingNotFound exception = assertThrows(BookingNotFound.class, () -> bookingService.findById(bookingId, 2));
        assertEquals("Бронь с ID " + bookingId + " не найдена.", exception.getMessage(),
                "Ошибка при поиске бронирования с неправильным указанием бронирования.");
    }

    @Test
    void findByStateNormalWithPage() {
        User user = new User(1, "User1", "email@email.com");
        UserDto userDto = UserDto.builder()
                .id(2)
                .name("User2")
                .email("email2@email.com")
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto2)
                .build();

        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByStateFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByStateAll);
        Mockito
                .when(mockFindByStateAll.findByBookingState(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDto2));
        assertEquals(List.of(bookingDto2), bookingService.findByState(BookingState.ALL, 1, 0, 1),
                "Ошибка при поске брони по состоянию с пагинацией.");
    }

    @Test
    void findByStateNormalWithoutPage() {
        User user = new User(1, "User1", "email@email.com");
        UserDto userDto = UserDto.builder()
                .id(2)
                .name("User2")
                .email("email2@email.com")
                .build();
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto1)
                .build();
        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto2)
                .build();
        List<BookingDto> requsts = List.of(bookingDto1, bookingDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByStateFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByStateAll);
        Mockito
                .when(mockFindByStateAll.findByBookingState(Mockito.anyInt(), Mockito.isNull(), Mockito.anyInt()))
                .thenReturn(requsts);
        assertEquals(requsts, bookingService.findByState(BookingState.ALL, 1, null, 1),
                "Ошибка при поске брони по состоянию без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByStateWrongUserIdWithPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.findByState(BookingState.ALL, userId, 0, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по состоянию с неправильным указанием пользователя с пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByStateWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.findByState(BookingState.ALL, userId, null, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по состоянию с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findByStateFromNegative() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByStateFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByStateAll);
        Mockito
                .when(mockFindByStateAll.findByBookingState(1, -1, 1))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByState(BookingState.ALL, 1, -1, 1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по состоянию с неправильным указанием From.");
    }

    @Test
    void findByStateWrongSizeNegative() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByStateFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByStateAll);
        Mockito
                .when(mockFindByStateAll.findByBookingState(1, 0, -1))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByState(BookingState.ALL, 1, 0, -1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по состоянию с неправильным указанием Size.");
    }

    @Test
    void findByStateWrongSizeZero() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByStateFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByStateAll);
        Mockito
                .when(mockFindByStateAll.findByBookingState(1, 0, 0))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByState(BookingState.ALL, 1, 0, 0));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по состоянию с неправильным указанием Size.");
    }

    @Test
    void findByOwnerNormalWithPage() {
        User user = new User(1, "User1", "email@email.com");
        UserDto userDto = UserDto.builder()
                .id(2)
                .name("User2")
                .email("email2@email.com")
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto2)
                .build();

        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByOwnerFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByOwnerAll);
        Mockito
                .when(mockFindByOwnerAll.findByBookingState(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDto2));
        assertEquals(List.of(bookingDto2), bookingService.findByOwner(BookingState.ALL, 1, 0, 1),
                "Ошибка при поске брони по собственнику с пагинацией.");
    }

    @Test
    void findByOwnerNormalWithoutPage() {
        User user = new User(1, "User1", "email@email.com");
        UserDto userDto = UserDto.builder()
                .id(2)
                .name("User2")
                .email("email2@email.com")
                .build();
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Item1")
                .description("description")
                .available(true)
                .build();
        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto1)
                .build();
        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(BookingStatus.WAITING)
                .booker(userDto)
                .item(itemDto2)
                .build();
        List<BookingDto> requsts = List.of(bookingDto1, bookingDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByOwnerFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByOwnerAll);
        Mockito
                .when(mockFindByOwnerAll.findByBookingState(Mockito.anyInt(), Mockito.isNull(), Mockito.anyInt()))
                .thenReturn(requsts);
        assertEquals(requsts, bookingService.findByOwner(BookingState.ALL, 1, null, 1),
                "Ошибка при поске брони по собственнику без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByOwnerWrongUserIdWithPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.findByOwner(BookingState.ALL, userId, 0, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по собственнику с неправильным указанием пользователя с пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByOwnerWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> bookingService.findByOwner(BookingState.ALL, userId, null, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по собственнику с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findByOwnerFromNegative() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByOwnerFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByOwnerAll);
        Mockito
                .when(mockFindByOwnerAll.findByBookingState(1, -1, 1))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner(BookingState.ALL, 1, -1, 1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным указанием From.");
    }

    @Test
    void findByOwnerWrongSizeNegative() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByOwnerFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByOwnerAll);
        Mockito
                .when(mockFindByOwnerAll.findByBookingState(1, 0, -1))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner(BookingState.ALL, 1, 0, -1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным указанием Size.");
    }

    @Test
    void findByOwnerWrongSizeZero() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockStrategyByOwnerFactory.findStrategy(Mockito.any()))
                .thenReturn(mockFindByOwnerAll);
        Mockito
                .when(mockFindByOwnerAll.findByBookingState(1, 0, 0))
                .thenThrow(new WrongEnumParameter("Указаны неправильные параметры."));
        WrongEnumParameter exception = assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner(BookingState.ALL, 1, 0, 0));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным указанием Size.");
    }

}