package strip.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.domain.PackageDriver;
import strip.service.UsermanageService;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.PackageDriverDTO;
import strip.service.dto.UsermanageDTO;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/manager")
public class ManagerResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "username", "firstName", "lastName", "email", "active", "gender", "phoneNumber")
    );

    private final UsermanageService usermanageService;

    private final Logger log = LoggerFactory.getLogger(ManagerResource.class);

    public ManagerResource(UsermanageService usermanageService) {
        this.usermanageService = usermanageService;
    }

    @GetMapping("/GetAllUsers")
    public ResponseEntity<List<UsermanageDTO>> getAllUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Boolean active
    ) {
        log.debug("REST request to get all Users with filters");

        // Kiểm tra các thuộc tính sắp xếp có hợp lệ không
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        // Gọi service để lấy danh sách UsermanageDTO theo phân trang và lọc
        final Page<UsermanageDTO> page = usermanageService.getAllUsers(pageable, firstName, lastName, email, active);

        // Tạo HttpHeaders để hỗ trợ phân trang giống JHipster
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    // @GetMapping("/details/{id}")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    // public ResponseEntity<UsermanageDetailsDTO> getManagerDetailsById(@PathVariable Long id) {
    //     log.debug("REST request to get detailed Manager information: {}", id);
    //     return usermanageService.getUserDetailsById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @GetMapping("/driver/details/{username}")
    public ResponseEntity<DriverInfoDTO> getDriverDetailsByUsername(@PathVariable String username) {
        Optional<DriverInfoDTO> driverInfo = usermanageService.getDriverDetailsByUsername(username);
        return driverInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/vehicles/pending-approval")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.STAFF + "\")")
    public ResponseEntity<List<ConfirmingVehicleDriverDTO>> getAllPendingApprovalVehicles() {
        log.debug("REST request to get all vehicles pending approval (CONFIRMING)");
        List<ConfirmingVehicleDriverDTO> vehicles = usermanageService.getAllConfirmingVehicles();
        return ResponseEntity.ok().body(vehicles);
    }

    @PutMapping("/vehicles/confirming/{vehicleId}/approve")
    public ResponseEntity<String> approveVehicle(@PathVariable UUID vehicleId) {
        boolean success = usermanageService.approveVehicle(vehicleId);
        if (success) {
            return ResponseEntity.ok("Vehicle approved successfully.");
        }
        return ResponseEntity.badRequest().body("Vehicle not found.");
    }

    @PutMapping("/vehicles/confirming/{vehicleId}/reject")
    public ResponseEntity<String> rejectVehicle(@PathVariable UUID vehicleId) {
        boolean success = usermanageService.rejectVehicle(vehicleId);
        if (success) {
            return ResponseEntity.ok("Vehicle rejected successfully.");
        }
        return ResponseEntity.badRequest().body("Vehicle not found.");
    }

    @GetMapping("/drivers/confirming/getAll")
    public ResponseEntity<List<ConfirmingVehicleDriverDTO>> getConfirmingDrivers() {
        List<ConfirmingVehicleDriverDTO> confirmingDrivers = usermanageService.getConfirmingDrivers();
        return ResponseEntity.ok(confirmingDrivers);
    }

    @PatchMapping("/drivers/confirming/{driverId}/approve")
    public ResponseEntity<Void> approveDriver(@PathVariable UUID driverId) {
        log.debug("REST request to approve driver {}", driverId);
        usermanageService.approveDriver(driverId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/drivers/confirming/{driverId}/reject")
    public ResponseEntity<Void> rejectDriver(@PathVariable UUID driverId) {
        log.debug("REST request to reject driver {}", driverId);
        usermanageService.rejectDriver(driverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/packages/getAllPackage")
    public ResponseEntity<List<PackageDriverDTO>> getActivePackages() {
        List<PackageDriverDTO> activePackages = usermanageService.getActivePackages();
        return ResponseEntity.ok(activePackages);
    }

    @PostMapping("/packages/createPackage")
    public ResponseEntity<PackageDriverDTO> createPackage(@RequestBody PackageDriverDTO packageDriverDTO) throws URISyntaxException {
        PackageDriverDTO result = usermanageService.createPackage(packageDriverDTO);
        return ResponseEntity.created(new URI("/api/manager/packages/" + result.getPackageID())).body(result);
    }

    @PatchMapping("/package/{driverid}/expire")
    public ResponseEntity<PackageDriver> expirePackage(@PathVariable UUID driverid) {
        Optional<PackageDriver> updatedPackage = usermanageService.expirePackage(driverid);
        return updatedPackage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
