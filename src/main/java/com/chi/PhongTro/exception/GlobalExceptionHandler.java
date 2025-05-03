package com.chi.PhongTro.exception;

import com.chi.PhongTro.dto.Request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.net.MalformedURLException;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String errorKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(errorKey);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> handleIOException(IOException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.FILE_PROCESSING_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.FILE_PROCESSING_ERROR.getMessage() + ": " + ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ cho FileController
    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<?> handleMalformedURLException(MalformedURLException ex, WebRequest request) {
        // Kiểm tra nếu request đến từ /files/**
        String requestUri = request.getDescription(false);
        if (requestUri != null && requestUri.contains("/files/")) {
            // Trả về phản hồi không có body, chỉ có status code
            return ResponseEntity.badRequest().build();
        }

        // Xử lý mặc định cho các request khác
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(400);
        apiResponse.setMessage("Invalid file URL: " + ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


}
