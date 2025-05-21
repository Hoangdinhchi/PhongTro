package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Response.PaymentResponse;
import com.chi.PhongTro.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
   PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ApiResponse<PaymentResponse> pay(HttpServletRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .code(1000)
                .result(paymentService.createVnPayPayment(request))
                .build();
    }


}