package com.chi.PhongTro.dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostStatusUpdateRequest {
    @NotBlank(message = "INVALID_STATUS")
    @NotNull(message = "INVALID_STATUS")
   String status;
}
