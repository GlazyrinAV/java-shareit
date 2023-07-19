package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongOwner;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class ItemServiceTests {

    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemMapper mockItemMapper = Mockito.mock(ItemMapper.class);
    private final BookingRepository mcckBookingRepository = Mockito.mock(BookingRepository.class);
    private final BookingMapper mockBookingMapper = Mockito.mock(BookingMapper.class);
    private final CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
    private final CommentMapper mockCommentMapper = Mockito.mock(CommentMapper.class);
    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    private final ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository, mockItemMapper,
            mcckBookingRepository, mockBookingMapper, mockCommentRepository, mockCommentMapper, mockItemRequestRepository);

    static Stream<Integer> wrongIdParameters() {
        return Stream.of(-1, 0, 99);
    }

    @Test
    void saveNormal() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item = new Item(1, "Item1", "Description1", true, owner, null);
        ItemDto fromDto = ItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDto toDto = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(mockItemRepository.save(Mockito.any()))
                .thenReturn(item);
        Mockito
                .when(mockItemMapper.fromDto(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(item);
        Mockito
                .when(mockItemMapper.toDto(Mockito.any(Item.class)))
                .thenReturn(toDto);
        Assertions.assertEquals(toDto, itemService.save(2, fromDto),
                "Ошибка при нормальном сохранении вещи.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveWrongOwnerId(int ownerId) {
        ItemDto fromDto = ItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemService.save(ownerId, fromDto));
        Assertions.assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при поиске запросов с неправильным указанием пользователя.");
    }

    @Test
    void findAllByUserIdNormalWithPage() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        Item item2 = new Item(1, "Item2", "Description2", true, owner, null);
        ItemDtoWithTime toDto1 = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDtoWithTime toDto2 = ItemDtoWithTime.builder()
                .id(2)
                .name("Item2")
                .description("Description2")
                .available(true)
                .build();
        List<Item> itemList = List.of(item1, item2);
        Page<Item> itemPage = new PageImpl<>(itemList);
        List<ItemDtoWithTime> itemDtoList = List.of(toDto1, toDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(mockItemRepository.findAllWhereOwnerIdIn(Mockito.anyInt(), Mockito.any()))
                .thenReturn(itemPage);
        Mockito
                .when(mockItemMapper.toDtoWithTime(Mockito.anyCollection()))
                .thenReturn(itemDtoList);
        Assertions.assertEquals(itemDtoList, itemService.findAllByUserId(1, 0, 1),
                "Ошибка прои поиске всех вещей с пагинацией.");
    }

    @Test
    void findAllByUserIdNormalWithoutPage() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        Item item2 = new Item(1, "Item2", "Description2", true, owner, null);
        ItemDtoWithTime toDto1 = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDtoWithTime toDto2 = ItemDtoWithTime.builder()
                .id(2)
                .name("Item2")
                .description("Description2")
                .available(true)
                .build();
        List<Item> itemList = List.of(item1, item2);
        List<ItemDtoWithTime> itemDtoList = List.of(toDto1, toDto2);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(mockItemRepository.findAllWhereOwnerIdIn(Mockito.anyInt()))
                .thenReturn(itemList);
        Mockito
                .when(mockItemMapper.toDtoWithTime(Mockito.anyCollection()))
                .thenReturn(itemDtoList);
        Assertions.assertEquals(itemDtoList, itemService.findAllByUserId(1, null, 1),
                "Ошибка прои поиске всех вещей без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findAllByUserIdWrongUserIdWithPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemService.findAllByUserId(userId, 0, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным указанием пользователя c пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findAllByUserIdWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemService.findAllByUserId(userId, null, 1));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findAllByUserIdFromNegative() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, -1, 1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным From.");
    }

    @Test
    void findAllByUserIdSizeNegative() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, 0, -1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным Size.");
    }

    @Test
    void findAllByUserIdSizeZero() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, 0, 0));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным Size.");
    }

    @Test
    void findByIdNormal() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        Booking newBooking = new Booking(1, item1, owner, BookingStatus.WAITING, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4));
        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .id(1)
                .bookerId(1)
                .build();
        ItemDtoWithTime toDto1 = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .nextBooking(bookingDtoShort)
                .comments(new ArrayList<>())
                .build();
        Mockito
                .when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item1));
        Mockito
                .when(mcckBookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStart(1, LocalDateTime.now(), BookingStatus.APPROVED))
                .thenReturn(newBooking);
        Mockito
                .when(mockBookingMapper.toDtoShort(Mockito.any()))
                .thenReturn(bookingDtoShort);
        Mockito
                .when(mockItemMapper.toDtoWithTime(Mockito.any(Item.class)))
                .thenReturn(toDto1);
        Assertions.assertEquals(toDto1, itemService.findById(1, 2),
                "Ошибка при нормальном поиске предмета собственником по ид.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongItemId(int itemId) {
        ItemNotFound exception = Assertions.assertThrows(ItemNotFound.class, () -> itemService.findById(itemId, 1));
        Assertions.assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным указанием пользователя.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongOwnerId(int ownerId) {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        ItemDtoWithTime toDto1 = ItemDtoWithTime.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item1));
        Mockito
                .when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(mockItemMapper.toDtoWithTime(Mockito.any(Item.class)))
                .thenReturn(toDto1);
        Assertions.assertEquals(toDto1, itemService.findById(1, ownerId),
                "Ошибка при нормальном поиске предмета не собственником по ид.");
    }

    @Test
    void findByNameNormalWithPage() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        Item item2 = new Item(1, "Item2", "Description2", true, owner, null);
        List<Item> items = List.of(item1, item2);
        ItemDto toDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDto toDto2 = ItemDto.builder()
                .id(2)
                .name("Item2")
                .description("Description2")
                .available(true)
                .build();
        List<ItemDto> itemsDto = List.of(toDto1, toDto2);
        Page<Item> itemsPage = new PageImpl<>(items);
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString(), Mockito.any()))
                .thenReturn(itemsPage);
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(itemsDto);
        Assertions.assertEquals(itemsDto, itemService.findByName("iTeM", 0, 2),
                "Ошибка при нормальном поиске предметов по названию с пагинацией.");
    }

    @Test
    void findByNameNormalWithoutPage() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        Item item2 = new Item(1, "Item2", "Description2", true, owner, null);
        List<Item> items = List.of(item1, item2);
        ItemDto toDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        ItemDto toDto2 = ItemDto.builder()
                .id(2)
                .name("Item2")
                .description("Description2")
                .available(true)
                .build();
        List<ItemDto> itemsDto = List.of(toDto1, toDto2);
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString()))
                .thenReturn(items);
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(itemsDto);
        Assertions.assertEquals(itemsDto, itemService.findByName("iTeM", null, 2),
                "Ошибка при нормальном поиске предметов по названию без пагинацией.");
    }

    @Test
    void findByNameTextNullWithPage() {
        Page<Item> itemsPage = new PageImpl<>(new ArrayList<>());
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString(), Mockito.any()))
                .thenReturn(itemsPage);
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(new ArrayList<>());
        Assertions.assertEquals(new ArrayList<>(), itemService.findByName(null, 0, 2),
                "Ошибка при поиске предметов по пустому названию с пагинацией.");
    }

    @Test
    void findByNameTextNullWithoutPage() {
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString()))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(new ArrayList<>());
        Assertions.assertEquals(new ArrayList<>(), itemService.findByName(null, null, 2),
                "Ошибка при поиске предметов по пустому названию без пагинацией.");
    }

    @Test
    void findByNameTextBlankWithPage() {
        Page<Item> itemsPage = new PageImpl<>(new ArrayList<>());
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString(), Mockito.any()))
                .thenReturn(itemsPage);
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(new ArrayList<>());
        Assertions.assertEquals(new ArrayList<>(), itemService.findByName(" ", 0, 2),
                "Ошибка при поиске предметов по пустому названию с пагинацией.");
    }

    @Test
    void findByNameTextBlankWithoutPage() {
        Mockito
                .when(mockItemRepository.findByName(Mockito.anyString()))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(mockItemMapper.toDto(Mockito.anyCollection()))
                .thenReturn(new ArrayList<>());
        Assertions.assertEquals(new ArrayList<>(), itemService.findByName(" ", null, 2),
                "Ошибка при поиске предметов по пустому названию без пагинацией.");
    }

    @Test
    void findByNameFromNegative() {
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findByName("Item", -1, 1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным From.");
    }

    @Test
    void findByNameSizeNegative() {
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findByName("Item", 0, -1));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным Size.");
    }

    @Test
    void findByNameSizeZero() {
        WrongParameter exception = Assertions.assertThrows(WrongParameter.class, () -> itemService.findByName("Item", 0, 0));
        Assertions.assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным Size.");
    }

    @Test
    void deleteByIdNormal() {
        itemService.deleteById(1);
        Mockito.verify(mockItemRepository, Mockito.times(1))
                .deleteById(1);
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void deleteByIdWrongId(int itemId) {
        itemService.deleteById(itemId);
        Mockito.verify(mockItemRepository, Mockito.times(1))
                .deleteById(itemId);
    }

    @Test
    void updateByIdNormal() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        ItemDto toDto1 = ItemDto.builder()
                .description("NewDescription")
                .build();
        ItemDto toDtoAfterUpdate = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("NewDescription")
                .available(true)
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item1));
        Mockito
                .when(mockItemRepository.save(Mockito.any()))
                .thenReturn(item1);
        Mockito
                .when(mockItemMapper.toDto(Mockito.any(Item.class)))
                .thenReturn(toDtoAfterUpdate);
        Assertions.assertEquals(toDtoAfterUpdate, itemService.updateById(2, 1, toDto1),
                "Ошибка при нормально апдайте предмета.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateByIdWrongItemId(int itemId) {
        User owner = new User(2, "User2", "email2@email.com");
        ItemDto toDto1 = ItemDto.builder()
                .description("NewDescription")
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        ItemNotFound exception = Assertions.assertThrows(ItemNotFound.class, () -> itemService.updateById(2, itemId, toDto1));
        Assertions.assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
                "Ошибка при апдайте предмета с неправильным указанием пользователя.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void updateByIdWrongOwnerId(int ownerId) {
        ItemDto toDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemService.updateById(ownerId, 1, toDto1));
        Assertions.assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при поиске обновлении предмета с неправильным указанием пользователя.");
    }

    @Test
    void isExistsNormal() {
        Mockito
                .when(mockItemRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);
        Assertions.assertTrue(itemService.isExists(1),
                "Ошибка при нормальной проверке наличия предмета.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void isExistsWrongItemId(int itemId) {
        Mockito
                .when(mockItemRepository.existsById(Mockito.anyInt()))
                .thenReturn(false);
        Assertions.assertFalse(itemService.isExists(itemId),
                "Ошибка при нормальной проверке наличия предмета.");
    }

    @Test
    void saveCommentNormal() {
        User owner = new User(2, "User2", "email2@email.com");
        User user = new User(1, "User1", "email1@email.com");
        Item item = new Item(1, "Item1", "Description1", true, owner, null);
        CommentDto fromDto = CommentDto.builder()
                .text("text")
                .build();
        Comment comment = new Comment(1, item, user, "text", LocalDateTime.now());
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .created(comment.getCreated())
                .authorName("User1")
                .build();
        Mockito
                .when(mcckBookingRepository.existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(true);
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(mockCommentRepository.save(Mockito.any()))
                .thenReturn(comment);
        Mockito
                .when(mockCommentMapper.fromDto(fromDto, user, item))
                .thenReturn(comment);
        Mockito
                .when(mockCommentMapper.toDto(Mockito.any(Comment.class)))
                .thenReturn(commentDto);
        Assertions.assertEquals(commentDto, itemService.saveComment(1, 1, fromDto),
                "Ошибка при нормальном добавлении комментария.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveCommentWrongItemId(int itemId) {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .created(LocalDateTime.now())
                .authorName("name")
                .build();
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        ItemNotFound exception = Assertions.assertThrows(ItemNotFound.class, () -> itemService.saveComment(2, itemId, commentDto));
        Assertions.assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении комментария с неправильным указанием пользователя.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveCommentWrongOwnerId(int userId) {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .created(LocalDateTime.now())
                .authorName("name")
                .build();
        UserNotFound exception = Assertions.assertThrows(UserNotFound.class, () -> itemService.saveComment(userId, 1, commentDto));
        Assertions.assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении комментария с неправильным указанием пользователя.");
    }

    @Test
    void updateByIdWrongOwner() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
        ItemDto toDto1 = ItemDto.builder()
                .description("NewDescription")
                .build();
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item1));
        WrongOwner exception = Assertions.assertThrows(WrongOwner.class, () -> itemService.updateById(1, 1, toDto1));
    }
}