package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByBooker_IdEquals(int userId, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartAfter(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndBefore(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartBeforeAndEndAfter(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusEquals(int userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItem_Owner_Id(int ownerId, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStartAfter(int ownerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndEndBefore(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(int userId, LocalDateTime time, LocalDateTime time2, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStatusEquals(int userId, BookingStatus status, Pageable pageable);

    Booking findFirstByItem_IdAndStartAfterAndStatusOrderByStart(int itemId, LocalDateTime time, BookingStatus status);

    Booking findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(int itemId, LocalDateTime time, BookingStatus status);

    boolean existsBookingByBooker_IdAndItem_IdAndEndBefore(int userId, int itemId, LocalDateTime time);

}