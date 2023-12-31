package ru.practicum.shareit.request;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REQUEST", schema = "PUBLIC")
@EqualsAndHashCode(of = {"id"})
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED")
    @CreationTimestamp
    private LocalDateTime created;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID")
    private Collection<Item> items;

}