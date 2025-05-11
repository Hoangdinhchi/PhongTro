package com.chi.PhongTro.entity;


import com.chi.PhongTro.util.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long user_id;


    String username;
    String password;
    String email;
    String phone;
    String avatar;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Buildings> buildings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Renters> renters = new ArrayList<>();

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified;

    @Enumerated(EnumType.STRING) // Lưu ENUM dưới dạng chuỗi
    @Column(nullable = false, columnDefinition = "ENUM('OWNER', 'RENTER', 'ADMIN') DEFAULT 'RENTER'")
    Role role;
    LocalDate created_at;
}
