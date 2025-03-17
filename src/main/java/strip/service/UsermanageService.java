package strip.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.Vehicle;
import strip.domain.enumeration.VehicleStatus;
import strip.repository.DriverRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.VehicleRepository;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.mapper.DriverInfoMapper;
import strip.service.mapper.UsermanageMapper;

@Service
@Transactional
public class UsermanageService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UsermanageMapper usermanageMapper;
    private final DriverRepository driverRepository;
    private final DriverInfoMapper driverInfoMapper;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    public UsermanageService(
        UserRepository userRepository,
        UserDetailRepository userDetailRepository,
        UsermanageMapper usermanageMapper,
        DriverRepository driverRepository,
        DriverInfoMapper driverInfoMapper,
        VehicleService vehicleService,
        VehicleRepository vehicleRepository
    ) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.usermanageMapper = usermanageMapper;
        this.driverRepository = driverRepository;
        this.driverInfoMapper = driverInfoMapper;
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
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

    public Optional<UsermanageDetailsDTO> getUserDetailsById(Long id) {
        return userRepository
            .findById(id)
            .map(user -> {
                Optional<UserDetail> userDetail = userDetailRepository.findByUser(user);
                Optional<Driver> driver = driverRepository.findByUser(user);
                return new UsermanageDetailsDTO(user, userDetail.orElse(null), driver.orElse(null));
            });
    }

    public Optional<DriverInfoDTO> getDriverDetailsByUsername(String username) {
        Optional<User> user = userRepository.findOneByLogin(username);

        if (user.isPresent()) {
            Optional<UserDetail> userDetail = userDetailRepository.findById(user.get().getId());
            Optional<Driver> driver = driverRepository.findById(user.get().getId());

            if (driver.isPresent()) {
                DriverInfoDTO dto = driverInfoMapper.toDriverInfoDTO(user.get(), userDetail.orElse(null), driver.get());

                // Lấy danh sách phương tiện của driver và ánh xạ sang DTO
                Set<DriverVehicleDTO> vehicleDTOs = driver
                    .get()
                    .getVehicles()
                    .stream()
                    .map(driverInfoMapper::toDriverVehicleDTO)
                    .collect(Collectors.toSet());

                dto.setVehicles(vehicleDTOs);
                return Optional.of(dto);
            }
        }

        return Optional.empty();
    }

    public List<ConfirmingVehicleDTO> getAllConfirmingVehicles() {
        List<Vehicle> confirmingVehicles = vehicleRepository.findByStatus(VehicleStatus.CONFIRMING);

        return confirmingVehicles
            .stream()
            .map(vehicle -> {
                User user = vehicle.getDriver().getUser();
                UserDetail userDetail = userDetailRepository.findById(user.getId()).orElse(null);
                Driver driver = vehicle.getDriver();
                return driverInfoMapper.toConfirmingVehicleDTO(user, userDetail, driver, vehicle);
            })
            .collect(Collectors.toList());
    }

    public boolean approveVehicle(UUID vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVehicleID(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setStatus(VehicleStatus.ACTIVE);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }

    public boolean rejectVehicle(UUID vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByVehicleID(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setStatus(VehicleStatus.CANCEL);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }
}
