// === TripCustomService.java ===
package strip.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.config.ApplicationProperties;
import strip.domain.*;
import strip.domain.enumeration.*;
import strip.repository.*;
import strip.service.dto.TripCreateDTO;
import strip.service.dto.TripStopLocationUpdateDTO;
import strip.service.dto.TripUpdateDTO;

@Service
@Transactional
public class TripCustomService {

    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final UserWalletRepository userWalletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final SystemWalletRepository systemWalletRepository;
    private final TripStopLocationRepository tripStopLocationRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationProperties applicationProperties;
    private final RequestTripRepository requestTripRepository;

    public TripCustomService(
        TripRepository tripRepository,
        DriverRepository driverRepository,
        UserWalletRepository userWalletRepository,
        WalletTransactionRepository walletTransactionRepository,
        SystemWalletRepository systemWalletRepository,
        TripStopLocationRepository tripStopLocationRepository,
        VehicleRepository vehicleRepository,
        ApplicationProperties applicationProperties,
        RequestTripRepository requestTripRepository
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.userWalletRepository = userWalletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.systemWalletRepository = systemWalletRepository;
        this.tripStopLocationRepository = tripStopLocationRepository;
        this.vehicleRepository = vehicleRepository;
        this.applicationProperties = applicationProperties;
        this.requestTripRepository = requestTripRepository;
    }

    public Trip createTripWithFee(TripCreateDTO dto, UUID driverId) {
        Driver driver = validateDriver(driverId);
        Vehicle vehicle = validateVehicleOwnership(dto.getVehicleId(), driver);
        validateTripScheduleConflict(driver.getDriverID(), dto.getStartDate(), dto.getEndDate());

        double revenue = dto.getPricePerSeat() * dto.getMaxSeat();
        double fee = revenue * applicationProperties.getTrip().getFee().getDriverCreate();

        UserWallet userWallet = validateWalletBalance(driver, fee);
        SystemWallet systemWallet = systemWalletRepository.findTopByOrderByMobifyDateDesc().orElseGet(this::createInitialSystemWallet);

        Trip trip = buildTrip(dto, driver, vehicle);
        tripRepository.save(trip);
        saveStopLocations(dto, trip);

        createAndSaveWalletTransactions(userWallet, systemWallet, trip, fee);
        return trip;
    }

    private Driver validateDriver(UUID driverId) {
        Driver driver = driverRepository.findByDriverID(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));

