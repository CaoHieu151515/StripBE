// === TripCustomService.java ===
package strip.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.config.ApplicationProperties;
import strip.domain.*;
import strip.domain.enumeration.*;
import strip.repository.*;
import strip.security.SecurityUtils;
import strip.service.dto.DriverRawDTO;
import strip.service.dto.RequestTripCusDTO;
import strip.service.dto.TripCardDTO;
import strip.service.dto.TripCreateDTO;
import strip.service.dto.TripDetailDTO;
import strip.service.dto.TripDetailForDriverHistoryDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.TripStopLocationSkipTripDTO;
import strip.service.dto.TripStopLocationUpdateDTO;
import strip.service.dto.TripUpdateDTO;
import strip.service.dto.VehicleRawDTO;
import strip.service.mapper.TripCusMapper;
import strip.service.mapper.TripStopLocationSkipTripMapper;
import strip.service.mapper.UsermanageMapper;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional
public class TripCustomService {

    private final ImageUrlService imageUrlService;

    private static final Logger LOG = LoggerFactory.getLogger(TripCustomService.class);
    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final UserWalletRepository userWalletRepository;
    private final SystemWalletRepository systemWalletRepository;
    private final TripStopLocationRepository tripStopLocationRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationProperties applicationProperties;
    private final RequestTripRepository requestTripRepository;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final TripStopLocationSkipTripMapper tripStopLocationSkipTripMapper;
    private final UsermanageMapper usermanageMapper;
    private final TripCusMapper tripCusMapper;
    private final RatingRepository ratingRepository;

    public TripCustomService(
        TripRepository tripRepository,
        DriverRepository driverRepository,
        UserWalletRepository userWalletRepository,
        SystemWalletRepository systemWalletRepository,
        TripStopLocationRepository tripStopLocationRepository,
        VehicleRepository vehicleRepository,
        ApplicationProperties applicationProperties,
        RequestTripRepository requestTripRepository,
        UserRepository userRepository,
        UserDetailRepository userDetailRepository,
        WalletTransactionRepository walletTransactionRepository,
        TripStopLocationSkipTripMapper tripStopLocationSkipTripMapper,
        ImageUrlService imageUrlService,
        UsermanageMapper usermanageMapper,
        TripCusMapper tripCusMapper,
        RatingRepository ratingRepository
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.userWalletRepository = userWalletRepository;
        this.systemWalletRepository = systemWalletRepository;
        this.tripStopLocationRepository = tripStopLocationRepository;
        this.vehicleRepository = vehicleRepository;
        this.applicationProperties = applicationProperties;
        this.requestTripRepository = requestTripRepository;
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.imageUrlService = imageUrlService;
        this.tripStopLocationSkipTripMapper = tripStopLocationSkipTripMapper;
        this.usermanageMapper = usermanageMapper;
        this.tripCusMapper = tripCusMapper;
        this.ratingRepository = ratingRepository;
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
        userTx.setTransactionThirdPartyID(trip.getTripID().toString());
        userTx.setUserWallet(userWallet);
        userWallet.addWalletTransactionAndUpdateBalance(userTx);
        userWalletRepository.save(userWallet);

        WalletTransaction systemTx = new WalletTransaction();
        systemTx.setTransID(UUID.randomUUID());
        systemTx.setAmount(fee);
        systemTx.setDate(Instant.now());
        systemTx.setWalletType(WalletTransactionType.SYSTEM_GAIN_CREATE_TRIP_FEE);
        systemTx.setTransStatus(TransactionStatus.SUCCESS);
        systemTx.setTransactionThirdPartyID(trip.getTripID().toString());
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

    @Transactional
    public List<RequestTripCusDTO> getRequestTripCusDTOsByTripId(UUID tripId) {
        List<RequestTrip> requests = findRequestsByTripId(tripId);

        return requests.stream().map(this::mapToRequestTripCusDTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateTripStopLocations(UUID tripId, Set<TripStopLocationUpdateDTO> newStops) {
        Trip trip = tripRepository.findByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        boolean hasActivePassenger = trip
            .getRequestTrips()
            .stream()
            .anyMatch(
                r ->
                    r.getStatus() == PassengerStatus.WAITING ||
                    r.getStatus() == PassengerStatus.BOOKED ||
                    r.getStatus() == PassengerStatus.DONE
            );

        if (hasActivePassenger) {
            throw new BadRequestAlertException("Không thể cập nhật điểm dừng vì đã có người tham gia chuyến đi", "tripStop", "locked");
        }

        // Xoá các điểm dừng cũ
        tripStopLocationRepository.deleteAllByTrip_TripID(tripId);

        // Tạo lại danh sách mới
        for (TripStopLocationUpdateDTO dto : newStops) {
            TripStopLocation stop = new TripStopLocation();
            stop.setStopLocaID(UUID.randomUUID());
            stop.setStopLoca(dto.getStopLoca());
            stop.setStopLocaTime(dto.getStopLocaTime());
            stop.setStopLocaStatus(dto.getStopLocaStatus());
            stop.setEstimatedKM(dto.getEstimatedKM());
            stop.setEstimatedTime(dto.getEstimatedTime());
            stop.setStoplocaPosition(dto.getStoplocaPosition());
            stop.setTrip(trip);
            tripStopLocationRepository.save(stop);
        }
    }

    public List<RequestTrip> findRequestsByTripId(UUID tripId) {
        return requestTripRepository.findAllByTrip_TripID(tripId);
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

    public TripDetailDTO getFullTrip(UUID tripId) {
        Trip trip = tripRepository.findFullTripByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        // ✅ Map cơ bản
        TripDetailDTO dto = usermanageMapper.toTripDetailDTO(trip);

        // ✅ Trip image
        dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));

        // ✅ Driver + Avatar + CMND
        if (trip.getDriver() != null && trip.getDriver().getUser() != null) {
            userDetailRepository
                .findByUserId(trip.getDriver().getUser().getId())
                .ifPresent(detail -> {
                    DriverRawDTO driverDTO = usermanageMapper.toRawDTO(trip.getDriver(), detail);

                    UUID driverId = driverDTO.getDriverId();
                    UUID userDetailId = detail.getAppUserDetail();
                    driverDTO.setRating(getAverageRatingForDriver(driverId));
                    // Set ảnh
                    driverDTO.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverId));
                    driverDTO.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverId));
                    driverDTO.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverId));
                    driverDTO.setAvatarUrl(imageUrlService.buildUserAvatarUrl(userDetailId));

