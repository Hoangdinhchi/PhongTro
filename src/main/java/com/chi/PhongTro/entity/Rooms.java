package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    Long roomId;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Buildings building;

    @Column(name = "room_number", nullable = false)
    String roomNumber;

    @Column(nullable = false)
    String status;

    private Integer occupants;

    @Column(nullable = false)
    Double price;

    @ManyToMany
    @JoinTable(
            name = "room_renters",
            joinColumns = @JoinColumn( name = "room_id"),
            inverseJoinColumns = @JoinColumn( name = "renter_id")
    )
    List<Renters> renters = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}