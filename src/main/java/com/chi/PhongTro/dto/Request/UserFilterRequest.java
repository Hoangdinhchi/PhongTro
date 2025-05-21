package com.chi.PhongTro.dto.Request;

import com.chi.PhongTro.util.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterRequest {
    String username;
    String email;
    String phone;
    Role role;
    int page = 0;
    int size = 10;
}