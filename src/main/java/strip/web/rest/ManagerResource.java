package strip.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.PackageDriver;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;
import strip.domain.enumeration.PackageDriverStatus;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.TripStatus;
import strip.domain.enumeration.WalletIncomeType;
import strip.domain.enumeration.WalletTransactionType;
import strip.service.UsermanageService;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.CustomPageDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverPenaltyRequestDTO;
import strip.service.dto.DriverPointHistoryListDTO;
import strip.service.dto.DriverPointHistoryRefundDTO;
import strip.service.dto.FeedbackCusDTO;
import strip.service.dto.PackageDriverDTO;
import strip.service.dto.RejectTripDTO;
import strip.service.dto.SystemWalletBalanceDTO;
import strip.service.dto.TripDetailDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.WalletTransactionAdminDTO;
import strip.service.dto.WithdrawalRequestManageDTO;
import strip.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/manager")
public class ManagerResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "username", "firstName", "lastName", "email", "active", "gender", "phoneNumber")
    );
    private static final List<String> ALLOWED_ORDERED_PROPERTIES_TRIP = List.of(
        "startLocation",
        "endLocation",
        "startDate",
        "endDate",
        "tripStatus"
    );
    private static final List<String> ALLOWED_ORDERED_PROPERTIES_FEEDBACK = List.of("feedbackStatus", "feedbackType", "feedbackRating");

    private static final List<String> ALLOWED_ORDERED_PROPERTIES_VEHICLE = List.of(
        "firstName",
        "lastName",
        "email",
        "phone",
        "rating",
        "driverId"
    );
    // private static final List<String> ALLOWED_ORDERED_PROPERTIES_REPORT =
    // List.of("reportID", "date", "reportType", "reportStatus");

    private static final List<String> ALLOWED_ORDERED_PROPERTIES_DRIVER_CONFIRMING = List.of("firstName", "lastName", "email", "phone");
    private final UsermanageService usermanageService;

    private final Logger log = LoggerFactory.getLogger(ManagerResource.class);

    public ManagerResource(UsermanageService usermanageService) {
        this.usermanageService = usermanageService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/GetAllUsers")
    public ResponseEntity<CustomPageDTO<UsermanageDTO>> getAllUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Boolean active
    ) {
        log.debug("REST request to get all Users with filters");

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        Page<UsermanageDTO> page = usermanageService.getAllUsers(pageable, firstName, lastName, email, active);
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    // @GetMapping("/details/{id}")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    // public ResponseEntity<UsermanageDetailsDTO>
    // getManagerDetailsById(@PathVariable Long id) {
    // log.debug("REST request to get detailed Manager information: {}", id);
    // return
    // usermanageService.getUserDetailsById(id).map(ResponseEntity::ok).orElseGet(()
    // -> ResponseEntity.notFound().build());
    // }

    @GetMapping("/details/{userId}")
    public ResponseEntity<DriverInfoDTO> getDriverDetailsByUsername(@PathVariable UUID userId) {
        Optional<DriverInfoDTO> driverInfo = usermanageService.getDriverDetailsByUserId(userId);
        return driverInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/vehicles/pending-approval")
    public ResponseEntity<CustomPageDTO<ConfirmingVehicleDriverDTO>> getAllPendingApprovalVehicles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) UUID driverId
    ) {
        if (!onlyContainsAllowedPropertiesVehicle(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        Page<ConfirmingVehicleDriverDTO> page = usermanageService.getAllConfirmingVehicles(
            pageable,
            firstName,
            lastName,
            email,
            phone,
            driverId
        );
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    private boolean onlyContainsAllowedPropertiesVehicle(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES_VEHICLE::contains);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/vehicles/confirming/{vehicleId}/approve")
    public ResponseEntity<String> approveVehicle(@PathVariable UUID vehicleId) {
        boolean success = usermanageService.approveVehicle(vehicleId);
        if (success) {
            return ResponseEntity.ok("Vehicle approved successfully.");
        }
        return ResponseEntity.badRequest().body("Vehicle not found.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/vehicles/confirming/{vehicleId}/reject")
    public ResponseEntity<String> rejectVehicle(@PathVariable UUID vehicleId) {
        boolean success = usermanageService.rejectVehicle(vehicleId);
        if (success) {
            return ResponseEntity.ok("Vehicle rejected successfully.");
        }
        return ResponseEntity.badRequest().body("Vehicle not found.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/drivers/confirming/getAll")
    public ResponseEntity<CustomPageDTO<ConfirmingVehicleDriverDTO>> getConfirmingDrivers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) UUID driverId
    ) {
        if (!onlyContainsAllowedPropertiesDriverConfirming(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        Page<ConfirmingVehicleDriverDTO> page = usermanageService.getConfirmingDrivers(
            pageable,
            firstName,
            lastName,
            email,
            phone,
            driverId
        );

        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    private boolean onlyContainsAllowedPropertiesDriverConfirming(Pageable pageable) {
        return pageable
            .getSort()
            .stream()
            .map(order -> order.getProperty())
            .allMatch(ALLOWED_ORDERED_PROPERTIES_DRIVER_CONFIRMING::contains);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PatchMapping("/drivers/confirming/{driverId}/approve")
    public ResponseEntity<Void> approveDriver(@PathVariable UUID driverId) {
        log.debug("REST request to approve driver {}", driverId);
        usermanageService.approveDriver(driverId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PatchMapping("/drivers/confirming/{driverId}/reject")
    public ResponseEntity<Void> rejectDriver(@PathVariable UUID driverId) {
        log.debug("REST request to reject driver {}", driverId);
        usermanageService.rejectDriver(driverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/packages/getAllPackage")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CustomPageDTO<PackageDriverDTO>> getAllPackages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Double price,
        @RequestParam(required = false) Integer time,
        @RequestParam(required = false) PackageDriverStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant expireDate
    ) {
        Page<PackageDriverDTO> page = usermanageService.getAllPackagesWithFilter(
            pageable,
            name,
            price,
            time,
            status,
            createdDate,
            expireDate
        );
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/packages/createPackage")
    public ResponseEntity<PackageDriverDTO> createPackage(@RequestBody PackageDriverDTO packageDriverDTO) throws URISyntaxException {
        PackageDriverDTO result = usermanageService.createPackage(packageDriverDTO);
        return ResponseEntity.created(new URI("/api/manager/packages/" + result.getPackageID())).body(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PatchMapping("/package/{packageId}/toggle-status")
    public ResponseEntity<PackageDriver> togglePackageStatus(@PathVariable UUID packageId) {
        Optional<PackageDriver> updated = usermanageService.togglePackageStatus(packageId);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/wallet/withdraw/pending")
    public ResponseEntity<List<WithdrawalRequestManageDTO>> getPendingRequests() {
        return ResponseEntity.ok(usermanageService.getPendingWithdrawalRequests());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/wallet/withdraw/approve/{depositId}")
    public ResponseEntity<Void> approveWithdrawal(@PathVariable UUID depositId) {
        usermanageService.approveWithdrawal(depositId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/wallet/reject/{depositId}")
    public ResponseEntity<Void> rejectWithdrawal(@PathVariable UUID depositId) {
        usermanageService.rejectWithdrawal(depositId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trips/getall")
    public ResponseEntity<CustomPageDTO<TripListDTO>> getAllTrips(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) String startLocation,
        @RequestParam(required = false) String endLocation,
        @RequestParam(required = false) TripStatus status,
        @RequestParam(required = false) UUID driverId,
        @RequestParam(required = false) String tripHandleId
    ) {
        if (!onlyContainsAllowedPropertiesTrip(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        Page<TripListDTO> page = usermanageService.getAllTrips(pageable, startLocation, endLocation, status, driverId, tripHandleId);
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDetailDTO> getTripById(@PathVariable UUID id) {
        Optional<TripDetailDTO> tripDTO = usermanageService.getTripById(id);
        return tripDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private boolean onlyContainsAllowedPropertiesTrip(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES_TRIP::contains);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/trips/{tripId}/reject")
    public ResponseEntity<Void> rejectTrip(@PathVariable UUID tripId, @RequestBody RejectTripDTO dto) {
        if (dto.getReason() == null || dto.getReason().isBlank()) {
            throw new BadRequestAlertException("Lý do từ chối là bắt buộc", "trip", "reason-required");
        }

        usermanageService.rejectTrip(tripId, dto.getReason());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/trips/{tripId}/approve")
    public ResponseEntity<Void> approveTrip(@PathVariable UUID tripId) {
        usermanageService.approveTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/feedbacks")
    public ResponseEntity<CustomPageDTO<FeedbackCusDTO>> getAllFeedbacks(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) FeedbackStatus status,
        @RequestParam(required = false) FeedbackType type,
        @RequestParam(required = false) String tripId
    ) {
        if (!onlyContainsAllowedPropertiesFeedback(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        Page<FeedbackCusDTO> page = usermanageService.getAllFeedbacks(pageable, status, type, tripId);
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    private boolean onlyContainsAllowedPropertiesFeedback(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES_FEEDBACK::contains);
    }

    // @GetMapping("/reports/gettall")
    // public ResponseEntity<List<ReportCusDTO>> getAllReports(
    // @ParameterObject Pageable pageable,
    // @RequestParam(required = false) ReportStatus status,
    // @RequestParam(required = false) ReportType type
    // ) {
    // log.debug("REST request to get all reports");

    // if (!onlyContainsAllowedPropertiesReport(pageable)) {
    // return ResponseEntity.badRequest().build();
    // }

    // Page<ReportCusDTO> page = usermanageService.getAllReports(pageable, status,
    // type);
    // HttpHeaders headers =
    // PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
    // page);

    // return ResponseEntity.ok().headers(headers).body(page.getContent());
    // }

    // private boolean onlyContainsAllowedPropertiesReport(Pageable pageable) {
    // return
    // pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES_REPORT::contains);
    // }

    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    // @PatchMapping("/feedback/{id}/handle")
    // public ResponseEntity<Void> handleReport(@PathVariable UUID id, @RequestBody
    // HandleReportDTO dto) {
    // log.debug("REST request to handle report: {}", id);

    // usermanageService.handleReport(id, dto.getReason(), dto.getPoint());

    // return ResponseEntity.noContent().build(); // HTTP 204
    // }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/wallet-transactions")
    public ResponseEntity<CustomPageDTO<WalletTransactionAdminDTO>> getAllTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) WalletTransactionType walletType,
        @RequestParam(required = false) TransactionStatus walletStatus,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate
    ) {
        Page<WalletTransactionAdminDTO> page = usermanageService.getSystemTransactions(
            pageable,
            walletType,
            walletStatus,
            fromDate,
            toDate
        );
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    @GetMapping("/wallet/Transactionhistory")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CustomPageDTO<WalletTransactionAdminDTO>> getSystemIncomeTransactions(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) WalletIncomeType walletType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate
    ) {
        Page<WalletTransactionAdminDTO> page = usermanageService.getSystemIncomeTransactions(
            pageable,
            walletType != null ? WalletTransactionType.valueOf(walletType.name()) : null,
            fromDate,
            toDate
        );
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    @GetMapping("/wallet/system-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<SystemWalletBalanceDTO> getSystemWalletBalance() {
        double totalBalance = usermanageService.getSystemWalletBalance();
        return ResponseEntity.ok(new SystemWalletBalanceDTO(totalBalance));
    }

    @PostMapping("/driver-point/{id}/penalize")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> penalizeDriver(@PathVariable("id") UUID feedbackId, @RequestBody DriverPenaltyRequestDTO request) {
        usermanageService.penalizeDriverByFeedback(feedbackId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/driver-point/by-driver")
    public ResponseEntity<CustomPageDTO<DriverPointHistoryListDTO>> getDriverPointHistoryByUserDetail(
        @RequestParam UUID driveruuId,
        @ParameterObject Pageable pageable
    ) {
        Page<DriverPointHistoryListDTO> page = usermanageService.getPointHistoryListByUserDetail(driveruuId, pageable);
        return ResponseEntity.ok(new CustomPageDTO<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/driver-point/{pointId}/refund")
    public ResponseEntity<DriverPointHistoryRefundDTO> refundDriverPoint(@PathVariable UUID pointId) {
        DriverPointHistoryRefundDTO dto = usermanageService.refundByHistory(pointId);
        return ResponseEntity.ok(dto);
    }
}
