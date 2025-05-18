package strip.web.rest;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.User;
import strip.repository.UserRepository;
import strip.security.SecurityUtils;
import strip.service.MailService;
import strip.service.UserService;
import strip.service.dto.AdminUserDTO;
import strip.service.dto.ChangeAvatarDTO;
import strip.service.dto.PasswordChangeDTO;
import strip.service.dto.RegisterWithoutOTPDTO;
import strip.service.dto.UpdateUserProfileNoImageDTO;
import strip.service.dto.UserDTO;
import strip.web.rest.errors.EmailAlreadyUsedException;
import strip.web.rest.errors.InvalidPasswordException;
import strip.web.rest.errors.LoginAlreadyUsedException;
import strip.web.rest.vm.KeyAndPasswordVM;
import strip.web.rest.vm.ManagedUserVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password
     *                                   is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
     *                                   already used.
     */
    // @PostMapping("/register")
    // @ResponseStatus(HttpStatus.CREATED)
    // public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM)
    // {
    // if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
    // throw new InvalidPasswordException();
    // }

    // // Kiểm tra email đã tồn tại trước khi đăng ký
    // if
    // (userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).isPresent())
    // {
    // throw new EmailAlreadyUsedException();
    // }

    // User user = userService.registerUser(managedUserVM,
    // managedUserVM.getPassword());
    // mailService.sendActivationEmail(user);
    // }

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtp(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("Request to send OTP to email: {}", managedUserVM.getEmail());

        // Kiểm tra xem tài khoản có tồn tại không
        if (userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        // Kiểm tra username đã được sử dụng chưa (nếu có username)
        if (managedUserVM.getLogin() != null && userRepository.findOneByLogin(managedUserVM.getLogin()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }

        String OTP = userService.sendOTP(managedUserVM.getEmail());
        mailService.sendActivationOTP(managedUserVM.getEmail(), OTP);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody ManagedUserVM managedUserDetailsVM) {
        boolean isValid = userService.verifyUserOtp(managedUserDetailsVM.getEmail(), managedUserDetailsVM.getOTP());
        if (isValid) {
            userService.registerUserOTP(managedUserDetailsVM, managedUserDetailsVM.getPassword());
            return ResponseEntity.ok("Register Successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be returned.
     */
    @Hidden
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the
     *                                   user login wasn't found.
     */
    @Hidden
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new
     *                                  password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the
     * password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Pretend the request has been successful to prevent checking which emails
            // really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password
     * of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is
     *                                  incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the
     *                                  password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @PutMapping("/account/update/profile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody UpdateUserProfileNoImageDTO dto) {
        userService.updateUserProfile(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/avatar")
    public ResponseEntity<Void> changeAvatar(@RequestBody ChangeAvatarDTO dto) {
        userService.updateAvatar(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/public/register-without-otp")
    public ResponseEntity<UserDTO> registerUserWithoutOTP(@RequestBody RegisterWithoutOTPDTO dto) {
        User user = userService.registerUserWithoutOTP(dto);
        return ResponseEntity.ok(new UserDTO(user));
    }
}
