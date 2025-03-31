package strip.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Authority;
import strip.domain.Driver;
import strip.domain.DriverPackageSubscription;
import strip.domain.PackageDriver;
import strip.domain.SystemWallet;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.UserWallet;
import strip.domain.Vehicle;
import strip.domain.WalletTransaction;
import strip.domain.enumeration.DriverStatus;
import strip.domain.enumeration.PackageDriverStatus;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.VehicleStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.DriverRepository;
import strip.repository.PackageDriverRepository;
import strip.repository.SystemWalletRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletTransactionRepository;
import strip.security.SecurityUtils;
import strip.service.dto.ConfirmingDriverDTO;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.UpdateUserProfileDTO;
import strip.service.dto.UserDetailsCusDTO;
import strip.service.dto.UserProfileDTO;
import strip.service.dto.UserWalletWithTransactionsDTO;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional(readOnly = true)
public class UserMobileService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserWalletRepository userWalletRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final PackageDriverRepository packageDriverRepository;
    private final SystemWalletRepository systemWalletRepository;
    private final ImageUrlService imageUrlService;

    public UserMobileService(
        UserRepository userRepository,
        UserDetailRepository userDetailRepository,
        UserWalletRepository userWalletRepository,
        DriverRepository driverRepository,
        VehicleRepository vehicleRepository,
        UserService userService,
        WalletTransactionRepository walletTransactionRepository,
        PackageDriverRepository packageDriverRepository,
        SystemWalletRepository systemWalletRepository,
        ImageUrlService imageUrlService
    ) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.userWalletRepository = userWalletRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
        this.walletTransactionRepository = walletTransactionRepository;
        this.packageDriverRepository = packageDriverRepository;
        this.systemWalletRepository = systemWalletRepository;
        this.imageUrlService = imageUrlService;
    }

    public Optional<UserProfileDTO> getCurrentUserProfile() {
        Optional<User> optionalUser = userService.getUserWithAuthorities();
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        Optional<UserDetail> userDetailOpt = userDetailRepository.findByUser(user);
        Optional<UserWallet> userWalletOpt = userWalletRepository.findByUser(user);
        Optional<Driver> driverOpt = driverRepository.findByUser(user);

        Set<String> roles = extractRoles(user);
        boolean isDriver = driverOpt.map(d -> d.getDriverStatus() != DriverStatus.NOT_DRIVER).orElse(false);
        List<Vehicle> vehicles = getDriverVehicles(driverOpt);
        boolean hasVehicle = !vehicles.isEmpty();

        UserDetailsCusDTO userDetailsCusDTO = mapUserDetailToDTO(userDetailOpt);
        DriverInfoDTO driverDTO = mapToDriverInfoDTO(user, userDetailOpt, driverOpt);
        List<DriverVehicleDTO> vehicleDTOList = mapVehiclesToDTO(vehicles);

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUser(user);
        dto.setUserDetailsCusDTO(userDetailsCusDTO);
        dto.setUserWallet(userWalletOpt.orElse(null));
        dto.setDriver(driverDTO);
        dto.setDriverVehicleDTO(vehicleDTOList);
        dto.setDriver(isDriver);
        dto.setHasVehicle(hasVehicle);
        dto.setRoles(roles);

        return Optional.of(dto);
    }

    private Set<String> extractRoles(User user) {
        return user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

    private List<Vehicle> getDriverVehicles(Optional<Driver> driverOpt) {
        return driverOpt.map(driver -> vehicleRepository.findAllByDriver(driver)).orElse(Collections.emptyList());
    }

    private UserDetailsCusDTO mapUserDetailToDTO(Optional<UserDetail> userDetailOpt) {
        return userDetailOpt
            .map(userDetail -> {
                UserDetailsCusDTO dto = new UserDetailsCusDTO();
                dto.setAppUserDetail(userDetail.getAppUserDetail());
                dto.setPhone(userDetail.getPhone());
                dto.setGender(userDetail.getGender());
                dto.setAddress(userDetail.getAddress());
                dto.setDob(userDetail.getDob());
                dto.setImageUrl(imageUrlService.buildUserAvatarUrl(userDetail.getAppUserDetail()));
                return dto;
            })
            .orElse(null);
    }

    private DriverInfoDTO mapToDriverInfoDTO(User user, Optional<UserDetail> userDetailOpt, Optional<Driver> driverOpt) {
        if (driverOpt.isEmpty() || userDetailOpt.isEmpty()) return null;

        Driver driver = driverOpt.get();
        UserDetail userDetail = userDetailOpt.get();

        DriverInfoDTO dto = new DriverInfoDTO();
        dto.setUserId(user.getId());
        dto.setDriverId(driver.getDriverID());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(userDetail.getPhone());
        dto.setAddress(userDetail.getAddress());
        dto.setGender(userDetail.getGender());
        dto.setDob(userDetail.getDob());
        dto.setEmail(user.getEmail());

        UUID driverId = driver.getDriverID();
        dto.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverId));
        dto.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverId));
        dto.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverId));

        return dto;
    }

    private List<DriverVehicleDTO> mapVehiclesToDTO(List<Vehicle> vehicles) {
        return vehicles
            .stream()
            .map(vehicle -> {
                DriverVehicleDTO dto = new DriverVehicleDTO();
                UUID vehicleId = vehicle.getVehicleID();

                dto.setVehicleId(vehicleId);
                dto.setVehicleType(vehicle.getVehicleType().name());
                dto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicleId));
                dto.setCarRegistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicleId));
                dto.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicleId));
                dto.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicleId));
                dto.setVehicleNumber(vehicle.getVehicleNumber());
                dto.setNumberOfSeats(vehicle.getNumberOfSeats());
                dto.setVehicleColor(vehicle.getVehicleColor());
                dto.setVehicleBrand(vehicle.getVehicleBrand());
                dto.setStatus(vehicle.getStatus().name());

                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void updatePassengerProfile(UpdateUserProfileDTO dto) {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật thông tin cơ bản trong User
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        userRepository.save(user);

        // Cập nhật thông tin trong UserDetail
        UserDetail userDetail = userDetailRepository.findByUser(user).orElseThrow(() -> new RuntimeException("UserDetail not found"));

        userDetail.setPhone(dto.getPhone());
        userDetail.setAddress(dto.getAddress());
        userDetail.setDob(dto.getDob());
        userDetail.setGender(dto.getGender());

        if (dto.getUserImage() != null) {
            userDetail.setUserimage(dto.getUserImage());
            userDetail.setUserimageContentType(dto.getUserImageContentType());
        }

        userDetailRepository.save(userDetail);
    }

    @Transactional(readOnly = true)
    public UserWalletWithTransactionsDTO getWalletAndTransactions() {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User not found"));

        UserWallet wallet = userWalletRepository.findByUser(user).orElseThrow(() -> new RuntimeException("UserWallet not found"));

        List<WalletTransaction> transactions = walletTransactionRepository.findAllByUserWalletOrderByDateDesc(wallet);

        return new UserWalletWithTransactionsDTO(wallet, transactions);
    }

    @Transactional(readOnly = true)
    public List<PackageDriver> getAvailableDriverPackages() {
        return packageDriverRepository.findAllByStatus(PackageDriverStatus.ACTIVE);
    }

    @Transactional
    public void buyDriverPackage(UUID packageId) {
        User user = getCurrentUser();
        Driver driver = getValidatedDriver(user);
        PackageDriver pkg = getActivePackage(packageId);
        double price = pkg.getPrice();

        UserWallet userWallet = getValidUserWalletWithBalance(user, price);
        SystemWallet systemWallet = systemWalletRepository.findTopByOrderByMobifyDateDesc().orElseGet(this::createInitialSystemWallet);

        createAndAssignSubscription(driver, pkg, price);
        updateDriverStatusAndExpiration(driver, pkg);
        handleWalletTransactions(userWallet, systemWallet, price);
    }

    private User getCurrentUser() {
        return userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Driver getValidatedDriver(User user) {
        Driver driver = driverRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Driver not found"));
        if (!driver.getUsedtoDriver()) {
            throw new RuntimeException("Bạn phải xác minh tài xế trước khi mua gói.");
        }
        return driver;
    }

    private PackageDriver getActivePackage(UUID packageId) {
        return packageDriverRepository
            .findByPackageID(packageId)
            .filter(p -> p.getStatus() == PackageDriverStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Gói tài xế không tồn tại hoặc đã ngừng hoạt động."));
    }

    private UserWallet getValidUserWalletWithBalance(User user, double requiredAmount) {
        UserWallet wallet = userWalletRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Không tìm thấy ví người dùng."));
        if (wallet.getCurrent() == null || wallet.getCurrent() < requiredAmount) {
            throw new RuntimeException("Số dư không đủ để mua gói.");
        }
        return wallet;
    }

    private void createAndAssignSubscription(Driver driver, PackageDriver pkg, double price) {
        DriverPackageSubscription subscription = new DriverPackageSubscription();
        subscription.setPackageDriver(pkg);

        Instant now = Instant.now();
        ZonedDateTime baseTimeUtc = getBaseTimeForNewPackage(driver);
        Instant expiration = baseTimeUtc.plusMonths(pkg.getTime()).toInstant();

        subscription.setPurchaseDate(now);
        subscription.setExpirationDate(expiration);
        subscription.setPackagePrice(price);
        subscription.setActive(true);

        driver.addDriverPackageSubscription(subscription);
        driverRepository.save(driver);
    }

    private void handleWalletTransactions(UserWallet userWallet, SystemWallet systemWallet, double price) {
        Instant now = Instant.now();

        // User transaction
        WalletTransaction userTx = new WalletTransaction();
        userTx.setTransID(UUID.randomUUID());
        userTx.setAmount(price);
        userTx.setDate(now);
        userTx.setWalletType(WalletTransactionType.DRIVER_BUY_PACKAGE);
        userTx.setTransStatus(TransactionStatus.SUCCESS);
        userTx.setTransactionThirdPartyID(null);
        userTx.setUserWallet(userWallet);
        userWallet.addWalletTransactionAndUpdateBalance(userTx);
        userWalletRepository.save(userWallet);

        // System transaction
        WalletTransaction sysTx = new WalletTransaction();
        sysTx.setTransID(UUID.randomUUID());
        sysTx.setAmount(price);
        sysTx.setDate(now);
        sysTx.setWalletType(WalletTransactionType.SYSTEM_GAIN_PACKAGE_FEE);
        sysTx.setTransStatus(TransactionStatus.SUCCESS);
        sysTx.setTransactionThirdPartyID(null);
        sysTx.setSystemWallet(systemWallet);
        systemWallet.addWalletTransaction(sysTx);
        systemWalletRepository.save(systemWallet);
    }

    private SystemWallet createInitialSystemWallet() {
        SystemWallet sw = new SystemWallet();
        sw.setSystemWalletID(UUID.randomUUID());
        sw.setBefore(0.0);
        sw.setAmount(0.0);
        sw.setCurrent(0.0);
        sw.setBlockAmount(0.0);
        sw.setMobifyDate(Instant.now());
        return systemWalletRepository.save(sw);
    }

    private void updateDriverStatusAndExpiration(Driver driver, PackageDriver pkg) {
        if (driver.getDriverStatus() == DriverStatus.NOT_DRIVER) {
            driver.setDriverStatus(DriverStatus.ACTIVE);
        }

        ZonedDateTime base = getBaseTimeForNewPackage(driver);
        Instant newExpiration = base.plusMonths(pkg.getTime()).toInstant();

        driver.setExpirationDate(newExpiration);
        driverRepository.save(driver);
    }

    private ZonedDateTime getBaseTimeForNewPackage(Driver driver) {
        Instant now = Instant.now();
        Instant exp = driver.getExpirationDate();

        return (exp == null || exp.isBefore(now)) ? now.atZone(ZoneId.of("UTC")) : exp.atZone(ZoneId.of("UTC"));
    }

    //////////////////////////////// CONFIRMING DRIVER ///////////////////////////
    @Transactional
    public ConfirmingVehicleDriverDTO confirmDriver(ConfirmingDriverDTO dto) {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy người dùng"));

        validateUserCanSubmitDriverApplication(user);

        UserDetail userDetail = userDetailRepository
            .findByUser(user)
            .orElseGet(() -> {
                UserDetail ud = new UserDetail();
                ud.setAppUserDetail(UUID.randomUUID());
                ud.setUser(user);
                return ud;
            });
        // UserDetail userDetail = saveOrUpdateUserDetail(user, dto);
        Driver driver = saveOrUpdateDriver(user, dto);
        Vehicle vehicle = saveOrUpdateVehicle(driver, dto);

        return buildConfirmingVehicleDriverDTO(user, userDetail, driver, vehicle);
    }

    private Driver saveOrUpdateDriver(User user, ConfirmingDriverDTO dto) {
        Driver driver = driverRepository
            .findByUser(user)
            .orElseGet(() -> {
                Driver d = new Driver();
                d.setDriverID(UUID.randomUUID());
                d.setUser(user);
                d.setDriverPoint(0);
                return d;
            });

        driver.setDriverLicense(dto.getDriverLicense());
        driver.setDriverLicenseContentType(dto.getDriverLicenseContentType());
        driver.setIdentityCardFaceUp(dto.getIdentityCardFaceUp());
        driver.setIdentityCardFaceUpContentType(dto.getIdentityCardFaceUpContentType());
        driver.setIdentityCardFacedown(dto.getIdentityCardFacedown());
        driver.setIdentityCardFacedownContentType(dto.getIdentityCardFacedownContentType());
        driver.setDriverStatus(DriverStatus.CONFIRMING);
        driver.setUsedtoDriver(false);

        return driverRepository.save(driver);
    }

    private Vehicle saveOrUpdateVehicle(Driver driver, ConfirmingDriverDTO dto) {
        Vehicle vehicle = vehicleRepository
            .findAllByDriver(driver)
            .stream()
            .findFirst()
            .orElseGet(() -> {
                Vehicle v = new Vehicle();
                v.setVehicleID(UUID.randomUUID());
                v.setDriver(driver);
                return v;
            });

        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setNumberOfSeats(dto.getNumberOfSeats());
        vehicle.setVehicleColor(dto.getVehicleColor());
        vehicle.setVehicleBrand(dto.getVehicleBrand());
        vehicle.setStatus(VehicleStatus.CONFIRMING);

        vehicle.setVehicleImage(dto.getVehicleImage());
        vehicle.setVehicleImageContentType(dto.getVehicleImageContentType());

        vehicle.setCarregistration(dto.getCarRegistration());
        vehicle.setCarregistrationContentType(dto.getCarRegistrationContentType());

        vehicle.setVehicleInspectionCertificate(dto.getVehicleInspectionCertificate());
        vehicle.setVehicleInspectionCertificateContentType(dto.getVehicleInspectionCertificateContentType());

        vehicle.setCarInsurance(dto.getCarInsurance());
        vehicle.setCarInsuranceContentType(dto.getCarInsuranceContentType());

        return vehicleRepository.save(vehicle);
    }

    private ConfirmingVehicleDriverDTO buildConfirmingVehicleDriverDTO(User user, UserDetail detail, Driver driver, Vehicle vehicle) {
        ConfirmingVehicleDriverDTO res = new ConfirmingVehicleDriverDTO();
        res.setUserId(user.getId());
        res.setDriverId(driver.getDriverID());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPhone(detail.getPhone());
        res.setEmail(user.getEmail());

        res.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driver.getDriverID()));
        res.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driver.getDriverID()));
        res.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driver.getDriverID()));

        ConfirmingVehicleDTO vDTO = new ConfirmingVehicleDTO();
        vDTO.setId(vehicle.getId());
        vDTO.setVehicleID(vehicle.getVehicleID());
        vDTO.setVehicleType(vehicle.getVehicleType());
        vDTO.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicle.getVehicleID()));
        vDTO.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicle.getVehicleID()));
        vDTO.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicle.getVehicleID()));
        vDTO.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicle.getVehicleID()));
        vDTO.setVehicleNumber(vehicle.getVehicleNumber());
        vDTO.setNumberOfSeats(vehicle.getNumberOfSeats());
        vDTO.setVehicleColor(vehicle.getVehicleColor());
        vDTO.setVehicleBrand(vehicle.getVehicleBrand());
        vDTO.setStatus(vehicle.getStatus());

        res.setVehicle(vDTO);
        return res;
    }

    /////////////////////////////////////// GET CONFIRMING DRIVER ///////////////
    @Transactional(readOnly = true)
    public Optional<ConfirmingVehicleDriverDTO> getDriverApplication() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .map(user -> {
                Optional<Driver> driverOpt = driverRepository.findByUser(user);
                Optional<UserDetail> detailOpt = userDetailRepository.findByUser(user);
                Optional<Vehicle> vehicleOpt = driverOpt.map(
                    driver -> vehicleRepository.findAllByDriver(driver).stream().findFirst().orElse(null)
                );

                ConfirmingVehicleDriverDTO res = new ConfirmingVehicleDriverDTO();
                res.setUserId(user.getId());
                res.setFirstName(user.getFirstName());
                res.setLastName(user.getLastName());
                res.setEmail(user.getEmail());

                // 🧾 UserDetail
                detailOpt.ifPresent(detail -> {
                    res.setPhone(detail.getPhone());
                });

                // 🧾 Driver
                driverOpt.ifPresent(driver -> {
                    res.setDriverId(driver.getDriverID());
                    res.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driver.getDriverID()));
                    res.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driver.getDriverID()));
                    res.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driver.getDriverID()));
                });

                // 🧾 Vehicle
                vehicleOpt.ifPresent(vehicle -> {
                    ConfirmingVehicleDTO vDTO = new ConfirmingVehicleDTO();
                    vDTO.setId(vehicle.getId());
                    vDTO.setVehicleID(vehicle.getVehicleID());
                    vDTO.setVehicleType(vehicle.getVehicleType());
                    vDTO.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicle.getVehicleID()));
                    vDTO.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicle.getVehicleID()));
                    vDTO.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicle.getVehicleID()));
                    vDTO.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicle.getVehicleID()));
                    vDTO.setVehicleNumber(vehicle.getVehicleNumber());
                    vDTO.setNumberOfSeats(vehicle.getNumberOfSeats());
                    vDTO.setVehicleColor(vehicle.getVehicleColor());
                    vDTO.setVehicleBrand(vehicle.getVehicleBrand());
                    vDTO.setStatus(vehicle.getStatus());

                    res.setVehicle(vDTO);
                });

                return res;
            });
    }

    private void validateUserCanSubmitDriverApplication(User user) {
        Driver existingDriver = driverRepository.findByUser(user).orElse(null);

        if (existingDriver != null) {
            DriverStatus status = existingDriver.getDriverStatus();
            boolean used = Boolean.TRUE.equals(existingDriver.getUsedtoDriver());

            // ❌ Chặn nếu đã từng được duyệt rồi
            if (used && status != DriverStatus.NOT_DRIVER) {
                throw new BadRequestAlertException("Bạn đã từng là tài xế trước đó. Không thể gửi lại đơn.", "driver", "usedToDriver");
            }

            // ❌ Chặn nếu đơn đang chờ duyệt
            if (status == DriverStatus.CONFIRMING) {
                throw new BadRequestAlertException("Đơn đăng ký đang chờ xét duyệt.", "driver", "alreadySubmitted");
            }

            // ❌ Chặn nếu bị từ chối
            if (status == DriverStatus.BANNED) {
                throw new BadRequestAlertException("Tài khoản đã bị từ chối, vui lòng liên hệ quản trị viên.", "driver", "banned");
            }
            // ✅ Nếu status = NOT_DRIVER và usedToDriver = false thì vẫn cho nộp đơn
        }
        // ✅ Nếu chưa có Driver record → cho nộp đơn
    }
}
