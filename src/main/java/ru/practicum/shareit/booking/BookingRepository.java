package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Collection<Booking> findByBooker_IdEqualsOrderByStartDesc(int userId);

    Page<Booking> findByBooker_IdEqualsOrderByStartDesc(int userId, Pageable pageable);

    Collection<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Page<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Collection<Booking> findByBooker_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Page<Booking> findByBooker_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdOrderByStartDesc(int ownerId);

    Page<Booking> findByItem_Owner_IdOrderByStartDesc(int ownerId, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime time);

    Page<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Page<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Page<Booking> findByItem_Owner_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status, Pageable pageable);

    Booking findFirstByItem_IdAndStartAfterAndStatusOrderByStart(int itemId, LocalDateTime time, BookingStatus status);

    Booking findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(int itemId, LocalDateTime time, BookingStatus status);

    boolean existsByBooker_IdAndItem_IdAndEndBeforeOrderByStartDesc(int userId, int itemId, LocalDateTime time);

}