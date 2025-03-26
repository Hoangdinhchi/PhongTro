package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    String status; // Ví dụ: "available", "occupied", "maintenance"

    private Integer occupants; // Số người hiện tại trong phòng

    @Column(nullable = false)
    Double price;

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;
}