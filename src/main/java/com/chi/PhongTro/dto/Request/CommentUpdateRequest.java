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
public class CommentUpdateRequest {
    @NotNull(message = "COMMENT_NULL")
    @NotBlank(message = "COMMENT_BLANK")
    String content;
}