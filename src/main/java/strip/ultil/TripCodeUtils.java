package strip.ultil;

public class TripCodeUtils {

    private static final String PREFIX = "TRIP-";
    private static final int LENGTH = 5; // Tạo mã như TRIP-00001

    /**
     * Tạo mã chuyến đi từ ID (Long).
     * Ví dụ: 7 -> TRIP-00007
     */
    public static String encode(Long id) {
        if (id == null) return PREFIX + "UNKNOWN";
        return PREFIX + String.format("%0" + LENGTH + "d", id);
    }

    /**
     * Giải mã để lấy lại ID từ mã chuyến.
     * Ví dụ: TRIP-00007 -> 7
     */
    public static Long decode(String tripCode) {
        if (tripCode != null && tripCode.startsWith(PREFIX)) {
            try {
                String numberPart = tripCode.substring(PREFIX.length());
                return Long.parseLong(numberPart);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Kiểm tra xem chuỗi có phải là tripCode hợp lệ không
     */
    public static boolean isValid(String tripCode) {
        return decode(tripCode) != null;
    }
}
