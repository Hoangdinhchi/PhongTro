package com.chi.PhongTro.service;

import com.chi.PhongTro.entity.Otp;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.repository.OtpRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    // Tạo và gửi OTP
    @Transactional
    public void sendOtp(String phone) {
        // Tạo mã OTP ngẫu nhiên (6 chữ số)
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // Xóa OTP cũ nếu tồn tại
        otpRepository.deleteByPhone(phone);

        // Lưu OTP mới
        LocalDateTime now = LocalDateTime.now();
        Otp otp = Otp.builder()
                .phone(phone)
                .otpCode(otpCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(5)) // Hết hạn sau 5 phút
                .build();
        otpRepository.save(otp);

        // Gửi OTP qua SMS
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new PhoneNumber(phone), // Số điện thoại người nhận
                new PhoneNumber(twilioPhoneNumber), // Số Twilio
                "Your OTP code is: " + otpCode
        ).create();

        // Log kết quả (nếu cần)
        System.out.println("Sent SMS with SID: " + message.getSid());
    }

    // Xác thực OTP
    @Transactional
    public void verifyOtp(String phone, String otpCode) {
        Otp otp = otpRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        // Kiểm tra OTP có hết hạn không
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        // Kiểm tra mã OTP
        if (!otp.getOtpCode().equals(otpCode)) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        // Xóa OTP sau khi xác thực thành công
        otpRepository.deleteByPhone(phone);
    }
}