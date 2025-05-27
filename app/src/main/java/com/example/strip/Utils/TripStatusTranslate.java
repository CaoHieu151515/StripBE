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
            case "BOOKED":
                return "Đã đặt chỗ";
            case "EMPTY":
                return "Trống không";
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
    public static String translateWalletTypeTransaction(String transaction) {
        switch (transaction) {
            case "DEPOSIT":
                return "Gửi tiền";
            case "WITHDRAW":
                return "Rút tiền";
            case "REFUND":
                return "Tiền trả lại";
            case "DRIVER_CREATE_TRIP_FEE":
                return "Phí tài xế tạo chuyến";
            case "DRIVER_DONE_TRIP_REFUND":
                return "Tiền trả lại tài xế hoàn thành chuyến";
            case "DRIVER_DONE_TRIP_FEE":
                return "Phí tài xế hoàn thành chuyến";
            case "PASSENGER_APPROVE_FEE":
                return "Phí hành khách chấp thuận";
            case "SYSTEM_GAIN_CREATE_TRIP_FEE":
                return "Phí tạo chuyến hệ thống nhận được";
            case "SYSTEM_GAIN_PASSENGER_APPROVE_FEE":
                return "Phí chấp thuận hành khách hệ thống nhận được";
            case "SYSTEM_GAIN_DONE_TRIP_FEE":
                return "Phí hoàn thành chuyến hệ thống nhận được";
            case "DRIVER_BUY_PACKAGE":
                return "Tài xế mua gói";
            case "SYSTEM_GAIN_PACKAGE_FEE":
                return "Phí mua gói hệ thống nhận được";
            default:
                return transaction; // Trả lại bản gốc nếu chưa có bản dịch
        }
    }
    public static String translateWalletStatusTransaction(String transaction) {
        switch (transaction) {
            case "PENDING":
                return "Chưa giải quyết";
            case "SUCCESS":
                return "Thành công";
            case "FAILED":
                return "Thất bại";
            default:
                return transaction; // Trả lại bản gốc nếu chưa có bản dịch
        }
    }
}