        if (driver.getExpirationDate() == null || driver.getExpirationDate().isBefore(Instant.now())) {
            throw new RuntimeException("Gói tài xế đã hết hạn, không thể tạo chuyến đi");
        }
        return driver;
    }

    private Vehicle validateVehicleOwnership(UUID vehicleId, Driver driver) {
        Vehicle vehicle = vehicleRepository.findByVehicleID(vehicleId).orElseThrow(() -> new RuntimeException("Xe không tìm thấy"));

        if (vehicle.getStatus() != VehicleStatus.ACTIVE) {
            throw new RuntimeException("Xe chưa được kích hoạt");
        }

        if (!vehicle.getDriver().getDriverID().equals(driver.getDriverID())) {
            throw new RuntimeException("Xe không thuộc quyền sở hữu của tài xế này");
        }
        return vehicle;
    }

    private void validateTripScheduleConflict(UUID driverId, Instant start, Instant end) {
        List<Trip> overlappingTrips = tripRepository.findOverlappingTripsByDriver(driverId, start, end);
        if (!overlappingTrips.isEmpty()) {
            throw new RuntimeException("Tài xế đã có chuyến đi trùng giờ");
        }
    }

    private UserWallet validateWalletBalance(Driver driver, double fee) {
        UserWallet wallet = userWalletRepository.findByUser(driver.getUser()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getCurrent() == null || wallet.getCurrent() < fee) {
            throw new RuntimeException("Số dư không đủ để tạo chuyến đi");
        }
        return wallet;
    }

    private Trip buildTrip(TripCreateDTO dto, Driver driver, Vehicle vehicle) {
        Trip trip = new Trip();
        trip.setTripID(UUID.randomUUID());
        trip.setPricePerSeat(dto.getPricePerSeat());
        trip.setMaxSeat(dto.getMaxSeat());
        trip.setCurrentSeat(0);
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setStartLocation(dto.getStartLocation());
        trip.setEndLocation(dto.getEndLocation());
        trip.setDescription(dto.getDescription());
        trip.setCondition(dto.getCondition());
        trip.setTripStatus(TripStatus.CONFIRMING);
        trip.setDriver(driver);
        trip.setTripImg(dto.getTripImg());
        trip.setVehicle(vehicle);
        return trip;
    }

    private void saveStopLocations(TripCreateDTO dto, Trip trip) {
        if (dto.getStopLocations() != null) {
            for (var stopDTO : dto.getStopLocations()) {
                TripStopLocation stop = new TripStopLocation();
                stop.setStopLocaID(UUID.randomUUID());
                stop.setStopLoca(stopDTO.getStopLoca());
                stop.setStopLocaTime(stopDTO.getStopLocaTime());
                stop.setStopLocaStatus(stopDTO.getStopLocaStatus());
                stop.setTrip(trip);
                tripStopLocationRepository.save(stop);
            }
        }
    }

    private void createAndSaveWalletTransactions(UserWallet userWallet, SystemWallet systemWallet, Trip trip, double fee) {
        WalletTransaction userTx = new WalletTransaction();
        userTx.setTransID(UUID.randomUUID());
        userTx.setAmount(fee);
        userTx.setDate(Instant.now());
        userTx.setWalletType(WalletTransactionType.DRIVER_CREATE_TRIP_FEE);
        userTx.setTransStatus(TransactionStatus.SUCCESS);
        userTx.setTransactionThirdPartyID(null);
        userTx.setUserWallet(userWallet);
        userWallet.addWalletTransactionAndUpdateBalance(userTx);
        userWalletRepository.save(userWallet);

        WalletTransaction systemTx = new WalletTransaction();
        systemTx.setTransID(UUID.randomUUID());
        systemTx.setAmount(fee);
        systemTx.setDate(Instant.now());
        systemTx.setWalletType(WalletTransactionType.SYSTEM_GAIN_CREATE_TRIP_FEE);
        systemTx.setTransStatus(TransactionStatus.SUCCESS);
        systemTx.setTransactionThirdPartyID(null);
        systemTx.setSystemWallet(systemWallet);
        systemWallet.addWalletTransactionAndUpdateBalance(systemTx);
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

    public List<RequestTrip> findRequestsByTripId(UUID tripId) {
        return requestTripRepository.findAllByTrip_TripID(tripId);
    }

    @Transactional
    public void updateTripStopLocations(UUID tripId, Set<TripStopLocationUpdateDTO> newStops) {
        Trip trip = tripRepository.findByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        // Xoá các điểm dừng cũ
        tripStopLocationRepository.deleteAllByTrip_TripID(tripId);

        // Tạo lại danh sách mới
        for (TripStopLocationUpdateDTO dto : newStops) {
            TripStopLocation stop = new TripStopLocation();
            stop.setStopLocaID(UUID.randomUUID());
            stop.setStopLoca(dto.getStopLoca());
            stop.setStopLocaTime(dto.getStopLocaTime());
            stop.setStopLocaStatus(dto.getStopLocaStatus());
            stop.setTrip(trip);
            tripStopLocationRepository.save(stop);
        }
    }

    @Transactional
    public Trip updateTripInfo(UUID tripId, TripUpdateDTO dto) {
        Trip trip = tripRepository.findByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        trip.setPricePerSeat(dto.getPricePerSeat());
        trip.setMaxSeat(dto.getMaxSeat());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setStartLocation(dto.getStartLocation());
        trip.setEndLocation(dto.getEndLocation());
        trip.setDescription(dto.getDescription());
        trip.setCondition(dto.getCondition());

        return tripRepository.save(trip);
    }

    public Trip getFullTrip(UUID tripId) {
        return tripRepository.findFullTripByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));
    }

    @Transactional
    public Trip resendAndUpdateTrip(UUID tripId, TripUpdateDTO dto) {
        Trip trip = tripRepository.findByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        trip.setPricePerSeat(dto.getPricePerSeat());
        trip.setMaxSeat(dto.getMaxSeat());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setStartLocation(dto.getStartLocation());
        trip.setEndLocation(dto.getEndLocation());
        trip.setDescription(dto.getDescription());
        trip.setCondition(dto.getCondition());

        // 🔁 Đặt lại trạng thái xét duyệt
        trip.setTripStatus(TripStatus.CONFIRMING);

        return tripRepository.save(trip);
    }

    @Transactional
    public RequestTrip acceptRequestTrip(UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (request.getStatus() != PassengerStatus.WAITING) {
            throw new RuntimeException("Yêu cầu không ở trạng thái chờ duyệt");
        }

        Trip trip = request.getTrip();
        if (trip == null) {
            throw new RuntimeException("Yêu cầu chưa gắn với chuyến đi nào");
        }

        // ✅ Tính tổng số ghế đã được BOOKED
        int totalBookedSeats = trip
            .getRequestTrips()
            .stream()
            .filter(r -> r.getStatus() == PassengerStatus.BOOKED)
            .mapToInt(RequestTrip::getNumberofSeats)
            .sum();

        int seatsLeft = trip.getMaxSeat() - totalBookedSeats;

        // ✅ Kiểm tra xem ghế của request này có hợp lệ không
        if (request.getNumberofSeats() > seatsLeft) {
            throw new RuntimeException("Không đủ số ghế trống để duyệt yêu cầu này");
        }

        // ✅ Cập nhật trạng thái và thông tin
        request.setStatus(PassengerStatus.BOOKED);
        request.setCheckIn(false);
        request.setCheckOut(false);
        request.setAppliedAt(Instant.now());

        // ✅ Đảm bảo liên kết giữa trip và requestTrip
        trip.addRequestTrip(request);

        // ✅ Lưu lại
        requestTripRepository.save(request);
        return request;
    }

    @Transactional
    public RequestTrip rejectRequestTrip(UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (request.getStatus() != PassengerStatus.WAITING) {
            throw new RuntimeException("Chỉ có thể từ chối yêu cầu đang chờ");
        }

        request.setStatus(PassengerStatus.REJECTED);
        request.setAppliedAt(Instant.now());
        return requestTripRepository.save(request);
    }

    @Transactional
    public RequestTrip checkIn(UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (request.getStatus() != PassengerStatus.BOOKED) {
            throw new RuntimeException("Không thể check-in yêu cầu không ở trạng thái BOOKED");
        }

        if (Boolean.TRUE.equals(request.getCheckIn())) {
            throw new RuntimeException("Đã check-in trước đó");
        }

        request.setCheckIn(true);
        request.setCheckInTime(Instant.now());

        return requestTripRepository.save(request);
    }

    @Transactional
    public RequestTrip checkOut(UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (!Boolean.TRUE.equals(request.getCheckIn())) {
            throw new RuntimeException("Chưa check-in, không thể check-out");
        }

        if (Boolean.TRUE.equals(request.getCheckOut())) {
            throw new RuntimeException("Đã check-out trước đó");
        }

        request.setCheckOut(true);
        request.setCheckOutTIme(Instant.now());

        return requestTripRepository.save(request);
    }
}
