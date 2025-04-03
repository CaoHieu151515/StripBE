package strip.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.domain.Driver;
import strip.domain.DriverPointHistory;
import strip.domain.Feedback;
import strip.domain.PackageDriver;
import strip.domain.Report;
import strip.domain.Trip;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.UserWallet;
import strip.domain.Vehicle;
import strip.domain.WalletDeposit;
import strip.domain.WalletTransaction;
import strip.domain.enumeration.DriverPointHistoryStatus;
import strip.domain.enumeration.DriverStatus;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;
import strip.domain.enumeration.PackageDriverStatus;
import strip.domain.enumeration.PaymentStatus;
import strip.domain.enumeration.ReportStatus;
import strip.domain.enumeration.ReportType;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.TripStatus;
import strip.domain.enumeration.VehicleStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.DriverPointHistoryRepository;
import strip.repository.DriverRepository;
import strip.repository.FeedbackRepository;
import strip.repository.PackageDriverRepository;
import strip.repository.ReportRepository;
import strip.repository.TripRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletDepositRepository;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverPointHistoryDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.FeedbackCusDTO;
import strip.service.dto.PackageDriverDTO;
import strip.service.dto.ReportCusDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripStopLocationDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.dto.WithdrawalRequestManageDTO;
import strip.service.mapper.DriverInfoMapper;
import strip.service.mapper.PackageDriverMapper;
import strip.service.mapper.UsermanageMapper;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional
public class UsermanageService {

    private final Logger log = LoggerFactory.getLogger(UsermanageService.class);
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final DriverRepository driverRepository;
    private final DriverInfoMapper driverInfoMapper;
    private final VehicleRepository vehicleRepository;
    private final PackageDriverRepository packageDriverRepository;
    private final PackageDriverMapper packageDriverMapper;
    private final WalletDepositRepository walletDepositRepository;
    private final UsermanageMapper usermanageMapper;
    private final UserWalletRepository userWalletRepository;
    private final TripRepository tripRepository;
    private final ImageUrlService imageUrlService;
    private final FeedbackRepository feedbackRepository;
    private final DriverPointHistoryRepository driverPointHistoryRepository;
    private final ReportRepository reportRepository;

