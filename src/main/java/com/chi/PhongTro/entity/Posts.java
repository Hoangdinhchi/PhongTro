package com.chi.PhongTro.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long post_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    RoomTypes type;

    String title;
    String description;
    String address;
    String city;
    String district;
    Double latitude;
    Double longitude;

    Double price;
    Double area;

    @ManyToMany
    @JoinTable(
            name = "post_utilities",
            joinColumns = @JoinColumn( name = "post_id"),
            inverseJoinColumns = @JoinColumn( name = "utility_id")
    )
    Set<Utilities> utilities;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Media> media = new ArrayList<>();


    String status;
    @Column(name = "created_at")
    LocalDate createdAt;


    int view_count;
    int save_count;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("created_at DESC")
    private List<Comments> comments = new ArrayList<>();
}
