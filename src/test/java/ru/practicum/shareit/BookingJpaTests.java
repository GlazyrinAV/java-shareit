package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    void findByBooker_IdEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdEqualsOrderByStartDesc(3, page).getContent());
    }

    @Test
    void findByBooker_IdEqualsOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(List.of(booking1, booking2), bookingRepository.findByBooker_IdEqualsOrderByStartDesc(3));
    }

    @Test
    void findByBooker_IdAndStartAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(3,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }
    @Test
    void findByBooker_IdAndStartAfterOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(3,
                LocalDateTime.of(2022,10,20,9,0)));
    }

    @Test
    void findByBooker_IdAndEndBeforeOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking2), bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(3,
                LocalDateTime.of(2022,12,20,9,0), page).getContent());
    }

    @Test
    void findByBooker_IdAndEndBeforeOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(List.of(booking2), bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(3,
                LocalDateTime.of(2022,12,20,9,0)));
    }

    @Test
    void findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(3,
                LocalDateTime.of(2022,12,20,9,0),
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(List.of(booking, booking2), bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(3,
                LocalDateTime.of(2022,12,20,9,0),
                LocalDateTime.of(2022,10,20,9,0)));
    }

    @Test
    void findByBooker_IdAndStatusEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking), bookingRepository.findByBooker_IdAndStatusEqualsOrderByStartDesc(3,
                BookingStatus.WAITING, page).getContent());
    }

    @Test
    void findByBooker_IdAndStatusEqualsOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(List.of(booking, booking2), bookingRepository.findByBooker_IdAndStatusEqualsOrderByStartDesc(3,
                BookingStatus.WAITING));
    }

    @Test
    void findByItem_Owner_IdOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking), bookingRepository.findByItem_Owner_IdOrderByStartDesc(1, page).getContent());
    }

    @Test
    void findByItem_Owner_IdOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking3 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Booking booking2 = new Booking(2, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Booking booking4 = new Booking(1, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,8,18,9,0),
                LocalDateTime.of(2022,9,18,9,0));
        Booking booking5 = new Booking(5, item1, booker, BookingStatus.APPROVED,
                LocalDateTime.of(2021,8,18,9,0),
                LocalDateTime.of(2021,9,18,9,0));
        Booking booking6 = new Booking(6, item1, booker, BookingStatus.REJECTED,
                LocalDateTime.of(2020,8,18,9,0),
                LocalDateTime.of(2020,9,18,9,0));
        Assertions.assertEquals(List.of(booking1, booking2, booking3, booking4, booking5, booking6),
                bookingRepository.findByItem_Owner_IdOrderByStartDesc(1));
    }

    @Test
    void findByItem_Owner_IdAndStartAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking1), bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(1,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItem_Owner_IdAndStartAfterOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking1 = new Booking(4, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,12,18,9,0),
                LocalDateTime.of(2022,12,20,9,0));
        Assertions.assertEquals(List.of(booking1), bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(1,
                LocalDateTime.of(2022,10,20,9,0)));
    }

    @Test
    void findByItem_Owner_IdAndEndBeforeOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking4 = new Booking(1, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,8,18,9,0),
                LocalDateTime.of(2022,9,18,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking4), bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(1,
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItem_Owner_IdAndEndBeforeOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking4 = new Booking(1, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,8,18,9,0),
                LocalDateTime.of(2022,9,18,9,0));
        Booking booking5 = new Booking(5, item1, booker, BookingStatus.APPROVED,
                LocalDateTime.of(2021,8,18,9,0),
                LocalDateTime.of(2021,9,18,9,0));
        Booking booking6 = new Booking(6, item1, booker, BookingStatus.REJECTED,
                LocalDateTime.of(2020,8,18,9,0),
                LocalDateTime.of(2020,9,18,9,0));
        Assertions.assertEquals(List.of(booking4, booking5, booking6), bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(1,
                LocalDateTime.of(2022,10,20,9,0)));
    }

    @Test
    void findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking2 = new Booking(2, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking2), bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(1,
                LocalDateTime.of(2022,11,20,9,0),
                LocalDateTime.of(2022,10,20,9,0), page).getContent());
    }

    @Test
    void findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Booking booking2 = new Booking(2, item1, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Booking booking3 = new Booking(3, item2, booker, BookingStatus.WAITING,
                LocalDateTime.of(2022,10,18,9,0),
                LocalDateTime.of(2022,11,18,9,0));
        Assertions.assertEquals(List.of(booking2, booking3), bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(1,
                LocalDateTime.of(2022,11,20,9,0),
                LocalDateTime.of(2022,10,20,9,0)));
    }

    @Test
    void findByItem_Owner_IdAndStatusEqualsOrderByStartDescWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking5 = new Booking(5, item1, booker, BookingStatus.APPROVED,
                LocalDateTime.of(2021,8,18,9,0),
                LocalDateTime.of(2021,9,18,9,0));
        Pageable page = PageRequest.of(0, 1);
        Assertions.assertEquals(List.of(booking5), bookingRepository.findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(1,
                BookingStatus.APPROVED, page).getContent());
    }

    @Test
    void findByItem_Owner_IdAndStatusEqualsOrderByStartDescWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User booker = new User(3, "User3", "email3@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Booking booking5 = new Booking(5, item1, booker, BookingStatus.APPROVED,
                LocalDateTime.of(2021,8,18,9,0),
                LocalDateTime.of(2021,9,18,9,0));
        Assertions.assertEquals(List.of(booking5), bookingRepository.findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(1,
                BookingStatus.APPROVED));
    }

    @Test
    void findFirstByItem_IdAndStartAfterAndStatusOrderByStart() {
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
    void findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc() {
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
    void existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc() {
        Assertions.assertTrue(bookingRepository.existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc(2, 1,
                LocalDateTime.of(2022,10,18,9,0)));
    }

}