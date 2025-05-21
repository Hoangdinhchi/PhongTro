package com.chi.PhongTro.dto.Response;

import lombok.Builder;

@Builder
public class PaymentResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
