package strip.web.rest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.PackageDriver;
import strip.domain.User;
import strip.service.MailService;
import strip.service.UserMobileService;
import strip.service.UserService;
import strip.service.dto.ConfirmingDriverDTO;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.PasswordChangeDTO;
import strip.service.dto.UpdateUserProfileDTO;
import strip.service.dto.UserProfileDTO;
import strip.service.dto.UserWalletWithTransactionsDTO;
import strip.service.dto.WithdrawRequestDTO;
import strip.web.rest.errors.InvalidPasswordException;
import strip.web.rest.vm.KeyAndPasswordVM;
import strip.web.rest.vm.ManagedUserVM;

@RestController
@RequestMapping("/api/mobile/user")
public class UserMobileResource {

    private static class UserMobileResourceException extends RuntimeException {

        private UserMobileResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(UserMobileResource.class);

    private final UserMobileService userMobileService;

    private final UserService userService;

    private final MailService mailService;

    public UserMobileResource(UserMobileService userMobileService, UserService userService, MailService mailService) {
        this.userMobileService = userMobileService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/getme")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        return userMobileService
            .getCurrentUserProfile()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping(path = "/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    @PostMapping(path = "/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            log.warn("Password reset requested for non existing mail");
        }
    }

    @PostMapping(path = "/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new UserMobileResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Void> updatePassengerProfile(@RequestBody UpdateUserProfileDTO dto) {
        userMobileService.updatePassengerProfile(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view/allpackages")
    public ResponseEntity<List<PackageDriver>> getDriverPackages() {
        return ResponseEntity.ok(userMobileService.getAvailableDriverPackages());
    }

    @PostMapping("/packages/{packageId}/buy")
    public ResponseEntity<Void> buyPackage(@PathVariable UUID packageId) {
        userMobileService.buyDriverPackage(packageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-driver")
    public ResponseEntity<ConfirmingVehicleDriverDTO> confirmDriver(@Valid @RequestBody ConfirmingDriverDTO dto) {
        log.debug("📨 REST request to confirm driver: {}", dto.getPhone());

        ConfirmingVehicleDriverDTO result = userMobileService.confirmDriver(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/confirm-driver")
    public ResponseEntity<ConfirmingVehicleDriverDTO> getDriverApplication() {
        log.debug("📨 REST request to get existing driver application");
        return userMobileService.getDriverApplication().map(ResponseEntity::ok).orElse(ResponseEntity.ok(new ConfirmingVehicleDriverDTO())); // fallback nếu lỗi
    }

    @PostMapping("/wallet/withdraw")
    public ResponseEntity<?> requestWithdraw(@RequestBody WithdrawRequestDTO dto) {
        userMobileService.createWithdrawRequest(dto);
        return ResponseEntity.ok("✅ Gửi yêu cầu rút tiền thành công!");
    }

    @GetMapping("/wallet/full")
    public ResponseEntity<UserWalletWithTransactionsDTO> getFullWalletInfo() {
        UserWalletWithTransactionsDTO dto = userMobileService.getWalletAndTransactions();
        return ResponseEntity.ok(dto);
    }
}
