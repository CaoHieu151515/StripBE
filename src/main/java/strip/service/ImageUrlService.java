package strip.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ImageUrlService {

    // 🚗 Trip Cover Image
    public String buildTripImageUrl(UUID tripId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/trips/")
            .path(tripId.toString())
            .path("/cover")
            .toUriString();
    }

    // 🚕 Vehicle Image
    public String buildVehicleImageUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/images/vehicle/").path(vehicleId.toString()).toUriString();
    }

    public String buildCarRegistrationUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/carregistration/")
            .path(vehicleId.toString())
            .toUriString();
    }

    public String buildInspectionCertificateUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/inspection/")
            .path(vehicleId.toString())
            .toUriString();
    }

    public String buildCarInsuranceUrl(UUID vehicleId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/vehicle/insurance/")
            .path(vehicleId.toString())
            .toUriString();
    }

    // 👤 Driver Info Image
    public String buildDriverLicenseUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/license/")
            .path(driverId.toString())
            .toUriString();
    }

    public String buildIdentityCardFaceUpUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/identity-card-up/")
            .path(driverId.toString())
            .toUriString();
    }

    public String buildIdentityCardFaceDownUrl(UUID driverId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/driver/identity-card-down/")
            .path(driverId.toString())
            .toUriString();
    }

    // 📦 Luggage (RequestTrip)
    public String buildLuggageImageUrl(UUID requestTripId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/request-trip/luggage/")
            .path(requestTripId.toString())
            .toUriString();
    }

    // 🧑 User Avatar
    public String buildUserAvatarUrl(UUID userDetailId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/images/user/avatar/")
            .path(userDetailId.toString())
            .toUriString();
    }
}
