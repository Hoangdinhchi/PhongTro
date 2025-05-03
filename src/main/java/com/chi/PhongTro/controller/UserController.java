package com.chi.PhongTro.controller;

import com.chi.PhongTro.dto.Request.*;
import com.chi.PhongTro.dto.Response.UserResponse;
import com.chi.PhongTro.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/otp")
    ApiResponse<String> initiateRegistration(@RequestBody @Valid RegisterRequest request){
        userService.initiateRegistration(request);
        return ApiResponse.<String>builder()
                .result("Đã gửi OTP vào số: " + request.getPhone())
                .build();
    }

    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createRequest(request));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(request,userId));

        return apiResponse;
    }

    @PatchMapping("/change-password")
    ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request){

        boolean result = true;
        if(!userService.changePassword(request)){
            result = false;
        };
        return ApiResponse.<String>builder()
                .code(1000)
                .result(result ? "Đổi mật khẩu thành công" : "Lỗi trong quá trình đổi mật khẩu")
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){

        var authentication = SecurityContextHolder.getContext().getAuthentication();


        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    @GetMapping("/my_info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.getMyInfo())
                .build();
    }


    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId)
    {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.deleteUser(userId);
        apiResponse.setMessage("Đã xóa người dùng");
        return apiResponse;
    }
}
