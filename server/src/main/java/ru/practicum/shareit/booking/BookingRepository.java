package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Collection<Booking> findTop100ByBooker_IdEqualsOrderByStartDesc(int userId);

    Page<Booking> findByBooker_IdEquals(int userId, Pageable pageable);

    Collection<Booking> findTop100ByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByBooker_IdAndStartAfter(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findTop100ByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByBooker_IdAndEndBefore(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findTop100ByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Page<Booking> findByBooker_IdAndStartBeforeAndEndAfter(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Collection<Booking> findTop100ByBooker_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Page<Booking> findByBooker_IdAndStatusEquals(int userId, BookingStatus status, Pageable pageable);

    Collection<Booking> findByItem_Owner_IdOrderByStartDesc(int ownerId);

    Page<Booking> findByItem_Owner_Id(int ownerId, Pageable pageable);

    Collection<Booking> findTop100ByItem_Owner_IdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime time);

    Page<Booking> findByItem_Owner_IdAndStartAfter(int ownerId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findTop100ByItem_Owner_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    Page<Booking> findByItem_Owner_IdAndEndBefore(int userId, LocalDateTime time, Pageable pageable);

    Collection<Booking> findTop100ByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime time, LocalDateTime time2);

    Page<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Collection<Booking> findTop100ByItem_Owner_IdAndStatusEqualsOrderByStartDesc(int userId, BookingStatus status);

    Page<Booking> findByItem_Owner_IdAndStatusEquals(int userId, BookingStatus status, Pageable pageable);

    Booking findFirstByItem_IdAndStartAfterAndStatusOrderByStart(int itemId, LocalDateTime time, BookingStatus status);

    Booking findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(int itemId, LocalDateTime time, BookingStatus status);

    @Query("FROM Booking b WHERE b.booker.id in :userId and b.item.id in :itemId and b.end < :time")
    Collection<Booking> exists(int userId, int itemId, LocalDateTime time);

}