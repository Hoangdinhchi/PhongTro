package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenterResponse {
    Long renterId;
    Long userId;
    String avatar;
    String fullName;
    String phone;
    String email;
    Boolean has_account;
    LocalDate createdAt;
}