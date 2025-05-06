package strip.web.rest;

import java.time.Instant;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.enumeration.RegistrationStatType;
import strip.service.DashboardService;
import strip.service.dto.dashboard.MultiListProfitStatDTO;
import strip.service.dto.dashboard.PackageSalesSimpleStatDTO;
import strip.service.dto.dashboard.RegistrationStatResponseDTO;
import strip.service.dto.dashboard.SimpleStatDTO;
import strip.service.dto.dashboard.TripCreateStatDTO;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /dashboard/registrations : Thống kê đăng ký user & driver.
     *
     * @param type     loại thống kê (WEEK, MONTH, YEAR)
     * @param fromDate ngày bắt đầu (dùng cho WEEK)
     * @param toDate   ngày kết thúc (dùng cho WEEK)
     * @param month    tháng (dùng cho MONTH)
     * @param year     năm (dùng cho MONTH, YEAR)
     * @return thống kê đăng ký
     */
    @GetMapping("/passenger-driver")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RegistrationStatResponseDTO> getRegistrations(
        @RequestParam RegistrationStatType type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        RegistrationStatResponseDTO result = dashboardService.getRegistrations(type, fromDate, toDate, month, year);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/package-sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<List<PackageSalesSimpleStatDTO>>> getPackageSalesMultiList(
        @RequestParam RegistrationStatType type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        List<List<PackageSalesSimpleStatDTO>> stats = dashboardService.getPackageSalesMultiList(type, fromDate, toDate, month, year);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/passenger-joined-trips")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<SimpleStatDTO>> getTripRegistrations(
        @RequestParam RegistrationStatType type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        List<SimpleStatDTO> stats = dashboardService.getTripRegistrations(type, fromDate, toDate, month, year);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/list-profit")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<MultiListProfitStatDTO> getMultiListProfitStats(
        @RequestParam RegistrationStatType type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        MultiListProfitStatDTO stats = dashboardService.getMultiListProfitStats(type, fromDate, toDate, month, year);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/trip-create-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<TripCreateStatDTO>> getTripCreateStats(
        @RequestParam RegistrationStatType type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        List<TripCreateStatDTO> stats = dashboardService.getTripCreateStats(type, fromDate, toDate, month, year);
        return ResponseEntity.ok(stats);
    }
}
