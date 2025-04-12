package strip.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Authority;
import strip.domain.Driver;
import strip.domain.DriverPointHistory;
import strip.domain.Feedback;
import strip.domain.PackageDriver;
import strip.domain.Rating;
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
import strip.repository.RatingRepository;
import strip.repository.ReportRepository;
import strip.repository.TripRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletDepositRepository;
import strip.repository.WalletTransactionRepository;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverPointHistoryDTO;
import strip.service.dto.DriverRawDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.FeedbackCusDTO;
import strip.service.dto.PackageDriverDTO;
import strip.service.dto.ReportCusDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.TripDetailDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.TripStopLocationDTO;
import strip.service.dto.TripStopLocationSkipTripDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.dto.VehicleRawDTO;
import strip.service.dto.WalletTransactionAdminDTO;
import strip.service.dto.WithdrawalRequestManageDTO;
import strip.service.mapper.DriverInfoMapper;
import strip.service.mapper.PackageDriverMapper;
import strip.service.mapper.TripStopLocationSkipTripMapper;
import strip.service.mapper.UsermanageMapper;
import strip.ultil.UserRoleUtils;
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
    private final WalletTransactionRepository walletTransactionRepository;
    private final TripStopLocationSkipTripMapper tripStopLocationSkipTripMapper;
    private final RatingRepository ratingRepository;

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
        ReportRepository reportRepository,
        WalletTransactionRepository walletTransactionRepository,
        TripStopLocationSkipTripMapper tripStopLocationSkipTripMapper,
        RatingRepository ratingRepository
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
        this.walletTransactionRepository = walletTransactionRepository;
        this.tripStopLocationSkipTripMapper = tripStopLocationSkipTripMapper;
        this.ratingRepository = ratingRepository;
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
        // Lấy toàn bộ danh sách user (chỉ trong 1 page) => có thể thiếu do filter sau
        // khi lấy
        List<UsermanageDTO> matchedUsers = userRepository
            .findAll()
            .stream()
            .filter(UserRoleUtils::isNormalUser)
            .map(user -> {
                Optional<UserDetail> userDetail = userDetailRepository.findByUserId(user.getId());
                return new UsermanageDTO(user, userDetail.orElse(null));
            })
            .filter(dto -> {
                if (firstName != null && !dto.getFirstName().toLowerCase().contains(firstName.toLowerCase())) return false;
                if (lastName != null && !dto.getLastName().toLowerCase().contains(lastName.toLowerCase())) return false;
                if (email != null && !dto.getEmail().toLowerCase().contains(email.toLowerCase())) return false;
                if (active != null && dto.isActive() != active) return false;
                return true;
            })
            .collect(Collectors.toList());

        // Thực hiện phân trang thủ công
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), matchedUsers.size());

        List<UsermanageDTO> pagedResult = matchedUsers.subList(Math.min(start, end), end);

        return new PageImpl<>(pagedResult, pageable, matchedUsers.size());
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
            .findByUser(user)
            .orElseThrow(() -> new BadRequestAlertException("Driver not found", "driver", "notfound"));

        // B3: Map DriverInfoDTO
        DriverInfoDTO dto = driverInfoMapper.toDriverInfoDTO(user, userDetail, driver);
        UUID driverId = driver.getDriverID();
        Double rating = ratingRepository.findAverageRatingByDriverId(driverId);
        dto.setAverageRating(rating);
        dto.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverId));
        dto.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverId));
        dto.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverId));

        // B4: Map Vehicle
        Set<DriverVehicleDTO> vehicleDTOs = driver
            .getVehicles()
            .stream()
            .map(vehicle -> {
                DriverVehicleDTO vdto = driverInfoMapper.toDriverVehicleDTO(vehicle);
                UUID vehicleId = vehicle.getVehicleID();
                vdto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicleId));
                vdto.setCarRegistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicleId));
                vdto.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicleId));
                vdto.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicleId));
                return vdto;
            })
            .collect(Collectors.toSet());
        dto.setUserId(userDetail.getAppUserDetail());
        dto.setVehicles(vehicleDTOs);
        return Optional.of(dto);
    }

    public List<ConfirmingVehicleDriverDTO> getAllConfirmingVehicles() {
        List<Vehicle> confirmingVehicles = vehicleRepository.findByStatus(VehicleStatus.CONFIRMING);

        return confirmingVehicles
            .stream()
            .map(vehicle -> {
                User user = vehicle.getDriver().getUser();
                UserDetail userDetail = userDetailRepository.findByUserId(user.getId()).orElse(null);
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

    public List<ConfirmingVehicleDriverDTO> getAllConfirmingDriversRaw() {
        List<ConfirmingVehicleDriverDTO> confirmingDrivers = new ArrayList<>();

        List<Driver> drivers = driverRepository.findByUsedtoDriverFalseAndDriverStatus(DriverStatus.CONFIRMING);
        log.debug("Found {} drivers with status CONFIRMING", drivers.size());

        for (Driver driver : drivers) {
            userRepository
                .findById(driver.getUser().getId())
                .filter(UserRoleUtils::isNormalUser)
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

    public Page<ConfirmingVehicleDriverDTO> getConfirmingDrivers(
        Pageable pageable,
        String firstName,
        String lastName,
        String email,
        String phone
    ) {
        List<ConfirmingVehicleDriverDTO> all = getAllConfirmingDriversRaw();

        // Lọc
        List<ConfirmingVehicleDriverDTO> filtered = all
            .stream()
            .filter(dto -> firstName == null || dto.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
            .filter(dto -> lastName == null || dto.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .filter(dto -> email == null || dto.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(dto -> phone == null || dto.getPhone().toLowerCase().contains(phone.toLowerCase()))
            .collect(Collectors.toList());

        // Sắp xếp (nếu có)
        Comparator<ConfirmingVehicleDriverDTO> comparator = null;
        for (Sort.Order order : pageable.getSort()) {
            Comparator<ConfirmingVehicleDriverDTO> c =
                switch (order.getProperty()) {
                    case "firstName" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getFirstName, String.CASE_INSENSITIVE_ORDER);
                    case "lastName" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getLastName, String.CASE_INSENSITIVE_ORDER);
                    case "email" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getEmail, String.CASE_INSENSITIVE_ORDER);
                    case "phone" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getPhone, String.CASE_INSENSITIVE_ORDER);
                    default -> null;
                };

            if (c == null) continue; // ✅ đúng cú pháp
            if (order.isDescending()) c = c.reversed();
            comparator = comparator == null ? c : comparator.thenComparing(c);
        }
        if (comparator != null) {
            filtered.sort(comparator);
        }

        // Phân trang
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<ConfirmingVehicleDriverDTO> paged = filtered.subList(Math.min(start, end), end);

        return new PageImpl<>(paged, pageable, filtered.size());
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
        dto.setRating(getAverageRatingForDriver(driverId));
        dto.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverId));
        dto.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverId));
        dto.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverId));

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

        dto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(v.getVehicleID()));
        dto.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(v.getVehicleID()));
        dto.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(v.getVehicleID()));
        dto.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(v.getVehicleID()));

        return dto;
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

    public Page<TripListDTO> getAllTrips(Pageable pageable, String startLocation, String endLocation, TripStatus status, UUID driverId) {
        Page<Trip> trips = tripRepository.findAllWithFilters(startLocation, endLocation, status, driverId, pageable);
        return trips.map(usermanageMapper::toTripListDTO);
    }

    public Optional<TripDetailDTO> getTripById(UUID id) {
        return tripRepository.findByIdWithRelations(id).map(this::toTripDetailDTO);
    }

    public TripDetailDTO toTripDetailDTO(Trip trip) {
        TripDetailDTO dto = usermanageMapper.toTripDetailDTO(trip); // ✅ map phần chính

        dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));

        if (trip.getVehicle() != null) {
            VehicleRawDTO vehicleDTO = usermanageMapper.toRawDTO(trip.getVehicle());

            vehicleDTO.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicleDTO.getVehicleID()));
            vehicleDTO.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicleDTO.getVehicleID()));
            vehicleDTO.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicleDTO.getVehicleID()));
            vehicleDTO.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicleDTO.getVehicleID()));

            dto.setVehicle(vehicleDTO);
        }

        if (trip.getDriver() != null && trip.getDriver().getUser() != null) {
            userDetailRepository
                .findByUserId(trip.getDriver().getUser().getId())
                .ifPresent(detail -> {
                    DriverRawDTO driverDTO = usermanageMapper.toRawDTO(trip.getDriver(), detail);
                    driverDTO.setRating(getAverageRatingForDriver(driverDTO.getDriverId()));

                    driverDTO.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverDTO.getDriverId()));
                    driverDTO.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverDTO.getDriverId()));
                    driverDTO.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverDTO.getDriverId()));
                    driverDTO.setAvatarUrl(imageUrlService.buildUserAvatarUrl(detail.getAppUserDetail()));

                    dto.setDriver(driverDTO);
                });
        }

        if (trip.getTripStopLocations() != null) {
            Set<TripStopLocationSkipTripDTO> stopDTOs = trip
                .getTripStopLocations()
                .stream()
                .map(tripStopLocationSkipTripMapper::toDto)
                .collect(Collectors.toSet());

            dto.setStoplocation(stopDTOs);
        }

        return dto;
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

        // Feedback Info
        dto.setFeedbackID(feedback.getFeedbackID());
        dto.setFeedbackType(feedback.getFeedbackType());
        dto.setFeedbackStatus(feedback.getFeedbackStatus());
        dto.setFeedbackDescription(feedback.getFeedbackDescription());
        dto.setFeedbackRating(feedback.getFeedbackRating());

        // Trip Info
        if (feedback.getTrip() != null) {
            Trip trip = feedback.getTrip();
            TripDTO tripDTO = usermanageMapper.toTripDTO(trip);
            tripDTO.setTripImg(null);
            dto.setTrip(tripDTO);
        }

        // Driver Info
        if (feedback.getDriver() != null) {
            Driver driver = feedback.getDriver();
            ConfirmingVehicleDriverDTO driverDTO = new ConfirmingVehicleDriverDTO();

            driverDTO.setDriverId(driver.getDriverID());

            // Set user info
            if (driver.getUser() != null) {
                User user = driver.getUser();
                driverDTO.setFirstName(user.getFirstName());
                driverDTO.setLastName(user.getLastName());
                driverDTO.setEmail(user.getEmail());
                driverDTO.setRating(getAverageRatingForDriver(driver.getDriverID()));

                userDetailRepository
                    .findByUserId(user.getId())
                    .ifPresent(detail -> {
                        driverDTO.setPhone(detail.getPhone());
                        driverDTO.setUserId(detail.getAppUserDetail());
                    });
            }

            // Set image URLs
            driverDTO.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driver.getDriverID()));
            driverDTO.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driver.getDriverID()));
            driverDTO.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driver.getDriverID()));

            // Set 1 vehicle if exists
            List<Vehicle> vehicles = vehicleRepository.findAllByDriver(driver);
            if (!vehicles.isEmpty()) {
                Vehicle vehicle = vehicles.get(0);
                ConfirmingVehicleDTO vehicleDTO = usermanageMapper.toConfirmingVehicleDTO(vehicle);
                driverDTO.setVehicle(vehicleDTO);
            }

            // ✅ Lấy thời gian rating nếu đủ thông tin
            if (feedback.getTrip() != null && feedback.getDriver() != null && feedback.getUser() != null) {
                ratingRepository
                    .findByTripAndDriverAndUser(feedback.getTrip(), feedback.getDriver(), feedback.getUser())
                    .map(Rating::getRatingTime)
                    .ifPresent(dto::setFeedbackTime);
            }

            dto.setDriver(driverDTO);
            dto.getDriver().setVehicle(null);
            dto.getTrip().setVehicle(null);
            dto.getTrip().setDriver(null);
        }

        // User Info
        if (feedback.getUser() != null) {
            User user = feedback.getUser();
            UsermanageDTO userDTO = new UsermanageDTO();

            userDTO.setUsername(user.getLogin());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setActive(user.isActivated());

            // Roles
            Set<String> roles = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
            userDTO.setRoles(roles);

            // Gender, Phone, Avatar
            userDetailRepository
                .findByUserId(user.getId())
                .ifPresent(detail -> {
                    userDTO.setUserId(detail.getAppUserDetail());
                    userDTO.setGender(detail.getGender());
                    userDTO.setPhoneNumber(detail.getPhone());
                });

            dto.setUser(userDTO);
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

    public Page<WalletTransactionAdminDTO> getSystemTransactions(
        Pageable pageable,
        WalletTransactionType walletType,
        Instant fromDate,
        Instant toDate
    ) {
        List<WalletTransaction> transactions = walletTransactionRepository.findAll();

        List<WalletTransaction> filtered = transactions
            .stream()
            .filter(tx -> walletType == null || tx.getWalletType() == walletType)
            .filter(tx -> fromDate == null || !tx.getDate().isBefore(fromDate))
            .filter(tx -> toDate == null || !tx.getDate().isAfter(toDate))
            .sorted(Comparator.comparing(WalletTransaction::getDate).reversed()) // sắp xếp theo thời gian mới nhất
            .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<WalletTransactionAdminDTO> content = filtered
            .subList(Math.min(start, end), end)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, filtered.size());
    }

    private WalletTransactionAdminDTO mapToDTO(WalletTransaction tx) {
        WalletTransactionAdminDTO dto = new WalletTransactionAdminDTO();
        dto.setTransactionId(tx.getTransID());
        dto.setType(tx.getWalletType());
        dto.setAmount(tx.getAmount());
        dto.setCreatedDate(tx.getDate());
        dto.setFromOwner(resolveFrom(tx));
        dto.setToOwner(resolveTo(tx));
        dto.setDescription(buildDescription(tx));
        return dto;
    }

    private String resolveUserName(UserWallet wallet) {
        if (wallet == null || wallet.getUser() == null) return "Unknown User";
        return wallet.getUser().getFirstName() + " " + wallet.getUser().getLastName() + " (User)";
    }

    private String resolveFrom(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            case DEPOSIT -> "System";
            case WITHDRAW, DRIVER_BUY_PACKAGE, DRIVER_CREATE_TRIP_FEE -> resolveUserName(tx.getUserWallet());
            case SYSTEM_GAIN_CREATE_TRIP_FEE, SYSTEM_GAIN_PACKAGE_FEE -> resolveUserName(tx.getUserWallet());
            case SYSTEM_REFUND_TO_DRIVER_DONE_TRIP -> "System";
            default -> "Unknown";
        };
    }

    private String resolveTo(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            case DEPOSIT, SYSTEM_REFUND_TO_PASSENGER -> resolveUserName(tx.getUserWallet());
            case SYSTEM_GAIN_PACKAGE_FEE, SYSTEM_GAIN_CREATE_TRIP_FEE -> "System";
            case DRIVER_DONE_TRIP_REFUND -> resolveUserName(tx.getUserWallet());
            default -> "Unknown";
        };
    }

    private String buildDescription(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            case DEPOSIT -> "Người dùng nạp tiền vào ví";
            case WITHDRAW -> "Người dùng rút tiền từ ví";
            case REFUND -> "Hoàn tiền về ví người dùng";
            case DRIVER_CREATE_TRIP_FEE -> "Tài xế tạo chuyến đi, trừ phí từ ví tài xế";
            case DRIVER_DONE_TRIP_REFUND -> "Hoàn tiền lại cho tài xế khi hoàn tất chuyến đi";
            case DRIVER_DONE_TRIP_FEE -> "Tổng tiền thu được từ các ghế đã đặt của chuyến đi";
            case PASSENGER_APPROVE_FEE -> "Tiền cọc của hành khách khi đặt chỗ chuyến đi";
            case SYSTEM_GAIN_CREATE_TRIP_FEE -> "Hệ thống thu phí từ việc tài xế tạo chuyến";
            case SYSTEM_GAIN_PASSENGER_APPROVE_FEE -> "Hệ thống giữ lại tiền cọc từ hành khách";
            case SYSTEM_GAIN_DONE_TRIP_FEE -> "Hệ thống thu phí từ chuyến đi hoàn thành";
            case DRIVER_BUY_PACKAGE -> "Tài xế mua gói dịch vụ";
            case SYSTEM_GAIN_PACKAGE_FEE -> "Hệ thống thu tiền từ tài xế mua gói";
            case SYSTEM_REFUND_TO_DRIVER_DONE_TRIP -> "Hệ thống hoàn tiền cho tài xế (sau chuyến đi)";
            case SYSTEM_REFUND_TO_PASSENGER -> "Hệ thống hoàn tiền cho hành khách";
            default -> "Giao dịch hệ thống khác";
        };
    }

    public Page<WalletTransactionAdminDTO> getSystemIncomeTransactions(
        Pageable pageable,
        WalletTransactionType walletType,
        Instant fromDate,
        Instant toDate
    ) {
        List<WalletTransactionType> incomeTypes = List.of(
            WalletTransactionType.SYSTEM_GAIN_CREATE_TRIP_FEE,
            WalletTransactionType.SYSTEM_GAIN_PASSENGER_APPROVE_FEE,
            WalletTransactionType.SYSTEM_GAIN_DONE_TRIP_FEE,
            WalletTransactionType.SYSTEM_GAIN_PACKAGE_FEE
        );

        List<WalletTransactionType> typesToFilter = (walletType != null && incomeTypes.contains(walletType))
            ? List.of(walletType)
            : incomeTypes;

        // 👇 Gán mặc định nếu không truyền vào
        Instant from = (fromDate != null) ? fromDate : Instant.EPOCH;
        Instant to = (toDate != null) ? toDate : Instant.now();

        Page<WalletTransaction> txPage = walletTransactionRepository.findByWalletTypeInAndDateBetween(typesToFilter, from, to, pageable);

        return txPage.map(this::mapToDTO);
    }

    public double getAverageRatingForDriver(UUID driverId) {
        Double avg = ratingRepository.findAverageRatingByDriverId(driverId);
        return avg != null ? avg : 0.0;
    }
}
