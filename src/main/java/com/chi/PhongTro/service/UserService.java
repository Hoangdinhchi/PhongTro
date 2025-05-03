package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.*;
import com.chi.PhongTro.dto.Response.UserResponse;
import com.chi.PhongTro.entity.Renters;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.UserMapper;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UsersRepository usersRepository;
    RenterRepository renterRepository;
    UserMapper userMapper;


    OtpService otpService;

    // Bước 1: Gửi OTP khi người dùng đăng ký
    @Transactional
    public void initiateRegistration(RegisterRequest request) {
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (usersRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        // Gửi OTP
        otpService.sendOtp(request.getPhone());
    }

    // Bước 2: Xác thực OTP và hoàn tất đăng ký
    @Transactional
    public void completeRegistration(VerifyOtpRequest request) {
        // Xác thực OTP
        otpService.verifyOtp(request.getPhone(), request.getOtpCode());

        // Tạo tài khoản người dùng
        Users user = Users.builder()
                .phone(request.getPhone())
                .phoneVerified(true)
                .build();

        usersRepository.save(user);
    }

    @Transactional
    public UserResponse createRequest(UserCreationRequest request) {

        if (usersRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        if (usersRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
//TODO: verifyOtp
//        otpService.verifyOtp(request.getPhone(), request.getOtpCode());

        Users users = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setCreated_at(LocalDate.now());
        return userMapper.toUserReponse(usersRepository.save(users));
    }


    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();

        Users users = usersRepository.findByPhone(context.getAuthentication().getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserReponse(users);

    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUser() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(userMapper::toUserReponse)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userid) {
        usersRepository.deleteById(userid);
    }

    @PostAuthorize("returnObject.phone == authentication.name || hasRole('ADMIN')")
    public UserResponse updateUser(UserUpdateRequest request, String userid) {
        Users users = usersRepository.findById(userid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(users, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        users.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserReponse(usersRepository.save(users));

    }

    @Transactional
    public boolean changePassword(ChangePasswordRequest request) {
        var context = SecurityContextHolder.getContext();
        Users users = usersRepository.findByPhone(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getOldPassword(), users.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        users.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(users);
        return true;
    }


    @PostAuthorize("returnObject.phone == authentication.name || hasRole('ADMIN')")
    public UserResponse getUser(String userId) {
        return userMapper.toUserReponse(usersRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