                    dto.setDriver(driverDTO);
                });
        }

        // ✅ Vehicle + ảnh
        if (trip.getVehicle() != null) {
            VehicleRawDTO vehicleDTO = usermanageMapper.toRawDTO(trip.getVehicle());
            UUID vehicleId = vehicleDTO.getVehicleID();

            vehicleDTO.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicleId));
            vehicleDTO.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicleId));
            vehicleDTO.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicleId));
            vehicleDTO.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicleId));

            dto.setVehicle(vehicleDTO);
        }

        // ✅ Stop locations
        if (trip.getTripStopLocations() != null && !trip.getTripStopLocations().isEmpty()) {
            Set<TripStopLocationSkipTripDTO> stops = trip
                .getTripStopLocations()
                .stream()
                .map(tripStopLocationSkipTripMapper::toDto)
                .collect(Collectors.toSet());

            dto.setStoplocation(stops);
        }

        return dto;
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
        trip.setCancelReason(null);
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
        requestTripRepository.save(request);

        // ✅ Chỉ hoàn tiền nếu đã thực sự thanh toán (trả trước)
        Double amount = request.getAmountApproveFee();
        if (amount != null && amount > 0) {
            User user = request.getUser();
            Optional<UserWallet> userWalletOpt = userWalletRepository.findByUser(user);
            Optional<UserDetail> detailOpt = userDetailRepository.findByUserId(user.getId());

            if (userWalletOpt.isPresent() && detailOpt.isPresent()) {
                UserWallet userWallet = userWalletOpt.get();
                UserDetail detail = detailOpt.get();

                String txKey = request.getTrip().getTripID().toString() + "-" + detail.getAppUserDetail().toString();

                Optional<WalletTransaction> approveTxOpt =
                    walletTransactionRepository.findByTransactionThirdPartyIDAndWalletTypeAndTransStatus(
                        txKey,
                        WalletTransactionType.PASSENGER_APPROVE_FEE,
                        TransactionStatus.SUCCESS
                    );

                if (approveTxOpt.isPresent()) {
                    // ✅ 1. Cộng lại vào ví Passenger
                    WalletTransaction userTx = new WalletTransaction();
                    userTx.setTransID(UUID.randomUUID());
                    userTx.setAmount(amount);
                    userTx.setDate(Instant.now());
                    userTx.setWalletType(WalletTransactionType.REFUND);
                    userTx.setTransStatus(TransactionStatus.SUCCESS);
                    userTx.setUserWallet(userWallet);

                    userWallet.addWalletTransactionAndUpdateBalance(userTx);
                    userWalletRepository.save(userWallet);

                    // ✅ 2. Trừ ví hệ thống
                    SystemWallet systemWallet = systemWalletRepository
                        .findTopByOrderByMobifyDateDesc()
                        .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy ví hệ thống", "wallet", "system-notfound"));

                    WalletTransaction sysTx = new WalletTransaction();
                    sysTx.setTransID(UUID.randomUUID());
                    sysTx.setAmount(amount);
                    sysTx.setDate(Instant.now());
                    sysTx.setWalletType(WalletTransactionType.SYSTEM_REFUND_TO_PASSENGER);
                    sysTx.setTransStatus(TransactionStatus.SUCCESS);

                    systemWallet.addWalletTransactionAndUpdateBalance(sysTx);
                    systemWalletRepository.save(systemWallet);
                }
            }
        }

        return request;
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

    public List<TripCardDTO> getAvailableTripsForPassenger() {
        List<TripStatus> statuses = List.of(TripStatus.UPCOMING, TripStatus.CONFIRMING);
        List<Trip> trips = tripRepository.findAvailableTrips(statuses);

        return trips
            .stream()
            .map(trip -> {
                TripCardDTO dto = new TripCardDTO();
                dto.setTripID(trip.getTripID());
                dto.setStartLocation(trip.getStartLocation());
                dto.setEndLocation(trip.getEndLocation());
                dto.setStartDate(trip.getStartDate());
                dto.setPricePerSeat(trip.getPricePerSeat());
                dto.setCurrentSeat(trip.getCurrentSeat());
                dto.setMaxSeat(trip.getMaxSeat());
                dto.setTripImgUrl(imageUrlService.buildTripImageUrl(dto.getTripID()));

                if (trip.getDriver() != null) {
                    dto.setDriverName(trip.getDriver().getUser().getFirstName()); // hoặc "Ẩn danh"
                }
                if (trip.getVehicle() != null) {
                    dto.setVehicleType(trip.getVehicle().getVehicleType());
                }

                return dto;
            })
            .collect(Collectors.toList());
    }

    public RequestTripCusDTO mapToRequestTripCusDTO(RequestTrip request) {
        RequestTripCusDTO dto = new RequestTripCusDTO();

        dto.setRequestTripID(request.getRequestTripID());
        dto.setAmountApproveFee(request.getAmountApproveFee());
        dto.setNumberofSeats(request.getNumberofSeats());
        dto.setLuggageDescription(request.getLuggageDescription());
        dto.setType(request.getType());
        dto.setStatus(request.getStatus());
        dto.setPickUpTime(request.getPickUpTime());
        dto.setEndTime(request.getEndTime());
        dto.setCheckIn(request.getCheckIn());
        dto.setCheckInTime(request.getCheckInTime());
        dto.setCheckOut(request.getCheckOut());
        dto.setCheckOutTIme(request.getCheckOutTIme());
        dto.setAppliedAt(request.getAppliedAt());

        // ✅ startLoca
        if (request.getStartLoca() != null) {
            tripStopLocationRepository
                .findByStopLocaID(UUID.fromString(request.getStartLoca()))
                .map(tripStopLocationSkipTripMapper::toDto)
                .ifPresent(dto::setStartLoca);
        }

        // ✅ endLoca
        if (request.getEndLoca() != null) {
            tripStopLocationRepository
                .findByStopLocaID((UUID.fromString(request.getEndLoca())))
                .map(tripStopLocationSkipTripMapper::toDto)
                .ifPresent(dto::setEndLoca);
        }

        // ✅ luggage image URL
        if (request.getLuggageImg() != null && request.getLuggageImg().length > 0) {
            dto.setLuggageImgUrl(imageUrlService.buildLuggageImageUrl(request.getRequestTripID()));
        }

        return dto;
    }

    @Transactional
    public void completeTrip(UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Trip not found", "trip", "notfound"));

        if (trip.getTripStatus() != TripStatus.ON_GOING) {
            throw new BadRequestAlertException("Trip not in ON_GOING status", "trip", "invalid-status");
        }

        Driver driver = trip.getDriver();
        if (driver == null || driver.getUser() == null) {
            throw new BadRequestAlertException("Driver info missing", "trip", "driver-null");
        }

        // ✅ Lấy current user và kiểm tra quyền
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("Current user not found", "user", "notfound"));

        if (!driver.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestAlertException("Bạn không phải là tài xế của chuyến đi này", "trip", "not-owner");
        }

        // ✅ Đánh dấu kết thúc
        trip.setTripStatus(TripStatus.DONE);
        trip.setEndDate(Instant.now());
        tripRepository.save(trip);

        User user = driver.getUser();
        UserWallet driverWallet = userWalletRepository
            .findByUser(user)
            .orElseThrow(() -> new BadRequestAlertException("Driver wallet not found", "wallet", "notfound"));

        SystemWallet systemWallet = systemWalletRepository.findTopByOrderByMobifyDateDesc().orElseThrow();

        List<RequestTrip> passengers = requestTripRepository.findByTripAndStatus(trip, PassengerStatus.DONE);

        double createFee = trip.getMaxSeat() * trip.getPricePerSeat() * 0.1;
        double totalEarnings = passengers.stream().mapToDouble(RequestTrip::getAmountApproveFee).sum();
        double gainFee = totalEarnings * 0.1;

        refundTripCreateFee(driverWallet, systemWallet, createFee, gainFee, trip);
        logSystemGainFee(systemWallet, gainFee, trip, driverWallet);
        transferPassengerMoneyToDriver(passengers, driverWallet, systemWallet, trip);
    }

    private void refundTripCreateFee(UserWallet driverWallet, SystemWallet systemWallet, double createFee, double gainFee, Trip trip) {
        double refund = createFee - gainFee;
        if (refund > 0) {
            WalletTransaction sysTx = new WalletTransaction();
            sysTx.setTransID(UUID.randomUUID());
            sysTx.setAmount(refund);
            sysTx.setDate(Instant.now());
            sysTx.setWalletType(WalletTransactionType.SYSTEM_REFUND_TO_DRIVER_DONE_TRIP);
            sysTx.setTransStatus(TransactionStatus.SUCCESS);
            sysTx.setTransactionThirdPartyID(trip.getTripID().toString());
            sysTx.setUserWallet(driverWallet);
            systemWallet.addWalletTransactionAndUpdateBalance(sysTx);
            systemWalletRepository.save(systemWallet);

            WalletTransaction driverTx = new WalletTransaction();
            driverTx.setTransID(UUID.randomUUID());
            driverTx.setAmount(refund);
            driverTx.setDate(Instant.now());
            driverTx.setWalletType(WalletTransactionType.DRIVER_DONE_TRIP_REFUND);
            driverTx.setTransStatus(TransactionStatus.SUCCESS);
            driverTx.setUserWallet(driverWallet);
            driverTx.setTransactionThirdPartyID(trip.getTripID().toString());
            driverWallet.addWalletTransactionAndUpdateBalance(driverTx);
            userWalletRepository.save(driverWallet);
        }
    }

    private void logSystemGainFee(SystemWallet systemWallet, double gainFee, Trip trip, UserWallet driverWallet) {
        WalletTransaction tx = new WalletTransaction();
        tx.setTransID(UUID.randomUUID());
        tx.setAmount(gainFee);
        tx.setDate(Instant.now());
        tx.setWalletType(WalletTransactionType.SYSTEM_GAIN_DONE_TRIP_FEE);
        tx.setTransStatus(TransactionStatus.SUCCESS);
        tx.setUserWallet(driverWallet);
        systemWallet.addWalletTransactionAndUpdateBalance(tx);
        systemWalletRepository.save(systemWallet);
    }

    private void transferPassengerMoneyToDriver(
        List<RequestTrip> passengers,
        UserWallet driverWallet,
        SystemWallet systemWallet,
        Trip trip
    ) {
        for (RequestTrip request : passengers) {
            double amount = request.getAmountApproveFee();
            request.setStatus(PassengerStatus.DONE);

            // Trừ từ System
            WalletTransaction sysTx = new WalletTransaction();
            sysTx.setTransID(UUID.randomUUID());
            sysTx.setAmount(amount);
            sysTx.setDate(Instant.now());
            sysTx.setWalletType(WalletTransactionType.SYSTEM_REFUND_TO_DRIVER_DONE_TRIP);
            sysTx.setTransStatus(TransactionStatus.SUCCESS);
            sysTx.setUserWallet(driverWallet);
            sysTx.setTransactionThirdPartyID(trip.getTripID().toString());
            systemWallet.addWalletTransactionAndUpdateBalance(sysTx);
            systemWalletRepository.save(systemWallet);

            // Cộng cho Driver
            WalletTransaction driverTx = new WalletTransaction();
            driverTx.setTransID(UUID.randomUUID());
            driverTx.setAmount(amount);
            driverTx.setDate(Instant.now());
            driverTx.setWalletType(WalletTransactionType.DRIVER_DONE_TRIP_REFUND);
            driverTx.setTransStatus(TransactionStatus.SUCCESS);
            driverTx.setUserWallet(driverWallet);
            driverTx.setTransactionThirdPartyID(trip.getTripID().toString());
            driverWallet.addWalletTransactionAndUpdateBalance(driverTx);
            requestTripRepository.save(request);
            userWalletRepository.save(driverWallet);
        }
    }

    @Transactional
    public void markTripAsDone(UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Trip not found", "trip", "notfound"));

        if (trip.getTripStatus() != TripStatus.ON_GOING) {
            throw new BadRequestAlertException("Trip not in ON_GOING status", "trip", "invalid-status");
        }

        // Xác minh tài xế là người gọi
        User currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new BadRequestAlertException("Current user not found", "user", "notfound"));

        if (!trip.getDriver().getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestAlertException("Bạn không phải tài xế của chuyến này", "trip", "not-owner");
        }

        trip.setTripStatus(TripStatus.DONE);
        trip.setEndDate(Instant.now());
        tripRepository.save(trip);
    }

    public void payoutToDriver(Trip trip) {
        Driver driver = trip.getDriver();
        User user = driver.getUser();

        LOG.debug("user:", user);
        UserWallet driverWallet = userWalletRepository
            .findByUser_Id(user.getId())
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy ví của tài xế", "wallet", "driver-notfound"));
        SystemWallet systemWallet = systemWalletRepository
            .findTopByOrderByMobifyDateDesc()
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy ví hệ thống", "wallet", "system-notfound"));

        List<RequestTrip> passengers = requestTripRepository.findByTripAndStatus(trip, PassengerStatus.DONE);

        double createFee = trip.getMaxSeat() * trip.getPricePerSeat() * 0.1;
        double totalEarnings = passengers.stream().mapToDouble(RequestTrip::getAmountApproveFee).sum();
        double gainFee = totalEarnings * 0.1;

        refundTripCreateFee(driverWallet, systemWallet, createFee, gainFee, trip);
        logSystemGainFee(systemWallet, gainFee, trip, driverWallet);
        transferPassengerMoneyToDriver(passengers, driverWallet, systemWallet, trip);
    }

    public TripDetailForDriverHistoryDTO getTripDetailForDriver(UUID tripId) {
        Trip trip = tripRepository.findFullTripByTripID(tripId).orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        TripDetailForDriverHistoryDTO dto = tripCusMapper.toTripDetailForDriverHistoryDTO(trip);

        // ✅ Ảnh trip
        dto.setTripImgUrl(imageUrlService.buildTripImageUrl(trip.getTripID()));

        // ✅ Driver
        userDetailRepository
            .findByUserId(trip.getDriver().getUser().getId())
            .ifPresent(detail -> {
                DriverRawDTO driverDTO = usermanageMapper.toRawDTO(trip.getDriver(), detail);
                driverDTO.setRating(getAverageRatingForDriver(driverDTO.getDriverId()));
                driverDTO.setDriverLicenseUrl(imageUrlService.buildDriverLicenseUrl(driverDTO.getDriverId()));
                driverDTO.setIdentityCardFaceUpUrl(imageUrlService.buildIdentityCardFaceUpUrl(driverDTO.getDriverId()));
                driverDTO.setIdentityCardFaceDownUrl(imageUrlService.buildIdentityCardFaceDownUrl(driverDTO.getDriverId()));
                driverDTO.setAvatarUrl(imageUrlService.buildUserAvatarUrl(detail.getAppUserDetail()));
                dto.setDriver(driverDTO);
            });

        // ✅ Vehicle
        VehicleRawDTO vehicleDTO = usermanageMapper.toRawDTO(trip.getVehicle());
        vehicleDTO.setVehicleImageUrl(imageUrlService.buildVehicleImageUrl(vehicleDTO.getVehicleID()));
        vehicleDTO.setCarregistrationUrl(imageUrlService.buildCarRegistrationUrl(vehicleDTO.getVehicleID()));
        vehicleDTO.setVehicleInspectionCertificateUrl(imageUrlService.buildInspectionCertificateUrl(vehicleDTO.getVehicleID()));
        vehicleDTO.setCarInsuranceUrl(imageUrlService.buildCarInsuranceUrl(vehicleDTO.getVehicleID()));
        dto.setVehicle(vehicleDTO);

        // ✅ Điểm dừng
        Set<TripStopLocationSkipTripDTO> stops = trip
            .getTripStopLocations()
            .stream()
            .map(tripStopLocationSkipTripMapper::toDto)
            .collect(Collectors.toSet());
        dto.setStoplocation(stops);

        // ✅ RequestTrip
        Set<RequestTripCusDTO> requests = trip
            .getRequestTrips()
            .stream()
            .map(request -> {
                RequestTripCusDTO rDto = new RequestTripCusDTO();
                rDto.setRequestTripID(request.getRequestTripID());
                rDto.setAmountApproveFee(request.getAmountApproveFee());
                rDto.setNumberofSeats(request.getNumberofSeats());
                rDto.setLuggageDescription(request.getLuggageDescription());
                rDto.setType(request.getType());
                rDto.setStatus(request.getStatus());
                rDto.setPickUpTime(request.getPickUpTime());
                rDto.setEndTime(request.getEndTime());
                rDto.setCheckIn(request.getCheckIn());
                rDto.setCheckInTime(request.getCheckInTime());
                rDto.setCheckOut(request.getCheckOut());
                rDto.setCheckOutTIme(request.getCheckOutTIme());
                rDto.setAppliedAt(request.getAppliedAt());

                // ảnh hành lý
                if (request.getLuggageImg() != null) {
                    rDto.setLuggageImgUrl(imageUrlService.buildLuggageImageUrl(request.getRequestTripID()));
                }

                // start & end stop
                if (request.getStartLoca() != null) {
                    tripStopLocationRepository
                        .findByStopLocaID(UUID.fromString(request.getStartLoca()))
                        .map(tripStopLocationSkipTripMapper::toDto)
                        .ifPresent(rDto::setStartLoca);
                }
                if (request.getEndLoca() != null) {
                    tripStopLocationRepository
                        .findByStopLocaID(UUID.fromString(request.getEndLoca()))
                        .map(tripStopLocationSkipTripMapper::toDto)
                        .ifPresent(rDto::setEndLoca);
                }

                return rDto;
            })
            .collect(Collectors.toSet());

        dto.setRequest(requests);

        return dto;
    }

    public Page<TripListDTO> getTripHistoryForDriver(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Không xác định được người dùng", "auth", "notfound"));

        List<TripStatus> historyStatuses = List.of(TripStatus.DONE, TripStatus.CANCEL);
        Page<Trip> trips = tripRepository.findByDriver_User_LoginAndTripStatusIn(login, historyStatuses, pageable);

        return trips.map(tripCusMapper::toTripListDTO);
    }

    public Page<TripListDTO> getActiveTripsForDriver(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Không xác định được tài xế", "auth", "notfound"));

        List<TripStatus> activeStatuses = List.of(TripStatus.UPCOMING, TripStatus.ON_GOING, TripStatus.CONFIRMING);

        Page<Trip> trips = tripRepository.findByDriver_User_LoginAndTripStatusIn(login, activeStatuses, pageable);

        return trips.map(usermanageMapper::toTripListDTO);
    }

    @Transactional
    public void startTrip(UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new BadRequestAlertException("Không tìm thấy chuyến đi", "trip", "notfound"));

        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Không xác định được tài xế", "auth", "notfound"));

        // ✅ Check tài xế có phải là chủ của trip không
        if (!trip.getDriver().getUser().getLogin().equals(login)) {
            throw new BadRequestAlertException("Không có quyền bắt đầu chuyến này", "trip", "forbidden");
        }

        if (trip.getTripStatus() != TripStatus.UPCOMING && trip.getTripStatus() != TripStatus.CONFIRMING) {
            throw new BadRequestAlertException("Chỉ có thể bắt đầu chuyến sắp tới", "trip", "invalid-status");
        }

        trip.setTripStatus(TripStatus.ON_GOING);
        tripRepository.save(trip);
    }

    public double getAverageRatingForDriver(UUID driverId) {
        Double avg = ratingRepository.findAverageRatingByDriverId(driverId);
        return avg != null ? avg : 0.0;
    }
}
