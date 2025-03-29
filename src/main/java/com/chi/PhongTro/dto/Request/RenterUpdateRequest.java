package com.chi.PhongTro.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenterUpdateRequest {
    @NotBlank(message = "FULL_NAME_BLANK")
    String fullName;

    @NotBlank(message = "PHONE_BLANK")
    String phone;
    String email;
}