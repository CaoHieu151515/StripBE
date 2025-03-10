package strip.web.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.service.UserService;
import strip.service.UsermanageService;
import strip.service.dto.AdminUserDTO;
import strip.service.dto.UsermanageDTO;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/manager")
public class ManagerResource {

    private static class ManagerResourceException extends RuntimeException {

        private ManagerResourceException(String message) {
            super(message);
        }
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "username", "firstName", "lastName", "email", "active", "gender", "phoneNumber")
    );

    private final UserService userService;
    private final UsermanageService usermanageService;

    private final Logger log = LoggerFactory.getLogger(ManagerResource.class);

    public ManagerResource(UserService userService, UsermanageService usermanageService) {
        this.userService = userService;
        this.usermanageService = usermanageService;
    }

    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new ManagerResourceException("User could not be found"));
    }

    @GetMapping("/GetAllUsers")
    public ResponseEntity<List<UsermanageDTO>> getAllUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Boolean active
    ) {
        log.debug("REST request to get all Users with filters");

        // Kiểm tra các thuộc tính sắp xếp có hợp lệ không
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        // Gọi service để lấy danh sách UsermanageDTO theo phân trang và lọc
        final Page<UsermanageDTO> page = usermanageService.getAllUsers(pageable, firstName, lastName, email, active);

        // Tạo HttpHeaders để hỗ trợ phân trang giống JHipster
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsermanageDTO> getUserById(@PathVariable Long id) {
        UsermanageDTO user = usermanageService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
