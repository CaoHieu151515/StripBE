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
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.DriverRepository;
import strip.repository.PackageDriverRepository;
import strip.repository.SystemWalletRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.repository.VehicleRepository;
import strip.repository.WalletTransactionRepository;
import strip.service.dto.UpdateUserProfileDTO;
import strip.service.dto.UserDetailsCusDTO;
import strip.service.dto.UserProfileDTO;
import strip.service.dto.UserWalletWithTransactionsDTO;

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
        Optional<UserWallet> userWallet = userWalletRepository.findByUser(user);
        Optional<Driver> driverOpt = driverRepository.findByUser(user);

        Set<String> roles = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());

        // Get vehicles nếu có driver
        List<Vehicle> vehicles = driverOpt.map(driver -> vehicleRepository.findAllByDriver(driver)).orElse(Collections.emptyList());

        boolean isDriver = driverOpt.isPresent() && driverOpt.get().getDriverStatus() != DriverStatus.NOT_DRIVER;
        boolean hasVehicle = !vehicles.isEmpty();

        // ✅ Convert UserDetail → UserDetailsCusDTO
        UserDetailsCusDTO userDetailsCusDTO = userDetailOpt
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

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUser(user);
        dto.setUserDetailsCusDTO(userDetailsCusDTO);
        dto.setUserWallet(userWallet.orElse(null));
        dto.setDriver(driverOpt.orElse(null));
        dto.setVehicles(vehicles);
        dto.setDriver(isDriver);
        dto.setHasVehicle(hasVehicle);
        dto.setRoles(roles);

        return Optional.of(dto);
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
}
