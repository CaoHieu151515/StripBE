package strip.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.Authority;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.repository.AuthorityRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.service.dto.StaffCreateDTO;
import strip.service.dto.StaffUpdateDTO;
import strip.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/admin")
public class StaffManagementResource {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailRepository userDetailRepository;

    public StaffManagementResource(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        UserDetailRepository userDetailRepository
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailRepository = userDetailRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/staff")
    public ResponseEntity<Void> createStaff(@RequestBody StaffCreateDTO dto) {
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new BadRequestAlertException("Username already used", "user", "userexists");
        }

        // 1. Tạo User
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLangKey("vi");
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 2. Gán ROLE_STAFF
        Authority staffRole = authorityRepository.findById("ROLE_STAFF").orElseThrow();
        user.setAuthorities(Set.of(staffRole));
        user = userRepository.save(user);

        // 3. Tạo UserDetail gắn với user vừa tạo
        UserDetail detail = new UserDetail();
        detail.setAppUserDetail(UUID.randomUUID());
        detail.setUser(user);
        detail.setPhone(dto.getPhone());
        detail.setAddress(dto.getAddress());
        detail.setDob(dto.getDob());
        detail.setGender(dto.getGender());
        userDetailRepository.save(detail);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/staff")
    public List<StaffUpdateDTO> getAllStaff(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phone
    ) {
        List<User> staffUsers = userRepository
            .findAll()
            .stream()
            .filter(user -> user.getAuthorities().stream().anyMatch(auth -> "ROLE_STAFF".equals(auth.getName())))
            .collect(Collectors.toList());

        return staffUsers
            .stream()
            .map(user -> {
                Optional<UserDetail> detail = userDetailRepository.findByUserId(user.getId());
                return new StaffUpdateDTO(user, detail.orElse(null));
            })
            .filter(dto -> firstName == null || dto.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
            .filter(dto -> lastName == null || dto.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .filter(dto -> email == null || dto.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(dto -> phone == null || (dto.getPhone() != null && dto.getPhone().toLowerCase().contains(phone.toLowerCase())))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/staff/update-by-detail")
    public ResponseEntity<Void> updateStaffByDetail(@RequestBody StaffUpdateDTO dto) {
        UserDetail detail = userDetailRepository
            .findByAppUserDetail(dto.getUserId())
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "userdetail", "notfound"));

        // Cập nhật thông tin chi tiết
        detail.setPhone(dto.getPhone());
        detail.setAddress(dto.getAddress());
        detail.setDob(dto.getDob());
        detail.setGender(dto.getGender());
        userDetailRepository.save(detail);

        // Cập nhật thông tin User
        User user = detail.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/staff/toggle-activation/{userid}")
    public ResponseEntity<Void> toggleStaffActivation(@PathVariable UUID userid) {
        // Tìm UserDetail
        UserDetail detail = userDetailRepository
            .findByAppUserDetail(userid)
            .orElseThrow(() -> new BadRequestAlertException("UserDetail not found", "userdetail", "notfound"));

        User user = detail.getUser();

        // Kiểm tra có phải STAFF không
        boolean isStaff = user.getAuthorities().stream().anyMatch(auth -> "ROLE_STAFF".equals(auth.getName()));
        if (!isStaff) {
            throw new BadRequestAlertException("User is not a staff", "user", "notstaff");
        }

        // Toggle trạng thái
        user.setActivated(!user.isActivated());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
