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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Authority;
import strip.domain.Driver;
import strip.domain.DriverPackageSubscription;
import strip.domain.PackageDriver;
import strip.domain.Payment;
import strip.domain.RequestTrip;
import strip.domain.SystemWallet;
import strip.domain.Trip;
import strip.domain.TripStopLocation;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.UserWallet;
import strip.domain.Vehicle;
import strip.domain.WalletDeposit;
import strip.domain.WalletTransaction;
import strip.domain.enumeration.DriverStatus;
import strip.domain.enumeration.PackageDriverStatus;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.PaymentStatus;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.TripStatus;
import strip.domain.enumeration.VehicleStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.AuthorityRepository;
import strip.repository.DriverRepository;
import strip.repository.PackageDriverRepository;
import strip.repository.PaymentRepository;
import strip.repository.RequestTripRepository;
import strip.repository.SystemWalletRepository;
import strip.repository.TripRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletDepositRepository;
import strip.repository.WalletTransactionRepository;
import strip.security.AuthoritiesConstants;
import strip.security.SecurityUtils;
import strip.service.dto.ConfirmingDriverDTO;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.JoinTripRequestDTO;
import strip.service.dto.RequestTripDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.TripStopLocationDTO;
import strip.service.dto.UpdateUserProfileDTO;
import strip.service.dto.UserDetailsCusDTO;
import strip.service.dto.UserProfileDTO;
import strip.service.dto.UserWalletWithTransactionsDTO;
import strip.service.dto.WithdrawRequestDTO;
import strip.service.mapper.RequestTripMapper;
import strip.service.mapper.TripCusMapper;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional(readOnly = true)
public class UserMobileService {

