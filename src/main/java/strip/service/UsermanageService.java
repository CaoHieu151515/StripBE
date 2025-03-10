package strip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.service.dto.UsermanageDTO;
import strip.service.mapper.UsermanageMapper;

@Service
@Transactional
public class UsermanageService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UsermanageMapper usermanageMapper;

    public UsermanageService(UserRepository userRepository, UserDetailRepository userDetailRepository, UsermanageMapper usermanageMapper) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.usermanageMapper = usermanageMapper;
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
        Page<User> users = userRepository.findAll(pageable);

        // Chuyển đổi danh sách User thành danh sách UsermanageDTO
        List<UsermanageDTO> filteredUsers = users
            .stream()
            .map(user -> {
                Optional<UserDetail> userDetail = userDetailRepository.findById(user.getId());
                UsermanageDTO dto = new UsermanageDTO(user, userDetail.orElse(null));

                // Kiểm tra các điều kiện lọc
                if (firstName != null && !dto.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                    return null;
                }
                if (lastName != null && !dto.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                    return null;
                }
                if (email != null && !dto.getEmail().toLowerCase().contains(email.toLowerCase())) {
                    return null;
                }
                if (active != null && dto.isActive() != active) {
                    return null;
                }
                return dto;
            })
            .filter(dto -> dto != null) // Loại bỏ các giá trị null do không phù hợp điều kiện lọc
            .collect(Collectors.toList());

        return new PageImpl<>(filteredUsers, pageable, filteredUsers.size()); // Trả về Page<UsermanageDTO>
    }
}
