package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    Long reportId;
    Long reporterId;
    String reporterPhone;
    Long reportedUserId;
    String reportedUserPhone;
    Long postId;
    String reason;
    String status;
    LocalDate createdAt;
}