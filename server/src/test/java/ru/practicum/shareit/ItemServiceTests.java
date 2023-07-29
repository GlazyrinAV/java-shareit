package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.exceptions.exceptions.ItemNotFound;
import ru.practicum.shareit.exceptions.exceptions.UserNotFound;
import ru.practicum.shareit.exceptions.exceptions.WrongOwner;
import ru.practicum.shareit.exceptions.exceptions.WrongParameter;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

    @Mock
    private ItemRepository mockItemRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ItemMapper mockItemMapper;

    @Mock
    private BookingRepository mcckBookingRepository;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private CommentMapper mockCommentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

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
                .when(mockItemMapper.toDto(Mockito.any(Item.class)))
                .thenReturn(toDto);
        assertEquals(toDto, itemService.save(2, fromDto),
                "Ошибка при нормальном сохранении вещи.");
        verify(mockItemRepository, times(1)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void saveWrongOwnerId(int ownerId) {
        ItemDto fromDto = ItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();
        UserNotFound exception = assertThrows(UserNotFound.class, () -> itemService.save(ownerId, fromDto));
        assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при поиске запросов с неправильным указанием пользователя.");
        verify(mockItemRepository, never()).save(Mockito.any());
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
        assertEquals(itemDtoList, itemService.findAllByUserId(1, 0, 1),
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
        assertEquals(itemDtoList, itemService.findAllByUserId(1, null, 1),
                "Ошибка прои поиске всех вещей без пагинации.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findAllByUserIdWrongUserIdWithPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> itemService.findAllByUserId(userId, 0, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным указанием пользователя c пагинацией.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findAllByUserIdWrongUserIdWithoutPage(int userId) {
        UserNotFound exception = assertThrows(UserNotFound.class, () -> itemService.findAllByUserId(userId, null, 1));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным указанием пользователя без пагинации.");
    }

    @Test
    void findAllByUserIdFromNegative() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, -1, 1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным From.");
    }

    @Test
    void findAllByUserIdSizeNegative() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, 0, -1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным Size.");
    }

    @Test
    void findAllByUserIdSizeZero() {
        User owner = new User(2, "User2", "email2@email.com");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(owner));
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findAllByUserId(2, 0, 0));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов с неправильным Size.");
    }

    @Test
    void findByIdNormal() {
        User owner = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "Description1", true, owner, null);
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
                .when(mockItemMapper.toDtoWithTime(Mockito.any(Item.class)))
                .thenReturn(toDto1);
        assertEquals(toDto1, itemService.findById(1, 2),
                "Ошибка при нормальном поиске предмета собственником по ид.");
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void findByIdWrongItemId(int itemId) {
        ItemNotFound exception = assertThrows(ItemNotFound.class, () -> itemService.findById(itemId, 1));
        assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
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
        assertEquals(toDto1, itemService.findById(1, ownerId),
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
        assertEquals(itemsDto, itemService.findByName("iTeM", 0, 2),
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
        assertEquals(itemsDto, itemService.findByName("iTeM", null, 2),
                "Ошибка при нормальном поиске предметов по названию без пагинацией.");
    }

    @Test
    void findByNameTextNullWithPage() {
        assertEquals(new ArrayList<>(), itemService.findByName(null, 0, 2),
                "Ошибка при поиске предметов по пустому названию с пагинацией.");
    }

    @Test
    void findByNameTextNullWithoutPage() {
        assertEquals(new ArrayList<>(), itemService.findByName(null, null, 2),
                "Ошибка при поиске предметов по пустому названию без пагинацией.");
    }

    @Test
    void findByNameTextBlankWithPage() {
        assertEquals(new ArrayList<>(), itemService.findByName(" ", 0, 2),
                "Ошибка при поиске предметов по пустому названию с пагинацией.");
    }

    @Test
    void findByNameTextBlankWithoutPage() {
        assertEquals(new ArrayList<>(), itemService.findByName(" ", null, 2),
                "Ошибка при поиске предметов по пустому названию без пагинацией.");
    }

    @Test
    void findByNameFromNegative() {
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findByName("Item", -1, 1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным From.");
    }

    @Test
    void findByNameSizeNegative() {
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findByName("Item", 0, -1));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным Size.");
    }

    @Test
    void findByNameSizeZero() {
        WrongParameter exception = assertThrows(WrongParameter.class, () -> itemService.findByName("Item", 0, 0));
        assertEquals("Указаны неправильные параметры.", exception.getMessage(),
                "Ошибка при поиске предметов по названию с неправильным Size.");
    }

    @Test
    void deleteByIdNormal() {
        itemService.deleteById(1);
        verify(mockItemRepository, Mockito.times(1))
                .deleteById(1);
    }

    @ParameterizedTest
    @MethodSource("wrongIdParameters")
    void deleteByIdWrongId(int itemId) {
        itemService.deleteById(itemId);
        verify(mockItemRepository, Mockito.times(1))
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
        assertEquals(toDtoAfterUpdate, itemService.updateById(2, 1, toDto1),
                "Ошибка при нормально апдайте предмета.");
        verify(mockItemRepository, times(1)).save(Mockito.any());
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
        ItemNotFound exception = assertThrows(ItemNotFound.class, () -> itemService.updateById(2, itemId, toDto1));
        assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
                "Ошибка при апдайте предмета с неправильным указанием пользователя.");
        verify(mockItemRepository, never()).save(Mockito.any());
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
        UserNotFound exception = assertThrows(UserNotFound.class, () -> itemService.updateById(ownerId, 1, toDto1));
        assertEquals("Пользователь с ID " + ownerId + " не найден.", exception.getMessage(),
                "Ошибка при поиске обновлении предмета с неправильным указанием пользователя.");
        verify(mockItemRepository, never()).save(Mockito.any());
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
        assertEquals(commentDto, itemService.saveComment(1, 1, fromDto),
                "Ошибка при нормальном добавлении комментария.");
        verify(mockCommentRepository, times(1)).save(Mockito.any());

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
        ItemNotFound exception = assertThrows(ItemNotFound.class, () -> itemService.saveComment(2, itemId, commentDto));
        assertEquals("Предмет с ID " + itemId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении комментария с неправильным указанием пользователя.");
        verify(mockCommentRepository, never()).save(Mockito.any());
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
        UserNotFound exception = assertThrows(UserNotFound.class, () -> itemService.saveComment(userId, 1, commentDto));
        assertEquals("Пользователь с ID " + userId + " не найден.", exception.getMessage(),
                "Ошибка при сохранении комментария с неправильным указанием пользователя.");
        verify(mockItemRepository, never()).save(Mockito.any());
        verify(mockCommentRepository, never()).save(Mockito.any());
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
        assertThrows(WrongOwner.class, () -> itemService.updateById(1, 1, toDto1));
        verify(mockItemRepository, never()).save(Mockito.any());
    }
}