    public UsermanageService(
        UserRepository userRepository,
        UserDetailRepository userDetailRepository,
        DriverRepository driverRepository,
        DriverInfoMapper driverInfoMapper,
        VehicleRepository vehicleRepository,
        PackageDriverRepository packageDriverRepository,
        PackageDriverMapper packageDriverMapper,
        WalletDepositRepository walletDepositRepository,
        UsermanageMapper usermanageMapper,
        UserWalletRepository userWalletRepository,
        TripRepository tripRepository,
        ImageUrlService imageUrlService,
        FeedbackRepository feedbackRepository,
        DriverPointHistoryRepository driverPointHistoryRepository,
        ReportRepository reportRepository
    ) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.driverRepository = driverRepository;
        this.driverInfoMapper = driverInfoMapper;
        this.vehicleRepository = vehicleRepository;
        this.packageDriverRepository = packageDriverRepository;
        this.packageDriverMapper = packageDriverMapper;
        this.walletDepositRepository = walletDepositRepository;
        this.usermanageMapper = usermanageMapper;
        this.userWalletRepository = userWalletRepository;
        this.tripRepository = tripRepository;
        this.imageUrlService = imageUrlService;
        this.feedbackRepository = feedbackRepository;
        this.driverPointHistoryRepository = driverPointHistoryRepository;
        this.reportRepository = reportRepository;
    }

    public List<UsermanageDTO> getAllUsers() {
        return userRepository
            .findAll()
            .stream()
            .map(user -> {
                UserDetail userDetail = userDetailRepository.findById(user.getId()).orElse(null);
                return new UsermanageDTO(user, userDetail);
            })
            .collect(Collectors.toList());
    }

    public UsermanageDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserDetail userDetail = userDetailRepository.findById(id).orElse(null);
        return new UsermanageDTO(user, userDetail);
    }

    public Page<UsermanageDTO> getAllUsers(Pageable pageable, String firstName, String lastName, String email, Boolean active) {
        Page<User> users = userRepository.findAll(pageable);

        // Chuyển đổi danh sách User thành danh sách UsermanageDTO
        List<UsermanageDTO> filteredUsers = users
            .stream()
            .map(user -> {
                Optional<UserDetail> userDetail = userDetailRepository.findById(user.getId());
                UsermanageDTO dto = new UsermanageDTO(user, userDetail.orElse(null));

                // Kiểm tra các điều kiện lọc
                if (firstName != null && !dto.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                    return null;
                }
                if (lastName != null && !dto.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                    return null;
                }
                if (email != null && !dto.getEmail().toLowerCase().contains(email.toLowerCase())) {
                    return null;
                }
                if (active != null && dto.isActive() != active) {
                    return null;
                }
                return dto;
            })
            .filter(dto -> dto != null) // Loại bỏ các giá trị null do không phù hợp điều kiện lọc
            .collect(Collectors.toList());

        return new PageImpl<>(filteredUsers, pageable, filteredUsers.size()); // Trả về Page<UsermanageDTO>
    }

    public Optional<UsermanageDetailsDTO> getUserDetailsById(Long id) {
        return userRepository
            .findById(id)
            .map(user -> {
                Optional<UserDetail> userDetail = userDetailRepository.findByUser(user);
                Optional<Driver> driver = driverRepository.findByUser(user);
                return new UsermanageDetailsDTO(user, userDetail.orElse(null), driver.orElse(null));
            });
    }

    @Transactional
    public Optional<DriverInfoDTO> getDriverDetailsByUserId(UUID userDetailId) {
        // B1: Tìm UserDetail
        UserDetail userDetail = userDetailRepository
            .findByAppUserDetail(userDetailId)
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "userDetail", "notfound"));

        User user = userDetail.getUser();

        // B2: Tìm Driver theo User
        Driver driver = driverRepository
            .findById(user.getId())
            .orElseThrow(() -> new BadRequestAlertException("Driver not found", "driver", "notfound"));

        // B3: Map DriverInfoDTO
        DriverInfoDTO dto = driverInfoMapper.toDriverInfoDTO(user, userDetail, driver);

        UUID driverId = driver.getDriverID();
        dto.setDriverLicenseUrl(buildDriverLicenseUrl(driverId));
        dto.setIdentityCardFaceUpUrl(buildIdentityCardFaceUpUrl(driverId));
        dto.setIdentityCardFaceDownUrl(buildIdentityCardFaceDownUrl(driverId));

        // B4: Map Vehicle
        Set<DriverVehicleDTO> vehicleDTOs = driver
            .getVehicles()
            .stream()
            .map(vehicle -> {
                DriverVehicleDTO vdto = driverInfoMapper.toDriverVehicleDTO(vehicle);
                UUID vehicleId = vehicle.getVehicleID();
                vdto.setVehicleImageUrl(buildVehicleImageUrl(vehicleId));
                vdto.setCarRegistrationUrl(buildCarregistrationUrl(vehicleId));
                vdto.setVehicleInspectionCertificateUrl(buildInspectionCertificateUrl(vehicleId));
                vdto.setCarInsuranceUrl(buildCarInsuranceUrl(vehicleId));
                return vdto;
            })
            .collect(Collectors.toSet());

        dto.setVehicles(vehicleDTOs);
        return Optional.of(dto);
    }

    public List<ConfirmingVehicleDriverDTO> getAllConfirmingVehicles() {
        List<Vehicle> confirmingVehicles = vehicleRepository.findByStatus(VehicleStatus.CONFIRMING);

        return confirmingVehicles
            .stream()
            .map(vehicle -> {
                User user = vehicle.getDriver().getUser();
                UserDetail userDetail = userDetailRepository.findById(user.getId()).orElse(null);
                Driver driver = vehicle.getDriver();

                return mapToConfirmingVehicleDriverDTO(user, userDetail, driver, vehicle);
            })
            .collect(Collectors.toList());
    }

    public boolean approveVehicle(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new BadRequestAlertException("Vehicle not found", "vehicle", "notfound"));

        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicleRepository.save(vehicle);
        return true;
    }

    public boolean rejectVehicle(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new BadRequestAlertException("Vehicle not found", "vehicle", "notfound"));

        vehicle.setStatus(VehicleStatus.REJECTED);
        vehicleRepository.save(vehicle);
        return true;
    }

    public List<ConfirmingVehicleDriverDTO> getConfirmingDrivers() {
        List<ConfirmingVehicleDriverDTO> confirmingDrivers = new ArrayList<>();

        List<Driver> drivers = driverRepository.findByUsedtoDriverFalseAndDriverStatus(DriverStatus.CONFIRMING);
        log.debug("Found {} drivers with status CONFIRMING", drivers.size());

        for (Driver driver : drivers) {
            userRepository
                .findById(driver.getUser().getId())
                .ifPresent(user -> {
                    Optional<UserDetail> userDetail = userDetailRepository.findByUserId(user.getId());
                    vehicleRepository
                        .findFirstByDriver_DriverIDAndStatus(driver.getDriverID(), VehicleStatus.CONFIRMING)
                        .ifPresent(vehicle -> {
                            ConfirmingVehicleDriverDTO dto = mapToConfirmingVehicleDriverDTO(
                                user,
                                userDetail.orElse(null),
                                driver,
                                vehicle
                            );
                            confirmingDrivers.add(dto);
                        });
                });
        }

        return confirmingDrivers;
    }

    @Transactional
    public void approveDriver(UUID driverId) {
        Driver driver = driverRepository
            .findByDriverID(driverId)
            .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));

        log.debug("Approving driver: {}", driverId);

        // Tìm xe đầu tiên có trạng thái CONFIRMING
        vehicleRepository
            .findFirstByDriver_DriverIDAndStatus(driver.getDriverID(), VehicleStatus.CONFIRMING)
            .ifPresent(vehicle -> {
                vehicle.setStatus(VehicleStatus.ACTIVE);
                vehicleRepository.save(vehicle);
            });

        // Cập nhật trạng thái tài xế
        driver.setDriverStatus(DriverStatus.ACTIVE);
        driver.setUsedtoDriver(true);
        driverRepository.save(driver);

        log.debug("Driver {} approved successfully", driverId);
    }

    @Transactional
    public void rejectDriver(UUID driverId) {
        Driver driver = driverRepository
            .findByDriverID(driverId)
            .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverId));

        log.debug("Rejecting driver: {}", driverId);

        // Tìm xe đầu tiên có trạng thái CONFIRMING
        vehicleRepository
            .findFirstByDriver_DriverIDAndStatus(driver.getDriverID(), VehicleStatus.CONFIRMING)
            .ifPresent(vehicle -> {
                vehicle.setStatus(VehicleStatus.REJECTED);
                vehicleRepository.save(vehicle);
            });

        // Cập nhật trạng thái tài xế
        driver.setDriverStatus(DriverStatus.NOT_DRIVER);
        driverRepository.save(driver);

        log.debug("Driver {} rejected successfully", driverId);
    }

    public List<PackageDriverDTO> getActivePackages() {
        return packageDriverRepository
            .findByStatus(PackageDriverStatus.ACTIVE)
            .stream()
            .map(packageDriverMapper::toDto)
            .collect(Collectors.toList());
    }

    public PackageDriverDTO createPackage(PackageDriverDTO packageDriverDTO) {
        PackageDriver packageDriver = packageDriverMapper.toEntity(packageDriverDTO);
        packageDriver.setPackageID(UUID.randomUUID());
        packageDriver.setStatus(PackageDriverStatus.ACTIVE);
        packageDriver = packageDriverRepository.save(packageDriver);
        return packageDriverMapper.toDto(packageDriver);
    }

    public Optional<PackageDriver> expirePackage(UUID packageId) {
        return packageDriverRepository
            .findByPackageID(packageId)
            .map(packageDriver -> {
                packageDriver.setStatus(PackageDriverStatus.EXPIRED);
                return packageDriverRepository.save(packageDriver);
            });
    }

    private ConfirmingVehicleDriverDTO mapToConfirmingVehicleDriverDTO(User user, UserDetail userDetail, Driver driver, Vehicle vehicle) {
        ConfirmingVehicleDriverDTO dto = driverInfoMapper.toConfirmingVehicleDTO(user, userDetail, driver, vehicle);

        UUID driverId = driver.getDriverID();
        dto.setIdentityCardFaceUpUrl(buildIdentityCardFaceUpUrl(driverId));
        dto.setIdentityCardFaceDownUrl(buildIdentityCardFaceDownUrl(driverId));
        dto.setDriverLicenseUrl(buildDriverLicenseUrl(driverId));

        ConfirmingVehicleDTO vehicleDTO = mapVehicleToConfirmingVehicleDTO(vehicle);
        dto.setVehicle(vehicleDTO);

        return dto;
    }

    private ConfirmingVehicleDTO mapVehicleToConfirmingVehicleDTO(Vehicle v) {
        ConfirmingVehicleDTO dto = new ConfirmingVehicleDTO();

        dto.setId(v.getId());
        dto.setVehicleID(v.getVehicleID());
        dto.setVehicleType(v.getVehicleType());
        dto.setVehicleNumber(v.getVehicleNumber());
        dto.setNumberOfSeats(v.getNumberOfSeats());
        dto.setVehicleColor(v.getVehicleColor());
        dto.setVehicleBrand(v.getVehicleBrand());
        dto.setStatus(v.getStatus());

        dto.setVehicleImageUrl(buildVehicleImageUrl(v.getVehicleID()));
        dto.setCarregistrationUrl(buildCarregistrationUrl(v.getVehicleID()));
        dto.setVehicleInspectionCertificateUrl(buildInspectionCertificateUrl(v.getVehicleID()));
        dto.setCarInsuranceUrl(buildCarInsuranceUrl(v.getVehicleID()));

        return dto;
    }

    public String buildDriverLicenseUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/license/")
            .path(driverId.toString())
            .toUriString();
    }

    public String buildIdentityCardFaceUpUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/identity-card-up/")
            .path(driverId.toString())
            .toUriString();
    }

    public String buildIdentityCardFaceDownUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/identity-card-down/")
            .path(driverId.toString())
            .toUriString();
    }

    public String buildVehicleImageUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/images/vehicle/").path(vehicleId.toString()).toUriString();
    }

    public String buildCarregistrationUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/carregistration/")
            .path(vehicleId.toString())
            .toUriString();
    }

    public String buildInspectionCertificateUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/inspection/")
            .path(vehicleId.toString())
            .toUriString();
    }

    public String buildCarInsuranceUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/insurance/")
            .path(vehicleId.toString())
            .toUriString();
    }

    public List<WithdrawalRequestManageDTO> getPendingWithdrawalRequests() {
        List<WalletDeposit> deposits = walletDepositRepository.findByStatus(PaymentStatus.PENDING);
        return usermanageMapper.toWithdrawalRequestManageDTOs(deposits);
    }

    @Transactional
    public void approveWithdrawal(UUID depositId) {
        WalletDeposit deposit = walletDepositRepository
            .findById(depositId)
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy yêu cầu rút tiền", "wallet", "notfound"));

        if (deposit.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestAlertException("Yêu cầu đã được xử lý rồi", "wallet", "alreadyProcessed");
        }

        deposit.setStatus(PaymentStatus.SUCCESS);

        log.info("✅ Đã xác nhận rút tiền cho user: {}", deposit.getUserWallet().getUser().getLogin());
    }

    @Transactional
    public void rejectWithdrawal(UUID depositId) {
        WalletDeposit deposit = walletDepositRepository
            .findById(depositId)
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy yêu cầu rút tiền", "wallet", "notfound"));

        if (deposit.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestAlertException("Yêu cầu đã được xử lý rồi", "wallet", "alreadyProcessed");
        }

        // ❌ Đánh dấu từ chối
        deposit.setStatus(PaymentStatus.FAILED);

        // 🔁 Tạo REFUND transaction để trả lại tiền cho user
        WalletTransaction transaction = new WalletTransaction();
        transaction.setTransID(UUID.randomUUID());
        transaction.setAmount(deposit.getAmount());
        transaction.setDate(Instant.now());
        transaction.setWalletType(WalletTransactionType.REFUND);
        transaction.setTransStatus(TransactionStatus.SUCCESS);

        UserWallet userWallet = deposit.getUserWallet();

        if (userWallet == null) {
            throw new BadRequestAlertException("Không tìm thấy ví người dùng", "wallet", "missingWallet");
        }

        userWallet.addWalletTransactionAndUpdateBalance(transaction);
        userWalletRepository.save(userWallet);

        log.info("❌ Đã từ chối rút tiền và hoàn tiền lại cho user: {}", userWallet.getUser().getLogin());
    }

    public Page<TripCusDTO> getAllTrips(Pageable pageable, String startLocation, String endLocation, TripStatus status, UUID driverId) {
        return tripRepository
            .findAllWithFilters(startLocation, endLocation, status, driverId, pageable)
            .map(this::toTripCusDTOWithCustomFields);
    }

    public TripCusDTO toTripCusDTOWithCustomFields(Trip trip) {
        TripCusDTO dto = usermanageMapper.toTripCusDTO(trip);

        // Trip image
        dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));

        // Driver name
        if (trip.getDriver() != null && trip.getDriver().getUser() != null) {
            User user = trip.getDriver().getUser();
            dto.setDriverName(user.getFirstName() + " " + user.getLastName());

            // Lấy số điện thoại từ UserDetail
            userDetailRepository.findById(user.getId()).map(UserDetail::getPhone).ifPresent(dto::setDriverPhone);
        }

        // Vehicle image
        if (trip.getVehicle() != null) {
            dto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(trip.getVehicle().getVehicleID()));
        }

        // Stop locations
        if (trip.getTripStopLocations() != null) {
            List<TripStopLocationDTO> stops = trip
                .getTripStopLocations()
                .stream()
                .map(usermanageMapper::toTripStopLocationDTO)
                .collect(Collectors.toList());
            dto.setStopLocations(stops);
        }

        return dto;
    }

    @Transactional
    public void approveTrip(UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Trip not found", "trip", "notfound"));

        trip.setTripStatus(TripStatus.UPCOMING);
        tripRepository.save(trip);
    }

    @Transactional
    public void rejectTrip(UUID tripId, String reason) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Trip not found", "trip", "notfound"));

        trip.setTripStatus(TripStatus.REJECTED);
        trip.setCancelReason(reason);
        tripRepository.save(trip);
    }

    public Page<FeedbackCusDTO> getAllFeedbacks(Pageable pageable, FeedbackStatus status, FeedbackType type) {
        Page<Feedback> feedbackPage = feedbackRepository.findAllWithFilters(status, type, pageable);

        return feedbackPage.map(this::convertToFeedbackCusDTO);
    }

    public FeedbackCusDTO convertToFeedbackCusDTO(Feedback feedback) {
        FeedbackCusDTO dto = new FeedbackCusDTO();

        dto.setFeedbackID(feedback.getFeedbackID());
        dto.setFeedbackType(feedback.getFeedbackType());
        dto.setFeedbackStatus(feedback.getFeedbackStatus());
        dto.setFeedbackDescription(feedback.getFeedbackDescription());
        dto.setFeedbackRating(feedback.getFeedbackRating());

        dto.setTripId(feedback.getTrip() != null ? feedback.getTrip().getTripID() : null);
        dto.setDriverId(feedback.getDriver() != null ? feedback.getDriver().getDriverID() : null);

        if (feedback.getUser() != null) {
            Optional<UserDetail> userDetailOpt = userDetailRepository.findByUser(feedback.getUser());
            userDetailOpt.ifPresent(userDetail -> {
                dto.setUserId(userDetail.getAppUserDetail());
            });
        }

        return dto;
    }

    public Page<DriverPointHistoryDTO> getPointHistoryByUserDetail(UUID userDetailId, Pageable pageable) {
        Page<DriverPointHistory> histories = driverPointHistoryRepository.findByUserDetail_AppUserDetail(userDetailId, pageable);
        return histories.map(usermanageMapper::toDto);
    }

    @Transactional
    public DriverPointHistoryDTO refundByHistory(UUID pointId) {
        DriverPointHistory original = driverPointHistoryRepository
            .findByPointId(pointId)
            .orElseThrow(() -> new BadRequestAlertException("History not found", "pointHistory", "notfound"));

        if (original.getStatus() != DriverPointHistoryStatus.DONE) {
            throw new BadRequestAlertException("Only DONE points can be refunded", "pointHistory", "invalidstatus");
        }

        Instant now = Instant.now();
        Instant expiredTime = original.getDate().plus(48, ChronoUnit.HOURS);

        if (now.isAfter(expiredTime)) {
            throw new BadRequestAlertException("Refund expired. Only allowed within 48 hours", "driverPointHistory", "expired");
        }

        Driver driver = original.getDriver();
        if (driver == null) {
            throw new BadRequestAlertException("Driver not found for history", "driver", "null");
        }

        // ✅ Cộng điểm lại
        driver.setDriverPoint(driver.getDriverPoint() + original.getPoint());
        driverRepository.save(driver); // hoặc không cần nếu có @Transactional

        // ✅ Cập nhật trạng thái bản ghi
        original.setStatus(DriverPointHistoryStatus.REFUND);
        driverPointHistoryRepository.save(original);

        return usermanageMapper.toDto(original);
    }

    public Page<ReportCusDTO> getAllReports(Pageable pageable, ReportStatus status, ReportType type) {
        return reportRepository
            .findAllWithFilters(status, type, pageable)
            .map(report -> {
                ReportCusDTO dto = usermanageMapper.toReportCusDTO(report);
                if (report.getUser() != null) {
                    userDetailRepository.findByUser(report.getUser()).ifPresent(detail -> dto.setUserId(detail.getAppUserDetail()));
                }
                return dto;
            });
    }

    @Transactional
    public void handleReport(UUID reportId, String reason, int point) {
        Report report = reportRepository
            .findByReportID(reportId)
            .orElseThrow(() -> new BadRequestAlertException("Report not found", "report", "notfound"));

        if (report.getReportStatus() == ReportStatus.DONE) {
            throw new BadRequestAlertException("Report already handled", "report", "alreadydone");
        }

        report.setReportStatus(ReportStatus.DONE);
        reportRepository.save(report);

        Driver driver = report.getDriver();
        if (driver == null) {
            throw new BadRequestAlertException("This report does not target a driver", "report", "nodriver");
        }

        DriverPointHistory history = new DriverPointHistory();
        history.setPointId(UUID.randomUUID());
        history.setPoint(point);
        history.setReason(reason != null ? reason : "Trừ điểm do bị báo cáo");
        history.setDate(Instant.now());
        history.setStatus(DriverPointHistoryStatus.DONE);

        if (report.getUser() != null) {
            userDetailRepository.findByUser(report.getUser()).ifPresent(history::setUserDetail);
        }

        driver.addDriverPointHistory(history);

        driverRepository.save(driver);
    }
}
