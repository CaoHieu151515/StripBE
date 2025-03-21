package strip.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
import strip.domain.enumeration.DriverStatus;
import strip.domain.enumeration.VehicleStatus;
import strip.repository.DriverRepository;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;
import strip.repository.VehicleRepository;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.mapper.DriverInfoMapper;
import strip.service.mapper.UsermanageMapper;

@Service
@Transactional
public class UsermanageService {

    private final Logger log = LoggerFactory.getLogger(UsermanageService.class);
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

    public List<ConfirmingVehicleDriverDTO> getAllConfirmingVehicles() {
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
            vehicle.setStatus(VehicleStatus.REJECTED);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }

    public List<ConfirmingVehicleDriverDTO> getConfirmingDrivers() {
        List<ConfirmingVehicleDriverDTO> confirmingDrivers = new ArrayList<>();

        List<Driver> drivers = driverRepository.findByUsedtoDriverFalseAndDriverStatus(DriverStatus.CONFIRMING);
        log.debug("Found {} drivers with status CONFIRMING", drivers.size());
        for (Driver driver : drivers) {
            Optional<User> user = userRepository.findById(driver.getUser().getId());
            Optional<UserDetail> userDetail = userDetailRepository.findByUserId(driver.getUser().getId());
            Optional<Vehicle> vehicle = vehicleRepository.findFirstByDriver_DriverIDAndStatus(
                driver.getDriverID(),
                VehicleStatus.CONFIRMING
            );
            if (user.isPresent() && vehicle.isPresent()) {
                ConfirmingVehicleDriverDTO dto = driverInfoMapper.toConfirmingVehicleDTO(
                    user.get(),
                    userDetail.orElse(null),
                    driver,
                    vehicle.get()
                );
                confirmingDrivers.add(dto);
            }
        }
        return confirmingDrivers;
    }

    @Transactional
    public void approveDriver(UUID driverId) {
        Optional<Driver> driverOptional = driverRepository.findByDriverID(driverId);

        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            log.debug("Approving driver: {}", driverId);

            // Tìm xe đầu tiên có trạng thái CONFIRMING
            Optional<Vehicle> vehicleOptional = vehicleRepository.findFirstByDriver_DriverIDAndStatus(
                driver.getDriverID(),
                VehicleStatus.CONFIRMING
            );

            // Cập nhật trạng thái tài xế và xe
            driver.setDriverStatus(DriverStatus.ACTIVE);
            driver.setUsedtoDriver(true);
            vehicleOptional.ifPresent(vehicle -> {
                vehicle.setStatus(VehicleStatus.ACTIVE);
                vehicleRepository.save(vehicle);
            });

            driverRepository.save(driver);
            log.debug("Driver {} approved successfully", driverId);
        } else {
            throw new EntityNotFoundException("Driver not found with ID: " + driverId);
        }
    }

    @Transactional
    public void rejectDriver(UUID driverId) {
        Optional<Driver> driverOptional = driverRepository.findByDriverID(driverId);
        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            log.debug("Rejecting driver: {}", driverId);

            // Tìm xe đầu tiên có trạng thái CONFIRMING
            Optional<Vehicle> vehicleOptional = vehicleRepository.findFirstByDriver_DriverIDAndStatus(
                driver.getDriverID(),
                VehicleStatus.CONFIRMING
            );

            // Cập nhật trạng thái tài xế và xe
            driver.setDriverStatus(DriverStatus.NOT_DRIVER);
            vehicleOptional.ifPresent(vehicle -> {
                vehicle.setStatus(VehicleStatus.REJECTED);
                vehicleRepository.save(vehicle);
            });

            driverRepository.save(driver);
            log.debug("Driver {} rejected successfully", driverId);
        } else {
            throw new EntityNotFoundException("Driver not found with ID: " + driverId);
        }
    }
}