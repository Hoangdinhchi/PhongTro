package com.chi.PhongTro.entity;


import com.chi.PhongTro.util.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Enumerated(EnumType.STRING) // Lưu ENUM dưới dạng chuỗi
    @Column(nullable = false, columnDefinition = "ENUM('OWNER', 'RENTER', 'ADMIN') DEFAULT 'RENTER'")
    Role role;
    LocalDate created_at;
}
