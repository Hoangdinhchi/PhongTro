package com.chi.PhongTro.dto.Response;

import com.chi.PhongTro.entity.TypeReports;
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
    Long toUserId;
    String toUserPhone;
    Long reportedId;
    TypeReports typeReport;
    String reason;
    String content;
    String status;
    LocalDate createdAt;
}