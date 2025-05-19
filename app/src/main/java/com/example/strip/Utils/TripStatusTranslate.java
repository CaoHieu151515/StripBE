package com.example.strip.Utils;

public class TripStatusTranslate {
    public static String translateStatus(String status) {
        switch (status) {
            case "UPCOMING":
                return "Sắp đến";
            case "UPCOMMING":
                return "Sắp đến";
            case "CONFIRMING":
                return "Đang chờ xác nhận";
            case "ON_GOING":
                return "Đang đi";
            case "DONE":
                return "Hoàn thành";
            case "RESEND":
                return "Gửi lại";
            case "CANCEL":
                return "Hủy bỏ";
            case "REJECTED":
                return "Đã từ chối";
            case "ACTIVE":
                return "Đang hoạt động";
            default:
                return status; // Trả lại bản gốc nếu chưa có bản dịch
        }
    }
    public static String translateVehicle(String vehicle) {
        switch (vehicle) {
            case "BIKE":
                return "Xe 2 bánh";
            case "CAR":
                return "Xe 4 bánh";
            default:
                return vehicle; // Trả lại bản gốc nếu chưa có bản dịch
        }
    }
}
