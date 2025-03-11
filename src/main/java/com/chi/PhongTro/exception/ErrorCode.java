package com.chi.PhongTro.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa xác định"),
    PHONE_EXISTED(1001, "Số điện thoại đã tồn tại"),
    PHONE_INVALID(1002, "Số điện thoại sai định dạng"),
    USERNAME_BLANK(1003, "Tên người dùng không được để trống"),
    EMAIL_INVALID(1004,"Email sai định dạng"),
    EMAIL_EXISTED(1005, "Email đã tồn tại"),
    PASSWORD_INVALID(1006, "Mật khẩu phải từ ít nhất 6 kí tự"),
    ROLE_NULL(1007, "Vui lòng chọn vai trò"),
    PHONE_BLANK(1008, "Số điện thoại không được để trống"),
    PASSWORD_BLANK(1009, "Mật khẩu không được để trống"),
    USER_NOT_EXISTED(1010, "Người dùng không tồn tại"),
    PHONE_NOT_EXISTED(1011, "Số điện thoại không tồn tại"),
    UNAUTHENTICATED(1012, "Xác thực không thành công")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
