package com.chi.PhongTro.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    Long invoiceId;
    String ownerName;
    String ownerPhone;
    Long roomId;
    String roomNumber;
    Long renterId;
    String renterFullName;
    Double totalAmount;
    String status;
    LocalDate dueDate;
    List<InvoiceDetailResponse> details;
    LocalDate createdAt;
}