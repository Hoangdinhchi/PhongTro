package com.chi.PhongTro.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Water {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "1", nullable = false)
    double level_1;
    @Column(name = "2", nullable = false)
    double level_2;
    @Column(name = "3", nullable = false)
    double level_3;
    @Column(name = "4", nullable = false)
    double level_4;
    LocalDate create_at;

}
