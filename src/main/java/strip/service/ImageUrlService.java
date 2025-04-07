package strip.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.domain.*;
import strip.repository.*;

@Service
public class ImageUrlService {

    private final UserDetailRepository userDetailRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final RequestTripRepository requestTripRepository;

    public ImageUrlService(
        UserDetailRepository userDetailRepository,
        VehicleRepository vehicleRepository,
        DriverRepository driverRepository,
        TripRepository tripRepository,
        RequestTripRepository requestTripRepository
    ) {
        this.userDetailRepository = userDetailRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.tripRepository = tripRepository;
        this.requestTripRepository = requestTripRepository;
    }

    private String buildUrl(String pathPrefix, UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(pathPrefix).path(id.toString()).toUriString();
    }

    // 🚗 Trip Cover Image
    public String buildTripImageUrl(UUID tripId) {
        Optional<Trip> optional = tripRepository.findByTripID(tripId);
        if (optional.isEmpty() || optional.get().getTripImg() == null) return null;
        return buildUrl("/api/images/trips/", tripId) + "/cover";
    }

    // 🚖 Vehicle Image
    public String buildVehicleImageUrl(UUID vehicleId) {
        Optional<Vehicle> optional = vehicleRepository.findByVehicleID(vehicleId);
        if (optional.isEmpty() || optional.get().getVehicleImage() == null) return null;
        return buildUrl("/api/images/vehicle/", vehicleId);
    }

    public String buildCarRegistrationUrl(UUID vehicleId) {
        Optional<Vehicle> optional = vehicleRepository.findByVehicleID(vehicleId);
        if (optional.isEmpty() || optional.get().getCarregistration() == null) return null;
        return buildUrl("/api/images/vehicle/carregistration/", vehicleId);
    }

    public String buildInspectionCertificateUrl(UUID vehicleId) {
        Optional<Vehicle> optional = vehicleRepository.findByVehicleID(vehicleId);
        if (optional.isEmpty() || optional.get().getVehicleInspectionCertificate() == null) return null;
        return buildUrl("/api/images/vehicle/inspection/", vehicleId);
    }

    public String buildCarInsuranceUrl(UUID vehicleId) {
        Optional<Vehicle> optional = vehicleRepository.findByVehicleID(vehicleId);
        if (optional.isEmpty() || optional.get().getCarInsurance() == null) return null;
        return buildUrl("/api/images/vehicle/insurance/", vehicleId);
    }

    // 👤 Driver Info Image
    public String buildDriverLicenseUrl(UUID driverId) {
        Optional<Driver> optional = driverRepository.findByDriverID(driverId);
        if (optional.isEmpty() || optional.get().getDriverLicense() == null) return null;
        return buildUrl("/api/images/driver/license/", driverId);
    }

    public String buildIdentityCardFaceUpUrl(UUID driverId) {
        Optional<Driver> optional = driverRepository.findByDriverID(driverId);
        if (optional.isEmpty() || optional.get().getIdentityCardFaceUp() == null) return null;
        return buildUrl("/api/images/driver/identity-card-up/", driverId);
    }

    public String buildIdentityCardFaceDownUrl(UUID driverId) {
        Optional<Driver> optional = driverRepository.findByDriverID(driverId);
        if (optional.isEmpty() || optional.get().getIdentityCardFacedown() == null) return null;
        return buildUrl("/api/images/driver/identity-card-down/", driverId);
    }

    // 📦 Luggage (RequestTrip)
    public String buildLuggageImageUrl(UUID requestTripId) {
        Optional<RequestTrip> optional = requestTripRepository.findByRequestTripID(requestTripId);
        if (optional.isEmpty() || optional.get().getLuggageImg() == null) return null;
        return buildUrl("/api/images/request-trip/luggage/", requestTripId);
    }

    // 🧑 User Avatar
    public String buildUserAvatarUrl(UUID userDetailId) {
        Optional<UserDetail> optional = userDetailRepository.findByAppUserDetail(userDetailId);
        if (optional.isEmpty() || optional.get().getUserimage() == null || optional.get().getUserimage().length == 0) {
            return null;
        }
        return buildUrl("/api/images/user/avatar/", userDetailId);
    }
}
