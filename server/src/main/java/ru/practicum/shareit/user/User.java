package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "USERS", schema = "PUBLIC")
@EqualsAndHashCode(of = {"id"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

}