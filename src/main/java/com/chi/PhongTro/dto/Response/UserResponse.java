package com.chi.PhongTro.dto.Response;

import com.chi.PhongTro.util.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String password;
    String email;
    String phone;
    String avatar;
    Role role;
    LocalDate created_at;
}
