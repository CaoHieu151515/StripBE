package strip.ultil;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class DashboardDateUtil {

    // ✅ Tính tuần: trả về từ Monday -> Sunday của tuần chứa ngày đó
    public static DateRange getWeekRange(Instant targetDate) {
        LocalDate localTargetDate = targetDate.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startOfWeek = localTargetDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = localTargetDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Instant fromDate = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = endOfWeek.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        return new DateRange(fromDate, toDate);
    }

    // ✅ Tính tháng: trả về từ ngày 1 đến ngày cuối tháng
    public static DateRange getMonthRange(Integer month, Integer year) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        return new DateRange(fromDate, toDate);
    }

    // ✅ Tính năm: trả về từ 01/01 đến 31/12
    public static DateRange getYearRange(Integer year) {
        LocalDate firstDay = LocalDate.of(year, 1, 1);
        LocalDate lastDay = LocalDate.of(year, 12, 31);

        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        return new DateRange(fromDate, toDate);
    }

    // ✅ Helper class để trả ra cặp fromDate & toDate
    public static class DateRange {

        private final Instant fromDate;
        private final Instant toDate;

        public DateRange(Instant fromDate, Instant toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        public Instant getFromDate() {
            return fromDate;
        }

        public Instant getToDate() {
            return toDate;
        }
    }
}