    private static final Logger LOG = LoggerFactory.getLogger(UserMobileService.class);
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
    private final WalletDepositRepository walletDepositRepository;
    private final AuthorityRepository authorityRepository;
    private final TripCusMapper tripCusMapper;
    private final RequestTripRepository requestTripRepository;
    private final TripRepository tripRepository;
    private final RequestTripMapper requestTripMapper;
    private final PaymentRepository paymentRepository;

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
        ImageUrlService imageUrlService,
        WalletDepositRepository walletDepositRepository,
        AuthorityRepository authorityRepository,
        TripCusMapper tripCusMapper,
        RequestTripRepository requestTripRepository,
        TripRepository tripRepository,
        RequestTripMapper requestTripMapper,
        PaymentRepository paymentRepository
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
        this.walletDepositRepository = walletDepositRepository;
        this.authorityRepository = authorityRepository;
        this.tripCusMapper = tripCusMapper;
        this.requestTripRepository = requestTripRepository;
        this.tripRepository = tripRepository;
        this.requestTripMapper = requestTripMapper;
        this.paymentRepository = paymentRepository;
    }

    public Optional<UserProfileDTO> getCurrentUserProfile() {
        return userService
            .getUserWithAuthorities()
            .map(user -> {
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

                return dto;
            });
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
        if (driverOpt.isEmpty() || userDetailOpt.isEmpty()) {
            return null;
        }

        Driver driver = driverOpt.orElseThrow(); // hoặc giữ nguyên vì đã kiểm tra isEmpty ở trên
        UserDetail userDetail = userDetailOpt.orElseThrow();

        DriverInfoDTO dto = new DriverInfoDTO();
        dto.setAvatar(imageUrlService.buildUserAvatarUrl(userDetail.getAppUserDetail()));
        dto.setUserId(userDetail.getAppUserDetail());
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

        Payment payment = createPaymentForDriverPackage(user, pkg, price);
        createAndAssignSubscription(driver, pkg, price);
        updateDriverStatusAndExpiration(driver, pkg);
        handleWalletTransactions(userWallet, systemWallet, price, pkg, payment);
        addDriverRoleIfMissing(user);
    }

    private Payment createPaymentForDriverPackage(User user, PackageDriver pkg, double price) {
        Payment payment = new Payment();
        payment.setPaymentID(UUID.randomUUID());
        payment.setAmount(price);
        payment.setPaymentDate(Instant.now());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setUser(user);
        payment.setPackageDriver(pkg);

        return paymentRepository.save(payment);
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

    private void addDriverRoleIfMissing(User user) {
        Authority driverRole = authorityRepository
            .findById(AuthoritiesConstants.DRIVER)
            .orElseGet(() -> {
                Authority newAuth = new Authority();
                newAuth.setName(AuthoritiesConstants.DRIVER);
                return authorityRepository.save(newAuth);
            });

        if (!user.getAuthorities().contains(driverRole)) {
            user.getAuthorities().add(driverRole);
            userRepository.save(user);
        }
    }

    private void handleWalletTransactions(
        UserWallet userWallet,
        SystemWallet systemWallet,
        double price,
        PackageDriver pkg,
        Payment payment
    ) {
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
        userTx.setPayment(payment);
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
        sysTx.setPayment(payment);
        sysTx.setUserWallet(userWallet);
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
        res.setUserId(detail.getAppUserDetail());
        res.setDriverId(driver.getDriverID());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPhone(detail.getPhone());
        res.setEmail(user.getEmail());
        res.setAvatar(imageUrlService.buildUserAvatarUrl(detail.getAppUserDetail()));
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

                res.setFirstName(user.getFirstName());
                res.setLastName(user.getLastName());
                res.setEmail(user.getEmail());

                // 🧾 UserDetail
                detailOpt.ifPresent(detail -> {
                    res.setPhone(detail.getPhone());
                    res.setUserId(detail.getAppUserDetail());
                    res.setAvatar(imageUrlService.buildUserAvatarUrl(detail.getAppUserDetail()));
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

    @Transactional
    public void createWithdrawRequest(WithdrawRequestDTO dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BadRequestAlertException("Số tiền rút không hợp lệ", "wallet", "invalidAmount");
        }

        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        UserWallet wallet = userWalletRepository
            .findByUser(user)
            .orElseThrow(() -> new BadRequestAlertException("Người dùng chưa có ví", "wallet", "notfound"));

        if (wallet.getCurrent() == null || wallet.getCurrent() < dto.getAmount()) {
            throw new BadRequestAlertException("Số dư không đủ", "wallet", "insufficientBalance");
        }

        // ✅ Tạo WalletDeposit
        WalletDeposit deposit = new WalletDeposit();
        deposit.setId(UUID.randomUUID());
        deposit.setAmount(dto.getAmount());
        deposit.setDate(Instant.now());
        deposit.setStatus(PaymentStatus.PENDING);
        deposit.setBank(dto.getBankName());
        deposit.setBankNumber(dto.getBankNumber());
        deposit.setNameOfBank(dto.getNameOfBank());
        deposit.setUserWallet(wallet);

        walletDepositRepository.save(deposit);

        // ✅ Tạo WalletTransaction tương ứng
        WalletTransaction trans = new WalletTransaction();
        trans.setTransID(UUID.randomUUID());
        trans.setAmount(dto.getAmount());
        trans.setDate(Instant.now());
        trans.setWalletType(WalletTransactionType.WITHDRAW);
        trans.setTransStatus(TransactionStatus.SUCCESS);
        trans.setUserWallet(wallet);

        wallet.addWalletTransactionAndUpdateBalance(trans);
        userWalletRepository.save(wallet); // cascade transaction
    }

    public List<TripCusDTO> getTripHistoryForPassenger() {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "user", "notfound"));

        List<RequestTrip> requests = requestTripRepository.findByUserAndStatus(user, PassengerStatus.DONE);

        LOG.debug("Total requestTrips for DONE: {}", requests);

        List<Trip> trips = requests
            .stream()
            .map(RequestTrip::getTrip)
            .filter(trip -> trip.getTripStatus() == TripStatus.DONE)
            .distinct()
            .collect(Collectors.toList());

        return trips
            .stream()
            .map(trip -> {
                Driver driver = trip.getDriver();
                Vehicle vehicle = trip.getVehicle();
                TripCusDTO dto = tripCusMapper.toDto(trip, driver, vehicle);

                // Bổ sung field phức tạp
                dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));
                dto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicle.getVehicleID()));
                if (driver != null && driver.getUser() != null) {
                    User driverUser = driver.getUser();
                    dto.setDriverName(driverUser.getFirstName() + " " + driverUser.getLastName());

                    String phone = userDetailRepository.findByUserId(driverUser.getId()).map(UserDetail::getPhone).orElse(null);
                    dto.setDriverPhone(phone);
                } else {
                    dto.setDriverName("Chưa xác định");
                    dto.setDriverPhone(null);
                }

                dto.setStopLocations(
                    trip
                        .getTripStopLocations()
                        .stream()
                        .map(
                            loc ->
                                new TripStopLocationDTO(
                                    loc.getStopLocaID(),
                                    loc.getStopLoca(),
                                    loc.getStoplocaPosition(),
                                    loc.getEstimatedTime(),
                                    loc.getEstimatedKM(),
                                    loc.getStopLocaTime(),
                                    loc.getStopLocaStatus()
                                )
                        )
                        .collect(Collectors.toList())
                );

                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<TripCusDTO> getTripHistoryForPassengerBooking() {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "user", "notfound"));

        List<RequestTrip> requests = requestTripRepository.findByUserAndStatus(user, PassengerStatus.BOOKED);

        LOG.debug("Total requestTrips for DONE: {}", requests);

        List<Trip> trips = requests
            .stream()
            .map(RequestTrip::getTrip)
            .filter(trip -> trip.getTripStatus() == TripStatus.UPCOMING || trip.getTripStatus() == TripStatus.ON_GOING)
            .distinct()
            .collect(Collectors.toList());

        return trips
            .stream()
            .map(trip -> {
                Driver driver = trip.getDriver();
                Vehicle vehicle = trip.getVehicle();
                TripCusDTO dto = tripCusMapper.toDto(trip, driver, vehicle);

                // Bổ sung field phức tạp
                dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));
                dto.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicle.getVehicleID()));
                if (driver != null && driver.getUser() != null) {
                    User driverUser = driver.getUser();
                    dto.setDriverName(driverUser.getFirstName() + " " + driverUser.getLastName());

                    String phone = userDetailRepository.findByUserId(driverUser.getId()).map(UserDetail::getPhone).orElse(null);
                    dto.setDriverPhone(phone);
                } else {
                    dto.setDriverName("Chưa xác định");
                    dto.setDriverPhone(null);
                }

                dto.setStopLocations(
                    trip
                        .getTripStopLocations()
                        .stream()
                        .map(
                            loc ->
                                new TripStopLocationDTO(
                                    loc.getStopLocaID(),
                                    loc.getStopLoca(),
                                    loc.getStoplocaPosition(),
                                    loc.getEstimatedTime(),
                                    loc.getEstimatedKM(),
                                    loc.getStopLocaTime(),
                                    loc.getStopLocaStatus()
                                )
                        )
                        .collect(Collectors.toList())
                );

                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void cancelRequestTrip(UUID requestTripId) {
        RequestTrip request = getValidCancelableRequest(requestTripId);
        User currentUser = getCurrentUser();

        validateRequestOwner(request, currentUser);

        // ✅ Cập nhật trạng thái hủy
        request.setStatus(PassengerStatus.CANCEL);
        requestTripRepository.save(request);

        // ✅ Giảm số ghế đang đặt
        updateTripSeatAfterCancel(request);

        // ✅ Thực hiện hoàn tiền nếu có
        handleRefundIfNeeded(request, currentUser);
    }

    private RequestTrip getValidCancelableRequest(UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy requestTrip", "trip", "notfound"));

        if (request.getStatus() == PassengerStatus.DONE || request.getStatus() == PassengerStatus.CANCEL) {
            throw new BadRequestAlertException("Yêu cầu đã hoàn tất hoặc đã huỷ", "trip", "already-final");
        }

        return request;
    }

    private void validateRequestOwner(RequestTrip request, User currentUser) {
        if (!request.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestAlertException("Không thể huỷ yêu cầu của người khác", "trip", "forbidden");
        }
    }

    private void updateTripSeatAfterCancel(RequestTrip request) {
        Trip trip = request.getTrip();
        int current = trip.getCurrentSeat() != null ? trip.getCurrentSeat() : 0;
        trip.setCurrentSeat(Math.max(current - request.getNumberofSeats(), 0));
        tripRepository.save(trip);
    }

    private void handleRefundIfNeeded(RequestTrip request, User currentUser) {
        Double amount = request.getAmountApproveFee();
        if (amount == null || amount <= 0) return;

        Optional<UserWallet> userWalletOpt = userWalletRepository.findByUser(currentUser);
        Optional<UserDetail> detailOpt = userDetailRepository.findByUserId(currentUser.getId());

        if (userWalletOpt.isEmpty() || detailOpt.isEmpty()) return;

        UserWallet userWallet = userWalletOpt.get();
        UserDetail detail = detailOpt.get();
        String txKey = request.getTrip().getTripID() + "-" + detail.getAppUserDetail();

        Optional<WalletTransaction> approveTxOpt = walletTransactionRepository.findByTransactionThirdPartyIDAndWalletTypeAndTransStatus(
            txKey,
            WalletTransactionType.PASSENGER_APPROVE_FEE,
            TransactionStatus.SUCCESS
        );

        if (approveTxOpt.isEmpty()) return;

        // ✅ Hoàn tiền cho Passenger
        WalletTransaction userTx = new WalletTransaction();
        userTx.setTransID(UUID.randomUUID());
        userTx.setAmount(amount);
        userTx.setDate(Instant.now());
        userTx.setWalletType(WalletTransactionType.REFUND);
        userTx.setTransStatus(TransactionStatus.SUCCESS);
        userTx.setUserWallet(userWallet);
        userWallet.addWalletTransactionAndUpdateBalance(userTx);
        userWalletRepository.save(userWallet);

        // ✅ Trừ tiền hệ thống
        SystemWallet systemWallet = systemWalletRepository
            .findTopByOrderByMobifyDateDesc()
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy ví hệ thống", "wallet", "system-notfound"));

        WalletTransaction sysTx = new WalletTransaction();
        sysTx.setTransID(UUID.randomUUID());
        sysTx.setAmount(amount);
        sysTx.setDate(Instant.now());
        sysTx.setWalletType(WalletTransactionType.SYSTEM_REFUND_TO_PASSENGER);
        sysTx.setTransStatus(TransactionStatus.SUCCESS);
        systemWallet.addWalletTransactionAndUpdateBalance(sysTx);
        systemWalletRepository.save(systemWallet);
    }

    @Transactional
    public RequestTripDTO joinTrip(JoinTripRequestDTO dto) {
        Trip trip = getAndValidateTrip(dto.getTripId());
        User user = getCurrentUser();
        validateJoinConditions(trip, user, dto.getNumberOfSeats());
        dto.setAmountApproveFee(trip.getPricePerSeat() * dto.getNumberOfSeats());

        // ✅ FE gửi lên luôn số tiền cần thanh toán
        // double totalFee = dto.getAmountApproveFee() != null ? dto.getAmountApproveFee() : 0.0;
        double totalFee = dto.getAmountApproveFee();
        if (totalFee <= 0) {
            throw new BadRequestAlertException("Amount must be greater than 0", "requestTrip", "invalidAmount");
        }

        RequestTrip request = buildRequestTrip(dto, trip, user, totalFee);

        if (dto.isPayNow()) {
            handlePrepaidPayment(user, totalFee, trip.getTripID());
        } else {
            createPendingTransaction(user, totalFee, trip.getTripID());
        }
        if (!isValidStopLocation(trip, dto.getStartLoca(), dto.getEndLoca())) {
            throw new BadRequestAlertException("Điểm lên/xuống không hợp lệ", "requestTrip", "invalidStop");
        }

        request.getTrip().setCurrentSeat(trip.getCurrentSeat() + request.getNumberofSeats());
        RequestTrip saved = requestTripRepository.save(request);
        return requestTripMapper.toDto(saved);
    }

    private Trip getAndValidateTrip(UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Trip not found", "trip", "notfound"));

        if (!trip.getTripStatus().equals(TripStatus.UPCOMING)) {
            throw new BadRequestAlertException("Trip is not open for joining", "trip", "invalidstatus");
        }

        return trip;
    }

    private RequestTrip buildRequestTrip(JoinTripRequestDTO dto, Trip trip, User user, double totalFee) {
        RequestTrip request = new RequestTrip();
        request.setRequestTripID(UUID.randomUUID());
        request.setTrip(trip);
        request.setUser(user);
        request.setCheckIn(false);
        request.setCheckOut(false);
        request.setStartLoca(dto.getStartLoca().toString());
        request.setEndLoca(dto.getEndLoca().toString());
        request.setNumberofSeats(dto.getNumberOfSeats());
        request.setType(dto.getType());
        request.setLuggageDescription(dto.getLuggageDescription());
        request.setPickUpTime(dto.getPickUpTime());
        request.setStatus(PassengerStatus.WAITING);
        request.setAmountApproveFee(totalFee);
        request.setAppliedAt(Instant.now());

        if (dto.getLuggageImg() != null) {
            request.setLuggageImg(dto.getLuggageImg());
        }

        return request;
    }

    private void handlePrepaidPayment(User user, double amount, UUID tripId) {
        UserWallet wallet = userWalletRepository
            .findByUser_Id(user.getId())
            .orElseThrow(() -> new BadRequestAlertException("User wallet not found", "wallet", "notfound"));

        WalletTransaction passengerTx = new WalletTransaction();
        passengerTx.setTransID(UUID.randomUUID());
        passengerTx.setAmount(amount);
        passengerTx.setDate(Instant.now());
        passengerTx.setWalletType(WalletTransactionType.PASSENGER_APPROVE_FEE);
        passengerTx.setTransactionThirdPartyID(tripId.toString());
        passengerTx.setTransStatus(TransactionStatus.SUCCESS);

        wallet.addWalletTransactionAndUpdateBalance(passengerTx);
        walletTransactionRepository.save(passengerTx);
        userWalletRepository.save(wallet);

        createSystemTransaction(amount, WalletTransactionType.SYSTEM_GAIN_PASSENGER_APPROVE_FEE, wallet);
    }

    private void createPendingTransaction(User user, double amount, UUID tripId) {
        UserWallet wallet = userWalletRepository
            .findByUser_Id(user.getId())
            .orElseThrow(() -> new BadRequestAlertException("User wallet not found", "wallet", "notfound"));

        WalletTransaction pendingTx = new WalletTransaction();
        pendingTx.setTransID(UUID.randomUUID());
        pendingTx.setAmount(amount);
        pendingTx.setDate(Instant.now());
        pendingTx.setWalletType(WalletTransactionType.PASSENGER_APPROVE_FEE);
        pendingTx.setTransStatus(TransactionStatus.PENDING);
        pendingTx.setUserWallet(wallet);
        pendingTx.setTransactionThirdPartyID(tripId.toString());
        wallet.addWalletTransactionAndUpdateBalance(pendingTx);
        userWalletRepository.save(wallet);
    }

    private void createSystemTransaction(double amount, WalletTransactionType type, UserWallet wallet) {
        SystemWallet systemWallet = systemWalletRepository
            .findTopByOrderByMobifyDateDesc()
            .orElseThrow(() -> new BadRequestAlertException("System wallet not found", "systemWallet", "notfound"));

        WalletTransaction tx = new WalletTransaction();
        tx.setTransID(UUID.randomUUID());
        tx.setSystemWallet(systemWallet);
        tx.setAmount(amount);
        tx.setDate(Instant.now());
        tx.setWalletType(type);
        tx.setUserWallet(wallet);
        tx.setTransStatus(TransactionStatus.SUCCESS);

        systemWallet.setBefore(systemWallet.getCurrent());
        systemWallet.setCurrent(systemWallet.getCurrent() + amount);
        systemWallet.setAmount(amount);
        systemWallet.setMobifyDate(Instant.now());
        systemWallet.addWalletTransactionAndUpdateBalance(tx);
        systemWalletRepository.save(systemWallet);
    }

    private void validateJoinConditions(Trip trip, User user, int numberOfSeats) {
        // 1. Kiểm tra số chỗ còn lại
        int seatsAvailable = trip.getMaxSeat() - trip.getCurrentSeat();
        if (numberOfSeats > seatsAvailable) {
            throw new BadRequestAlertException("Số ghế còn lại không đủ", "trip", "seatsInsufficient");
        }

        // 2. Kiểm tra user đã tham gia chuyến này chưa
        boolean alreadyJoined = requestTripRepository.existsByTripAndUser(trip, user);
        if (alreadyJoined) {
            throw new BadRequestAlertException("Bạn đã đăng ký tham gia chuyến đi này rồi", "trip", "alreadyJoined");
        }
    }

    private boolean isValidStopLocation(Trip trip, UUID startLocaId, UUID endLocaId) {
        if (startLocaId == null || endLocaId == null || startLocaId.equals(endLocaId)) {
            return false;
        }

        Set<TripStopLocation> stops = trip.getTripStopLocations();
        LOG.debug("list trip stoplocatoin {}", stops);
        if (stops == null || stops.isEmpty()) {
            return false;
        }

        Optional<TripStopLocation> start = stops.stream().filter(stop -> stop.getStopLocaID().equals(startLocaId)).findFirst();

        Optional<TripStopLocation> end = stops.stream().filter(stop -> stop.getStopLocaID().equals(endLocaId)).findFirst();

        return start.isPresent() && end.isPresent() && start.get().getStoplocaPosition() < end.get().getStoplocaPosition();
    }

    public Page<TripListDTO> getTripHistoryForDriver(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Không xác định được người dùng", "auth", "notfound"));

        List<TripStatus> historyStatuses = List.of(TripStatus.DONE, TripStatus.CANCEL);
        Page<Trip> trips = tripRepository.findByDriver_User_LoginAndTripStatusIn(login, historyStatuses, pageable);

        return trips.map(tripCusMapper::toTripListDTO);
    }
}
