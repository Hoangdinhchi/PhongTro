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
    UNAUTHENTICATED(1012, "Xác thực không thành công"),

    ROOM_TYPE_NOT_EXISTED(1031, "Loại phòng không tồn tại"),
    UTILITY_NOT_EXISTED(1032, "Tiện ích không tồn tại"),
    POST_TITLE_BLANK(1030, "Tiêu đề không được bỏ trống"),
    POST_TITLE_TOO_SHORT(1042, "Tiêu đề phải có ít nhất 10 ký tự"),
    POST_DESCRIPTION_BLANK(1033, "Mô tả không được bỏ trống"),
    POST_DESCRIPTION_TOO_SHORT(1043, "Mô tả phải có ít nhất 20 ký tự"),
    POST_ADDRESS_BLANK(1034, "Địa chỉ không được bỏ trống"),
    POST_CITY_BLANK(1035, "Thành phố không được bỏ trống"),
    POST_DISTRICT_BLANK(1036, "Quận/Huyện không được bỏ trống"),
    POST_PRICE_INVALID(1037, "Giá phải lớn hơn 0"),
    POST_AREA_INVALID(1038, "Diện tích phải lớn hơn 0"),
    POST_UTILITY_IDS_EMPTY(1039, "Danh sách tiện ích không được bỏ trống"),
    POST_MEDIA_EMPTY(1040, "Phải có ít nhất một hình ảnh"),
    POST_MEDIA_INVALID(1041, "Tệp phương tiện không hợp lệ"),
    FILE_PROCESSING_ERROR(1042, "Lỗi khi xử lý file"),
    POST_NOT_FOUND(1043, "Bài đăng không tồn tại"),
    INVALID_STATUS(1044, "Trạng thái không hợp lệ"),

    COMMENT_NOT_FOUND(1050, "Bình luận không tồn tại"),
    COMMENT_NULL(1051, "Bình luận không được đổ trống"),
    COMMENT_BLANK(1052, "Bình luận không được đổ trống"),

    REPORT_NOT_FOUND(1060, "Báo cáo không tồn tại"),

    OTP_NOT_FOUND(1070, "Vui lòng gửi lại OTP"),
    OTP_EXPIRED(1071, "OTP sai"),
    OTP_INVALID(1072, "OTP sai"),

    BUILDING_NOT_FOUND(1080, "Dãy phòng không tồn tại"),
    ROOM_NOT_FOUND(1081, "Phòng không tồn tại"),
    BUILDING_HAS_ROOMS(1082, "Không thể xóa dãy phòng (Dãy còn phòng)"),
    NAME_BUILDING_BLANK(1083, "Vui lòng nhập tên dãy phòng"),

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
