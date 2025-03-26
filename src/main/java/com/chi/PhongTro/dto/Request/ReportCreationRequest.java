package com.chi.PhongTro.dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportCreationRequest {
    String reportedUserId;
    String postId;
    String reason;
}