package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "BOOKING", schema = "PUBLIC")
@ToString(exclude = {"item", "booker"})
@EqualsAndHashCode(exclude = {"item", "booker", "status", "start", "end"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker")
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "finish")
    private LocalDateTime end;

}