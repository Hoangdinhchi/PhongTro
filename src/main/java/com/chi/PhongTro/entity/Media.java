package com.chi.PhongTro.entity;


import com.chi.PhongTro.util.TypeMedia;
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
@Table(name = "Media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long media_id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Posts post;
    String file_url;

    @Enumerated(EnumType.STRING) // Lưu ENUM dưới dạng chuỗi
    @Column(nullable = false, columnDefinition = "ENUM('image', 'video') DEFAULT 'image'")
    TypeMedia file_type;
    LocalDate created_at;
}
