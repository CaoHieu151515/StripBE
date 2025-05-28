package com.example.strip.Utils;

public class ErrorTranslate {
    public static String translateError(String detail) {
        if (detail == null || detail.trim().isEmpty()) {
            return "Lỗi không xác định";
        }

        // Normalize input
        String normalized = detail.trim();
        switch (normalized) {
            case "error.usedToDriver":
                return "Xác nhận tài xế đã được gửi trước đó";
            case "error.already-exists":
                return "Phản hồi này đã được gửi trước đó";
            case "error.alreadyJoined":
                return "Bạn đã tham gia chuyến đi này rồi";
            case "error.alreadySubmitted":
                return "Bạn đã gửi xác nhận tài xế trước đó rồi";
            case "Bad credentials":
                return "Sai tài khoản hoặc mật khẩu";
            case "User is disabled":
                return "Tài khoản đã bị vô hiệu hóa";
            case "User account is locked":
                return "Tài khoản đã bị khóa";
            case "Unexpected runtime exception":
                return "Lỗi xảy ra bất ngờ trong quá trình chạy chương trình";
            case "Failure during data access":
                return "Lỗi trong quá trình truy cập dữ liệu";
            case "Failed to read request":
                return "Thất bại trong việc đọc yêu cầu";
            case "Unauthorized":
                return "Không được phép";
            case "Internal Server Error":
                return "Lỗi máy chủ nội bộ";
            case "400 BAD_REQUEST":
                return "Đã xác nhận rồi";
            default:
                return normalized; // Return original message if no match
        }
    }

}

