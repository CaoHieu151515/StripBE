package strip.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.config.Constants;
import strip.domain.Authority;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.UserWallet;
import strip.domain.enumeration.DriverStatus;
import strip.repository.AuthorityRepository;
import strip.repository.DriverRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;
import strip.security.AuthoritiesConstants;
import strip.security.SecurityUtils;
import strip.service.dto.AdminUserDTO;
import strip.service.dto.ChangeAvatarDTO;
import strip.service.dto.CurrentUserDTO;
import strip.service.dto.UpdateUserProfileNoImageDTO;
import strip.service.dto.UserDTO;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserDetailRepository userDetailRepository;

    private final OtpCacheService otpCacheService;

    private final DriverRepository driverRepository;

    private final UserWalletRepository userWalletRepository;

    private final ImageUrlService imageUrlService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        OtpCacheService otpCacheService,
        UserDetailRepository userDetailRepository,
        DriverRepository driverRepository,
        UserWalletRepository userWalletRepository,
        ImageUrlService imageUrlService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.otpCacheService = otpCacheService;
        this.userDetailRepository = userDetailRepository;
        this.driverRepository = driverRepository;
        this.userWalletRepository = userWalletRepository;
        this.imageUrlService = imageUrlService;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.PASSENGER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Transactional
    public User registerUserOTP(AdminUserDTO userDTO, String password) {
        // Kiểm tra email đã tồn tại chưa
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyUsedException(); // Chặn đăng ký nếu email đã tồn tại
        }

        // Tạo mã login ngẫu nhiên 9 ký tự và đảm bảo không bị trùng
        String login;
        do {
            login = RandomUsername.generateRandomCode(9);
        } while (userRepository.findOneByLogin(login).isPresent());

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);

        newUser.setLogin(login);
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setActivated(true);
        newUser.setActivationKey(null);

        // Gán quyền mặc định
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.PASSENGER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        this.clearUserCaches(newUser);

        // 👉 Khởi tạo các entity liên quan
        initializeUserData(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public String sendOTP(String email) {
        String otp = generateOTP(6);
        otpCacheService.saveOtp(email, otp);

        return otp;
    }

    public boolean verifyUserOtp(String email, String inputOtp) {
        if (inputOtp == null || inputOtp.isEmpty()) {
            log.warn("OTP is null or empty for email: {}", email);
            return false;
        }

        // Lấy OTP từ cache
        String cachedOtp = otpCacheService.getOtp(email);
        log.debug("Cached OTP for {}: {}", email, cachedOtp);

        // So sánh OTP nhập vào với OTP trong cache
        if (cachedOtp != null && cachedOtp.equals(inputOtp)) {
            log.info("OTP verification successful for email: {}", email);
            otpCacheService.removeOtp(email); // Xóa OTP sau khi xác minh thành công
            return true;
        }

        log.warn("OTP verification failed for email: {}. Input OTP: {}, Expected OTP: {}", email, inputOtp, cachedOtp);
        return false;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the
     * current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Chỉ số
        }
        return otp.toString();
    }

    public Optional<CurrentUserDTO> getCurrentUserInfo() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .map(user -> {
                CurrentUserDTO dto = new CurrentUserDTO();
                dto.setLogin(user.getLogin());
                dto.setEmail(user.getEmail());
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());

                userDetailRepository
                    .findById(user.getId())
                    .ifPresent(detail -> {
                        dto.setPhone(detail.getPhone());
                        dto.setGender(detail.getGender());
                        dto.setAddress(detail.getAddress());
                        dto.setDob(detail.getDob());
                        dto.setUserImageUrl(imageUrlService.buildUserAvatarUrl(detail.getAppUserDetail()));
                    });
                Set<String> roles = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
                dto.setRoles(roles);

                return dto;
            });
    }

    @Transactional
    public void initializeUserData(User user) {
        // UserDetail
        UserDetail detail = new UserDetail();
        detail.setAppUserDetail(UUID.randomUUID());
        detail.setUser(user);
        userDetailRepository.save(detail);

        // UserWallet
        UserWallet wallet = new UserWallet();
        wallet.setUserWallet(UUID.randomUUID());
        wallet.setUser(user);
        wallet.setBefore(0.0);
        wallet.setAmount(0.0);
        wallet.setCurrent(0.0);
        wallet.setMobifyDate(Instant.now());
        userWalletRepository.save(wallet);

        // Driver (mặc định là NOT_DRIVER)
        Driver driver = new Driver();
        driver.setDriverID(UUID.randomUUID());
        driver.setUser(user);
        driver.setDriverPoint(14);
        driver.setUsedtoDriver(false);
        driver.setDriverStatus(DriverStatus.NOT_DRIVER);
        driver.setDriverPoint(0);
        driverRepository.save(driver);
    }

    @Transactional
    public void updateUserProfile(UpdateUserProfileNoImageDTO dto) {
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("Người dùng không hợp lệ", "user", "notfound"));

        // ✅ Cập nhật User
        currentUser.setFirstName(dto.getFirstName());
        currentUser.setLastName(dto.getLastName());
        userRepository.save(currentUser);

        // ✅ Cập nhật hoặc tạo UserDetail
        UserDetail userDetail = userDetailRepository
            .findByUserId(currentUser.getId())
            .orElseGet(() -> {
                UserDetail detail = new UserDetail();
                detail.setAppUserDetail(UUID.randomUUID());
                detail.setUser(currentUser);
                return detail;
            });

        userDetail.setPhone(dto.getPhone());
        userDetail.setAddress(dto.getAddress());
        userDetail.setDob(dto.getDob());
        userDetail.setGender(dto.getGender());

        userDetailRepository.save(userDetail);
    }

    @Transactional
    public void updateAvatar(ChangeAvatarDTO dto) {
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("Người dùng không hợp lệ", "user", "notfound"));

        UserDetail userDetail = userDetailRepository
            .findByUserId(currentUser.getId())
            .orElseGet(() -> {
                UserDetail detail = new UserDetail();
                detail.setAppUserDetail(UUID.randomUUID());
                detail.setUser(currentUser);
                return detail;
            });

        if (dto.getUserImage() == null || dto.getUserImage().length == 0) {
            throw new BadRequestAlertException("Ảnh không hợp lệ", "user", "invalid-avatar");
        } else {
            userDetail.setUserimage(dto.getUserImage());
            userDetail.setUserimageContentType(dto.getUserImageContentType());
        }
        userDetailRepository.save(userDetail);
    }
}
