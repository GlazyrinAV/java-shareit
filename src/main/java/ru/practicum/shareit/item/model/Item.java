//package ru.practicum.shareit.item.model;
//
//import lombok.*;
//import ru.practicum.shareit.user.User;
//
//import javax.persistence.*;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "Item", schema = "public")
//public class Item {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "available")
//    private Boolean available;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", nullable = false)
//    private User owner;
//
//}