package com.chi.PhongTro.dto.Request;

import com.chi.PhongTro.util.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "USERNAME_BLANK")
    String username;

    @NotBlank(message = "PASSWORD_BLANK")
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_BLANK")
    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "PHONE_INVALID"
    )
    String phone;

    String avatar;

    @NotNull(message = "ROLE_NULL")
    Role role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate created_at;

    String otpCode;

}
