package com.example.strip.Utils;

public class ErrorTranslate {
    public static String translateError(String detail) {
        if (detail == null || detail.trim().isEmpty()) {
            return "Lỗi không xác định";
        }

        // Normalize input
        String normalized = detail.trim();
        switch (normalized) {
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
            default:
                return normalized; // Return original message if no match
        }
    }

}

