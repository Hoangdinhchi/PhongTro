package com.chi.PhongTro.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Utilities")
public class Utilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long utility_id;
    String name;
    String description;
    LocalDate created_at;

}
