package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookingJpaTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findByBookerIdEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdEquals(3, page).getContent());
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStartAfter(3,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking2), bookingRepository.findByBooker_IdAndEndBefore(3,
                LocalDateTime.of(2022,12,20,9,0), page).getContent());
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(3,
                LocalDateTime.of(2022,12,20,9,0),
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByBookerIdAndStatusEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStatusEquals(3,
                BookingStatus.WAITING, page).getContent());
    }

    @Test
    void findByItemOwnerIdOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking), bookingRepository.findByItem_Owner_Id(1, page).getContent());
    }

    @Test
    void findByItemOwnerIdAndStartAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking1), bookingRepository.findByItem_Owner_IdAndStartAfter(1,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItemOwnerIdAndEndBeforeOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking4 = new Booking(1, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,8,18,9,0),
                LocalDateTime.of(2022,9,18,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking4), bookingRepository.findByItem_Owner_IdAndEndBefore(1,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking2 = new Booking(2, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking2), bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(1,
                LocalDateTime.of(2022,11,20,9,0),
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItemOwnerIdAndStatusEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking5 = new Booking(5, item1, booker, BookingStatus.APPROVED,
                LocalDateTime.of(2021,8,18,9,0),
                LocalDateTime.of(2021,9,18,9,0));
        Pageable page = PageRequest.of(0, 1, Sort.by("start").descending());
        Assertions.assertEquals(List.of(booking5), bookingRepository.findByItem_Owner_IdAndStatusEquals(1,
                BookingStatus.APPROVED, page).getContent());
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusOrderByStart() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Assertions.assertEquals(booking1, bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStart(1,
                LocalDateTime.of(2022,11,20,9,0), BookingStatus.WAITING));
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking2 = new Booking(2, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(booking2, bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(1,
                LocalDateTime.of(2022,11,20,9,0), BookingStatus.WAITING));
    }

    @Test
    void existsByBookerIdAndItemIdAndEndBeforeOrderByStartDesc() {
        Assertions.assertTrue(bookingRepository.existsBookingByBooker_IdAndItem_IdAndEndBefore(2, 1,
                LocalDateTime.of(2022,10,18,9,0)));
    }

}