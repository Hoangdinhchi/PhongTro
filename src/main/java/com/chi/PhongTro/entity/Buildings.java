package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buildings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Buildings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    Long buildingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(nullable = false)
    String name;

    String description;

    @Column(name = "created_at", nullable = false)
    LocalDate createdAt;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Rooms> rooms = new ArrayList<>();
}