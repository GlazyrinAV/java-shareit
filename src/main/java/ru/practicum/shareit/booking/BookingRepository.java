package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Collection<Booking> findByBooker_IdEqualsOrderByStartDesc(int userId);

    Collection<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    Collection<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Collection<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Collection<Booking> findByBooker_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Collection<Booking> findByItem_Owner_IdOrderByStartDesc(int ownerId);

    Collection<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime time);

    Collection<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Collection<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Collection<Booking> findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Booking findFirstByItem_IdAndStartAfterAndStatusOrderByStart(int itemId, LocalDateTime time, BookingStatus status);

    Booking findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(int itemId, LocalDateTime time, BookingStatus status);

    boolean existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc(int userId, int itemId, LocalDateTime time);

}