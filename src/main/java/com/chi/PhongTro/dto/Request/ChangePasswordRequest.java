package com.chi.PhongTro.dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotNull(message = "OLD_PASSWORD_NULL")
    String oldPassword;

    @NotBlank(message = "NEW_PASSWORD_BLANK")
    @Size(min = 6, message = "PASSWORD_INVALID")
    String newPassword;
}
