package strip.web.rest;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import strip.domain.Driver;
import strip.domain.RequestTrip;
import strip.domain.Trip;
import strip.domain.UserDetail;
import strip.domain.Vehicle;
import strip.repository.DriverRepository;
import strip.repository.RequestTripRepository;
import strip.repository.TripRepository;
import strip.repository.UserDetailRepository;
import strip.repository.VehicleRepository;

@RestController
@RequestMapping("/api/images")
public class ImageResource {

    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final RequestTripRepository requestTripRepository;
    private final UserDetailRepository userDetailRepository;

    public ImageResource(
        TripRepository tripRepository,
        DriverRepository driverRepository,
        VehicleRepository vehicleRepository,
        RequestTripRepository requestTripRepository,
        UserDetailRepository userDetailRepository
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.requestTripRepository = requestTripRepository;
        this.userDetailRepository = userDetailRepository;
    }

    // ==== DRIVER IMAGES ====

    @GetMapping("/driver/identity-card-up/{driverId}")
    public ResponseEntity<byte[]> getIdentityCardFaceUp(@PathVariable UUID driverId) {
        Driver driver = driverRepository
            .findByDriverID(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(driver.getIdentityCardFaceUpContentType()))
            .body(driver.getIdentityCardFaceUp());
    }

    @GetMapping("/driver/identity-card-down/{driverId}")
    public ResponseEntity<byte[]> getIdentityCardFaceDown(@PathVariable UUID driverId) {
        Driver driver = driverRepository
            .findByDriverID(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(driver.getIdentityCardFacedownContentType()))
            .body(driver.getIdentityCardFacedown());
    }

    @GetMapping("/driver/license/{driverId}")
    public ResponseEntity<byte[]> getDriverLicense(@PathVariable UUID driverId) {
        Driver driver = driverRepository
            .findByDriverID(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(driver.getDriverLicenseContentType()))
            .body(driver.getDriverLicense());
    }

    // ==== VEHICLE IMAGES ====

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<byte[]> getVehicleImage(@PathVariable UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(vehicle.getVehicleImageContentType()))
            .body(vehicle.getVehicleImage());
    }

    @GetMapping("/vehicle/carregistration/{vehicleId}")
    public ResponseEntity<byte[]> getCarRegistration(@PathVariable UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(vehicle.getCarregistrationContentType()))
            .body(vehicle.getCarregistration());
    }

    @GetMapping("/vehicle/inspection/{vehicleId}")
    public ResponseEntity<byte[]> getInspectionCertificate(@PathVariable UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(vehicle.getVehicleInspectionCertificateContentType()))
            .body(vehicle.getVehicleInspectionCertificate());
    }

    @GetMapping("/vehicle/insurance/{vehicleId}")
    public ResponseEntity<byte[]> getCarInsurance(@PathVariable UUID vehicleId) {
        Vehicle vehicle = vehicleRepository
            .findByVehicleID(vehicleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(vehicle.getCarInsuranceContentType()))
            .body(vehicle.getCarInsurance());
    }

    @GetMapping("/request-trip/luggage/{requestTripId}")
    public ResponseEntity<byte[]> getLuggageImage(@PathVariable UUID requestTripId) {
        RequestTrip request = requestTripRepository
            .findByRequestTripID(requestTripId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RequestTrip not found"));

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(request.getLuggageImgContentType())).body(request.getLuggageImg());
    }

    //Trip image
    @GetMapping("/trips/{tripId}/cover")
    public ResponseEntity<byte[]> getTripImage(@PathVariable UUID tripId) {
        Trip trip = tripRepository
            .findByTripID(tripId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

        if (trip.getTripImg() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip image not available");
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(trip.getTripImgContentType())).body(trip.getTripImg());
    }

    @GetMapping("/user/avatar/{userDetailId}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable UUID userDetailId) {
        UserDetail userDetail = userDetailRepository
            .findByAppUserDetail(userDetailId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDetail not found"));

        if (userDetail.getUserimage() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User image not available");
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(userDetail.getUserimageContentType()))
            .body(userDetail.getUserimage());
    }
}
