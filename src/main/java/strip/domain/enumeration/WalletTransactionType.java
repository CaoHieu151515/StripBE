package strip.domain.enumeration;

/**
 * The WalletTransactionType enumeration.
 */
public enum WalletTransactionType {
    DEPOSIT, //User nap tiền
    WITHDRAW, // user rút tiền
    REFUND, // tiền refund
    DRIVER_CREATE_TRIP_FEE, // phí driver tạo trip
    DRIVER_DONE_TRIP_REFUND, // sau khi hoàn thành chuyến đi thì trả lại cho driver
    DRIVER_DONE_TRIP_FEE, // số tiền cuối cùng hệ thống nhận được của chuyến đi = tổng số ghế được đặt
    PASSENGER_APPROVE_FEE, // số tiền passenger cọc trước đặt chổ
    SYSTEM_GAIN_CREATE_TRIP_FEE, // tiện hệ thống nhận khi create trip
    SYSTEM_GAIN_PASSENGER_APPROVE_FEE, // tiền hệ thống giữ khi pass senger approve chuyến
    SYSTEM_GAIN_DEPOSIT,
    SYSTEM_GAIN_DONE_TRIP_FEE, // Số tiền hệ thống nhận sau khi chuyến hoàn thành
    DRIVER_BUY_PACKAGE, // tài xế mua package
    SYSTEM_GAIN_PACKAGE_FEE, // Tiền hệ thống nhận khi driver mua package
    SYSTEM_REFUND_TO_DRIVER_DONE_TRIP,
    SYSTEM_REFUND_TO_PASSENGER,
}
