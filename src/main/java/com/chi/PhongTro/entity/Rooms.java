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

    Integer occupants;

    @Column(nullable = false)
    Double price;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RoomRenters> roomRenters = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}