package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
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

class BookingServiceTests {

    static Stream<Integer> wrongIdParameters() {
        return Stream.of(-1, 0, 99);
    }

    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);

    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);

    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);

    private final BookingMapper mockBookingMapper = Mockito.mock(BookingMapper.class);

    private final StrategyByOwnerFactory mockStrategyByOwnerFactory = Mockito.mock(StrategyByOwnerFactory.class);

    private final StrategyByStateFactory mockStrategyByStateFactory = Mockito.mock(StrategyByStateFactory.class);

    private final FindByStateAll mockFindByStateAll = Mockito.mock(FindByStateAll.class);

    private final FindByOwnerAll mockFindByOwnerAll = Mockito.mock(FindByOwnerAll.class);

    private final BookingService bookingService = new BookingServiceImpl(mockUserRepository, mockItemRepository,
            mockBookingRepository, mockBookingMapper, mockStrategyByOwnerFactory, mockStrategyByStateFactory);

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
        Assertions.assertEquals(bookingDto, bookingService.save(newBookingDto, 1),
                "Ошибка при нормальном сохранении нового бронирования.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveWrongUserId(int userId) {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(12))
                .build();
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.save(newBookingDto, userId));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении нового бронирования с неправильным указанием пользователя.");
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
        Assertions.assertEquals(bookingDto, bookingService.update(1, 2, true),
                "Ошибка при нормальном подтверждении бронирования.");
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
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.update(1, ownerId, true));
        Assertions.assertEquals("Данный запрос может сделать только собственник предмета.", exception.getMessage(),
                "Ошибка при подтверждении бронирования с неправильным указанием собственника.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateWrongBookingId(int bookingId) {
        BookingNotFound exception = Assertions.assertThrows(BookingNotFound.class, () -> bookingService.update(bookingId, 2, true));
        Assertions.assertEquals("Бронь с ID " + bookingId + " не найдена.", exception.getMessage(),
                "Ошибка при подтверждении бронирования с неправильным указанием бронирования.");
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
        Assertions.assertEquals(bookingDto, bookingService.findById(1, 2),
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
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.findById(1, ownerId));
        Assertions.assertEquals("Данный запрос может сделать только собственник предмета или бронирующий.", exception.getMessage(),
                "Ошибка при поиске бронирования с неправильным указанием собственника.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongBookingId(int bookingId) {
        BookingNotFound exception = Assertions.assertThrows(BookingNotFound.class, () -> bookingService.findById(bookingId, 2));
        Assertions.assertEquals("Бронь с ID " + bookingId + " не найдена.", exception.getMessage(),
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
        Assertions.assertEquals(List.of(bookingDto2), bookingService.findByState("ALL", 1, 0, 1),
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
        Assertions.assertEquals(requsts, bookingService.findByState("ALL", 1, null, 1),
                "Ошибка при поске брони по состоянию без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByStateWrongUserIdWithPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.findByState("ALL", userId, 0, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по состоянию с неправильным указанием пользователя с пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByStateWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.findByState("ALL", userId, null, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по состоянию с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findByStateWrongStateWithPage() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByState("UNKNOWN", 1, 0, 1));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage(),
                "Ошибка при поиске брони по состоянию с неправильным состоянием с пагинацией.");
    }

    @Test
    void findByStateWrongStateWithoutPage() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByState("UNKNOWN", 1, null, 1));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage(),
                "Ошибка при поиске брони по состоянию с неправильным состоянием без пагинации.");
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByState("ALL", 1, -1, 1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByState("ALL", 1, 0, -1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByState("ALL", 1, 0, 0));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
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
        Assertions.assertEquals(List.of(bookingDto2), bookingService.findByOwner("ALL", 1, 0, 1),
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
        Assertions.assertEquals(requsts, bookingService.findByOwner("ALL", 1, null, 1),
                "Ошибка при поске брони по собственнику без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByOwnerWrongUserIdWithPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.findByOwner("ALL", userId, 0, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по собственнику с неправильным указанием пользователя с пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByOwnerWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> bookingService.findByOwner("ALL", userId, null, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поске брони по собственнику с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findByOwnerWrongStateWithPage() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner("UNKNOWN", 1, 0, 1));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным состоянием с пагинацией.");
    }

    @Test
    void findByOwnerWrongStateWithoutPage() {
        User user = new User(1, "User1", "email@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner("UNKNOWN", 1, null, 1));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным состоянием без пагинации.");
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner("ALL", 1, -1, 1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner("ALL", 1, 0, -1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
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
        WrongEnumParameter exception = Assertions.assertThrows(WrongEnumParameter.class, () -> bookingService.findByOwner("ALL", 1, 0, 0));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске брони по собственнику с неправильным указанием Size.");
    }

}