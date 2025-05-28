package strip.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
import strip.domain.SystemWallet;
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
import strip.repository.SystemWalletRepository;
import strip.repository.TripRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletDepositRepository;
import strip.repository.WalletTransactionRepository;
import strip.security.SecurityUtils;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverPointHistoryListDTO;
import strip.service.dto.DriverPointHistoryRefundDTO;
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
import strip.ultil.TripCodeUtils;
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
    private final SystemWalletRepository systemWalletRepository;

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
        RatingRepository ratingRepository,
        SystemWalletRepository systemWalletRepository
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
        this.systemWalletRepository = systemWalletRepository;
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
        Set<String> roles = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());

        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_STAFF")) {
            throw new BadRequestAlertException("Staff/Admin không có thông tin tài xế", "driver", "invalid-role");
        }

        // B2: Tìm Driver theo User
        Driver driver = driverRepository
            .findByUser_id(user.getId())
            .orElseThrow(() -> new BadRequestAlertException("Driver not found", "driver", "notfound"));

        // B3: Map DriverInfoDTO
        DriverInfoDTO dto = driverInfoMapper.toDriverInfoDTO(user, userDetail, driver);
        UUID driverId = driver.getDriverID();
        dto.setAverageRating(Optional.ofNullable(ratingRepository.findAverageRatingByDriverId(driverId)).orElse(0.0));
        dto.setAvatar(imageUrlService.buildUserAvatarUrl(userDetailId));
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

    public Page<ConfirmingVehicleDriverDTO> getAllConfirmingVehicles(
        Pageable pageable,
        String firstName,
        String lastName,
        String email,
        String phone,
        UUID driverId
    ) {
        List<ConfirmingVehicleDriverDTO> all = getAllConfirmingDriversRawDriver();

        // 🔍 Filter theo từng trường
        List<ConfirmingVehicleDriverDTO> filtered = all
            .stream()
            .filter(dto -> firstName == null || dto.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
            .filter(dto -> lastName == null || dto.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .filter(dto -> email == null || dto.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(dto -> phone == null || dto.getPhone().toLowerCase().contains(phone.toLowerCase()))
            .filter(dto -> driverId == null || driverId.equals(dto.getDriverId()))
            .toList();

        // ↕️ Sort
        Comparator<ConfirmingVehicleDriverDTO> comparator = pageable
            .getSort()
            .stream()
            .map(order -> {
                Comparator<ConfirmingVehicleDriverDTO> c =
                    switch (order.getProperty()) {
                        case "firstName" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getFirstName, String.CASE_INSENSITIVE_ORDER);
                        case "lastName" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getLastName, String.CASE_INSENSITIVE_ORDER);
                        case "email" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getEmail, String.CASE_INSENSITIVE_ORDER);
                        case "phone" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getPhone, String.CASE_INSENSITIVE_ORDER);
                        case "rating" -> Comparator.comparingDouble(ConfirmingVehicleDriverDTO::getRating);
                        case "driverId" -> Comparator.comparing(
                            ConfirmingVehicleDriverDTO::getDriverId,
                            Comparator.nullsLast(UUID::compareTo)
                        );
                        default -> null;
                    };
                return (c != null && order.isDescending()) ? c.reversed() : c;
            })
            .filter(Objects::nonNull)
            .reduce(Comparator::thenComparing)
            .orElse(null);

        if (comparator != null) {
            filtered = filtered.stream().sorted(comparator).toList();
        }

        // 📄 Pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<ConfirmingVehicleDriverDTO> pageContent = (start <= end) ? filtered.subList(start, end) : List.of();

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    public <T> Page<T> toPage(List<T> list, Pageable pageable) {
        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<T> content = (start > end) ? List.of() : list.subList(start, end);
        return new PageImpl<>(content, pageable, total);
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

    public List<ConfirmingVehicleDriverDTO> getAllConfirmingDriversRawDriver() {
        List<ConfirmingVehicleDriverDTO> confirmingDrivers = new ArrayList<>();

        List<Driver> drivers = driverRepository.findByUsedtoDriverFalseAndDriverStatus(DriverStatus.CONFIRMING);
        log.debug("Found {} drivers with status CONFIRMING", drivers.size());

        for (Driver driver : drivers) {
            userRepository
                .findById(driver.getUser().getId())
                .filter(UserRoleUtils::isDriver)
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
        String phone,
        UUID driverId
    ) {
        List<ConfirmingVehicleDriverDTO> all = getAllConfirmingDriversRaw();

        // Lọc
        List<ConfirmingVehicleDriverDTO> filtered = all
            .stream()
            .filter(dto -> firstName == null || dto.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
            .filter(dto -> lastName == null || dto.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .filter(dto -> email == null || dto.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(dto -> phone == null || dto.getPhone().toLowerCase().contains(phone.toLowerCase()))
            .filter(dto -> driverId == null || driverId.equals(dto.getDriverId()))
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
                    case "driverId" -> Comparator.comparing(ConfirmingVehicleDriverDTO::getDriverId, Comparator.nullsLast(UUID::compareTo));
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

    public Page<PackageDriverDTO> getAllPackagesWithFilter(
        Pageable pageable,
        String name,
        Double price,
        Integer time,
        PackageDriverStatus status,
        Instant createdDate,
        Instant expireDate
    ) {
        List<PackageDriverDTO> filtered = packageDriverRepository
            .findAll()
            .stream()
            .filter(pkg -> name == null || pkg.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(pkg -> price == null || Objects.equals(pkg.getPrice(), price))
            .filter(pkg -> time == null || Objects.equals(pkg.getTime(), time))
            .filter(pkg -> status == null || pkg.getStatus() == status)
            .filter(pkg -> createdDate == null || (pkg.getCreatedDate() != null && pkg.getCreatedDate().equals(createdDate)))
            .filter(pkg -> expireDate == null || (pkg.getExpireDate() != null && pkg.getExpireDate().equals(expireDate)))
            .map(packageDriverMapper::toDto)
            .toList();

        // 🔀 Sort theo pageable
        Comparator<PackageDriverDTO> comparator = pageable
            .getSort()
            .stream()
            .map(order -> {
                Comparator<PackageDriverDTO> c =
                    switch (order.getProperty()) {
                        case "name" -> Comparator.comparing(PackageDriverDTO::getName, String.CASE_INSENSITIVE_ORDER);
                        case "price" -> Comparator.comparing(PackageDriverDTO::getPrice);
                        case "time" -> Comparator.comparing(PackageDriverDTO::getTime);
                        case "createdDate" -> Comparator.comparing(PackageDriverDTO::getCreatedDate);
                        case "expireDate" -> Comparator.comparing(PackageDriverDTO::getExpireDate);
                        default -> null;
                    };
                return (c != null && order.isDescending()) ? c.reversed() : c;
            })
            .filter(Objects::nonNull)
            .reduce(Comparator::thenComparing)
            .orElse(null);

        if (comparator != null) {
            filtered = filtered.stream().sorted(comparator).toList();
        }

        // 📄 Paging
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<PackageDriverDTO> pageContent = (start <= end) ? filtered.subList(start, end) : List.of();

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    public PackageDriverDTO createPackage(PackageDriverDTO packageDriverDTO) {
        PackageDriver packageDriver = packageDriverMapper.toEntity(packageDriverDTO);

        Instant now = Instant.now();

        // Nếu expireDate được gửi từ FE
        if (packageDriver.getExpireDate() != null) {
            if (packageDriver.getExpireDate().isBefore(now)) {
                throw new BadRequestAlertException("Ngày hết hạn không được nằm trong quá khứ", "packageDriver", "expireDate-past");
            }
            if (packageDriver.getCreatedDate() != null && packageDriver.getExpireDate().isBefore(packageDriver.getCreatedDate())) {
                throw new BadRequestAlertException("Ngày hết hạn phải sau ngày tạo", "packageDriver", "expireDate-before-created");
            }
        }

        packageDriver.setPackageID(UUID.randomUUID());
        packageDriver.setStatus(PackageDriverStatus.ACTIVE);

        // Nếu FE không set createdDate → gán mặc định
        if (packageDriver.getCreatedDate() == null) {
            packageDriver.setCreatedDate(now);
        }

        packageDriver = packageDriverRepository.save(packageDriver);
        return packageDriverMapper.toDto(packageDriver);
    }

    public Optional<PackageDriver> togglePackageStatus(UUID packageId) {
        return packageDriverRepository
            .findByPackageID(packageId)
            .map(pkg -> {
                if (pkg.getStatus() == PackageDriverStatus.ACTIVE) {
                    pkg.setStatus(PackageDriverStatus.EXPIRED);
                } else if (pkg.getStatus() == PackageDriverStatus.EXPIRED) {
                    pkg.setStatus(PackageDriverStatus.ACTIVE);
                }
                return packageDriverRepository.save(pkg);
            });
    }

    private ConfirmingVehicleDriverDTO mapToConfirmingVehicleDriverDTO(User user, UserDetail userDetail, Driver driver, Vehicle vehicle) {
        ConfirmingVehicleDriverDTO dto = driverInfoMapper.toConfirmingVehicleDTO(user, userDetail, driver, vehicle);

        UUID driverId = driver.getDriverID();
        dto.setRating(getAverageRatingForDriver(driverId));
        dto.setAvatar(imageUrlService.buildUserAvatarUrl(userDetail.getAppUserDetail()));
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

    public Page<TripListDTO> getAllTrips(
        Pageable pageable,
        String startLocation,
        String endLocation,
        TripStatus status,
        UUID driverId,
        String tripHandleId
    ) {
        Long tripDbId = TripCodeUtils.decode(tripHandleId);

        Page<Trip> trips = tripRepository.findAllWithFilters(startLocation, endLocation, status, driverId, tripDbId, pageable);

        return trips.map(trip -> {
            TripListDTO dto = usermanageMapper.toTripListDTO(trip);
            if (trip.getId() != null) {
                dto.setTripHandleId(TripCodeUtils.encode(trip.getId()));
            }
            return dto;
        });
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
        if (trip.getId() != null) {
            dto.setTripHandleId(TripCodeUtils.encode(trip.getId()));
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

    public Page<FeedbackCusDTO> getAllFeedbacks(Pageable pageable, FeedbackStatus status, FeedbackType type, String tripCode) {
        Long tripId = TripCodeUtils.decode(tripCode); // TRIP-00042 → 42
        Page<Feedback> feedbackPage = feedbackRepository.findAllWithFilters(status, type, tripId, pageable);

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
            if (trip.getId() != null) {
                dto.setTripId(TripCodeUtils.encode(trip.getId()));
            }
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
                        driverDTO.setAvatar(imageUrlService.buildVehicleImageUrl(detail.getAppUserDetail()));
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

    @Transactional(readOnly = true)
    public Page<DriverPointHistoryListDTO> getPointHistoryListByUserDetail(UUID userDetailId, Pageable pageable) {
        Page<DriverPointHistory> page = driverPointHistoryRepository.findByDriver_DriverID(userDetailId, pageable);

        Instant now = Instant.now();
        return page.map(history -> {
            // 👉 Map các field cơ bản (không bao gồm userName)
            DriverPointHistoryListDTO dto = usermanageMapper.toListDtoBase(history);
            dto.setDisable(false);

            // 👉 Truy vấn userName theo userID từ userDetail (nếu có)
            if (history.getUserDetail() != null && history.getUserDetail().getUser().getId() != null) {
                userRepository
                    .findById(history.getUserDetail().getUser().getId())
                    .ifPresent(user -> {
                        dto.setUserName(user.getLogin());
                    });
                Instant expiredTime = history.getDate().plus(48, ChronoUnit.HOURS);
                if (now.isAfter(expiredTime)) {
                    dto.setDisable(true);
                }
            } else {
                dto.setUserName("Hệ thống");
            }

            return dto;
        });
    }

    @Transactional
    public DriverPointHistoryRefundDTO refundByHistory(UUID pointId) {
        DriverPointHistory original = driverPointHistoryRepository
            .findByPointId(pointId)
            .orElseThrow(() -> new BadRequestAlertException("History not found", "pointHistory", "notfound"));

        if (original.getStatus() != DriverPointHistoryStatus.DONE) {
            throw new BadRequestAlertException("Only DONE points can be refunded", "pointHistory", "invalidstatus");
        }

        Instant now = Instant.now();
        Instant expiredTime = original.getDate().plus(48, ChronoUnit.HOURS);
        if (now.isAfter(expiredTime)) {
            throw new BadRequestAlertException("Refund expired. Only allowed within 48 hours", "pointHistory", "expired");
        }

        Driver driver = original.getDriver();
        if (driver == null) {
            throw new BadRequestAlertException("Driver not found", "driver", "null");
        }

        // ✅ Đánh dấu bản ghi đã được refund
        original.setStatus(DriverPointHistoryStatus.REFUND);
        driver.addDriverPointHistory(original);

        // ✅ Tính tổng điểm bị trừ thực tế trong tháng
        int netPenalty = calculateMonthlyNetPenalty(driver);

        int banThreshold = 14;

        // ✅ Gỡ banned nếu đủ điều kiện
        if (
            driver.getDriverStatus() == DriverStatus.BANNED &&
            driver.getBannedDay() != null &&
            Instant.now().isBefore(driver.getBannedDay()) &&
            netPenalty < banThreshold
        ) {
            driver.setDriverStatus(DriverStatus.ACTIVE);
            driver.setBannedDay(null);
        }

        driverRepository.save(driver);
        return usermanageMapper.toDto(original);
    }

    private int calculateMonthlyNetPenalty(Driver driver) {
        YearMonth currentMonth = YearMonth.now();

        int totalPenalty = driver
            .getDriverPointHistories()
            .stream()
            .filter(h -> h.getStatus() == DriverPointHistoryStatus.DONE)
            .filter(h -> YearMonth.from(h.getDate()).equals(currentMonth))
            .mapToInt(DriverPointHistory::getPoint)
            .sum();

        int totalRefund = driver
            .getDriverPointHistories()
            .stream()
            .filter(h -> h.getStatus() == DriverPointHistoryStatus.REFUND)
            .filter(h -> YearMonth.from(h.getDate()).equals(currentMonth))
            .mapToInt(DriverPointHistory::getPoint)
            .sum();

        return totalPenalty - totalRefund;
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

    @Transactional(readOnly = true)
    public Page<WalletTransactionAdminDTO> getSystemTransactions(
        Pageable pageable,
        WalletTransactionType walletType,
        TransactionStatus walletStatus,
        Instant fromDate,
        Instant toDate
    ) {
        List<WalletTransaction> transactions = walletTransactionRepository.findAll();

        List<WalletTransaction> filtered = transactions
            .stream()
            .filter(tx -> walletType == null || tx.getWalletType() == walletType)
            .filter(tx -> walletStatus == null || tx.getTransStatus() == walletStatus)
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
        dto.setStatus(tx.getTransStatus());
        dto.setTransactionId(tx.getTransID());
        dto.setType(tx.getWalletType());
        dto.setAmount(tx.getAmount());
        dto.setCreatedDate(tx.getDate());
        dto.setFromOwner(resolveFrom(tx));
        dto.setToOwner(resolveTo(tx));
        dto.setDescription(buildDescription(tx));
        dto.setBefore(tx.getBefore());
        dto.setCurrent(tx.getCurrent());
        return dto;
    }

    private String resolveUserName(UserWallet wallet) {
        if (wallet == null || wallet.getUser() == null) return "Unknown User";
        return wallet.getUser().getFirstName() + " " + wallet.getUser().getLastName();
    }

    private String resolveFrom(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            case DEPOSIT -> "System";
            case SYSTEM_GAIN_PACKAGE_FEE -> {
                if (tx.getPayment() != null && tx.getPayment().getUser() != null) {
                    User user = tx.getPayment().getUser();
                    yield user.getFirstName() + " " + user.getLastName() + " (User)";
                }
                yield "Unknown";
            }
            case WITHDRAW,
                DRIVER_CREATE_TRIP_FEE,
                DRIVER_BUY_PACKAGE,
                PASSENGER_APPROVE_FEE,
                SYSTEM_GAIN_CREATE_TRIP_FEE,
                SYSTEM_GAIN_DONE_TRIP_FEE,
                SYSTEM_GAIN_PASSENGER_APPROVE_FEE -> resolveUserName(tx.getUserWallet());
            case DRIVER_DONE_TRIP_FEE -> "Passenger(s)";
            case SYSTEM_REFUND_TO_DRIVER_DONE_TRIP, SYSTEM_REFUND_TO_PASSENGER, DRIVER_DONE_TRIP_REFUND, REFUND -> "System";
            default -> "Unknown";
        };
    }

    private String resolveTo(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            case DEPOSIT, SYSTEM_REFUND_TO_DRIVER_DONE_TRIP, SYSTEM_REFUND_TO_PASSENGER, DRIVER_DONE_TRIP_REFUND, REFUND -> resolveUserName(
                tx.getUserWallet()
            );
            case WITHDRAW,
                DRIVER_CREATE_TRIP_FEE,
                DRIVER_BUY_PACKAGE,
                PASSENGER_APPROVE_FEE,
                SYSTEM_GAIN_CREATE_TRIP_FEE,
                SYSTEM_GAIN_PACKAGE_FEE,
                SYSTEM_GAIN_DONE_TRIP_FEE,
                SYSTEM_GAIN_PASSENGER_APPROVE_FEE -> "System";
            case DRIVER_DONE_TRIP_FEE -> "System";
            default -> "Unknown";
        };
    }

    private String buildDescription(WalletTransaction tx) {
        return switch (tx.getWalletType()) {
            // 🔹 Giao dịch liên quan đến chuyến đi
            case DRIVER_DONE_TRIP_FEE -> "Tổng tiền thu từ chuyến đi" + buildTripIdSuffix(tx);
            case DRIVER_CREATE_TRIP_FEE -> "Tài xế tạo chuyến đi, trừ phí" + buildTripIdSuffix(tx);
            case SYSTEM_GAIN_CREATE_TRIP_FEE -> "Hệ thống thu phí tạo chuyến" + buildTripIdSuffix(tx);
            case SYSTEM_GAIN_DONE_TRIP_FEE -> "Hệ thống thu từ chuyến đi hoàn thành" + buildTripIdSuffix(tx);
            case DRIVER_DONE_TRIP_REFUND -> "Hệ thống hoàn tiền chuyến đi cho tài xế" + buildTripIdSuffix(tx);
            case PASSENGER_APPROVE_FEE -> "Tiền cọc của hành khách" + buildTripIdSuffix(tx);
            case SYSTEM_REFUND_TO_DRIVER_DONE_TRIP -> "Hệ thống hoàn tiền cho tài xế" + buildTripIdSuffix(tx);
            case SYSTEM_REFUND_TO_PASSENGER -> "Hệ thống hoàn tiền cho hành khách" + buildTripIdSuffix(tx);
            // 🔹 Giao dịch liên quan đến gói tài xế
            case DRIVER_BUY_PACKAGE -> "Tài xế mua gói dịch vụ" + getPackageIdSuffix(tx);
            case SYSTEM_GAIN_PACKAGE_FEE -> "Hệ thống thu tiền từ tài xế mua gói" + getPackageIdSuffix(tx);
            // 🔹 Giao dịch người dùng
            case DEPOSIT -> "Người dùng nạp tiền vào ví";
            case WITHDRAW -> "Người dùng rút tiền từ ví";
            case REFUND -> "Hoàn tiền về ví người dùng";
            // 🔹 Giao dịch khác
            default -> "Giao dịch hệ thống khác";
        };
    }

    @Transactional(readOnly = true)
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

        // ✅ Xác định types cần lọc
        List<WalletTransactionType> typesToFilter;
        if (walletType != null) {
            if (incomeTypes.contains(walletType)) {
                typesToFilter = List.of(walletType);
            } else {
                throw new BadRequestAlertException(
                    "WalletType không hợp lệ cho thu nhập hệ thống",
                    "walletTransaction",
                    "invalid-income-type"
                );
            }
        } else {
            typesToFilter = incomeTypes;
        }

        // ✅ Gán mặc định from/to nếu thiếu
        Instant from = (fromDate != null) ? fromDate : Instant.EPOCH;
        Instant to = (toDate != null) ? toDate : Instant.now();

        // ✅ Truy vấn từ DB
        Page<WalletTransaction> txPage = walletTransactionRepository.findByWalletTypeInAndDateBetween(typesToFilter, from, to, pageable);

        // ✅ Map sang DTO
        return txPage.map(this::mapToDTO);
    }

    public double getAverageRatingForDriver(UUID driverId) {
        Double avg = ratingRepository.findAverageRatingByDriverId(driverId);
        return avg != null ? avg : 0.0;
    }

    private String buildTripIdSuffix(WalletTransaction tx) {
        String rawUuid = tx.getTransactionThirdPartyID();

        if (rawUuid == null || rawUuid.isBlank()) {
            return "";
        }

        try {
            UUID tripUUID = UUID.fromString(rawUuid);
            Optional<Trip> optionalTrip = tripRepository.findByTripID(tripUUID);

            if (optionalTrip.isPresent()) {
                Trip trip = optionalTrip.get();
                String shortCode = TripCodeUtils.encode(trip.getId());
                return " (Trip: " + shortCode + ")";
            } else {
                return " (Trip đã xóa - UUID: " + rawUuid + ")";
            }
        } catch (IllegalArgumentException e) {
            return " (Trip UUID không hợp lệ)";
        }
    }

    private String getPackageIdSuffix(WalletTransaction tx) {
        if (tx.getPayment() != null && tx.getPayment().getPackageDriver() != null) {
            return " (Gói: " + tx.getPayment().getPackageDriver().getName() + ")";
        }
        return "";
    }

    @Transactional(readOnly = true)
    public double getSystemWalletBalance() {
        return systemWalletRepository.findTopByOrderByMobifyDateDesc().map(SystemWallet::getCurrent).orElse(0.0);
    }

    @Transactional
    public void penalizeDriverByFeedback(UUID feedbackId, String reason) {
        Feedback feedback = feedbackRepository
            .findByFeedbackID(feedbackId)
            .orElseThrow(() -> new BadRequestAlertException("Feedback not found", "feedback", "notfound"));

        if (feedback.getFeedbackStatus() != FeedbackStatus.WAITING) {
            throw new BadRequestAlertException("Feedback has already been processed", "feedback", "already-processed");
        }

        Driver driver = feedback.getDriver();
        if (driver == null) {
            throw new BadRequestAlertException("Driver not found in feedback", "driver", "notfound");
        }

        int rating = feedback.getFeedbackRating();
        int minusPoint = (rating == 1) ? 2 : (rating == 2) ? 1 : 0;

        if (minusPoint <= 0) {
            throw new BadRequestAlertException("Không cần xử phạt với đánh giá này", "feedback", "no-penalty");
        }

        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "user", "notfound"));
        UserDetail currentUserDetail = userDetailRepository
            .findByUser(currentUser)
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "userDetail", "notfound"));

        // ✅ Ghi lịch sử phạt
        DriverPointHistory history = new DriverPointHistory();
        history.setPointId(UUID.randomUUID());
        history.setPoint(minusPoint);
        history.setReason(reason);
        history.setDate(Instant.now());
        history.setStatus(DriverPointHistoryStatus.DONE);
        history.setUserDetail(currentUserDetail);

        driver.addDriverPointHistory(history);

        // ✅ Nếu hết điểm thì chuyển trạng thái và ban 1 tháng
        if (driver.getDriverPoint() <= 0) {
            driver.setDriverStatus(DriverStatus.BANNED);
            driver.setBannedDay(Instant.now().plus(30, ChronoUnit.DAYS));
        }

        // ✅ Đánh dấu feedback đã xử lý
        feedback.setFeedbackStatus(FeedbackStatus.DONE);

        driverRepository.save(driver);
        feedbackRepository.save(feedback);
    }
}
