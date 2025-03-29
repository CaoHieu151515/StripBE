package strip.web.rest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import strip.domain.RequestTrip;
import strip.domain.Trip;
import strip.service.TripCustomService;
import strip.service.dto.RequestTripCusDTO;
import strip.service.dto.TripCardDTO;
import strip.service.dto.TripCreateDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripStopLocationUpdateDTO;
import strip.service.dto.TripUpdateDTO;

@RestController
@RequestMapping("/api/mobile/trips")
public class TripMobileResource {

    private final TripCustomService tripCustomService;

    public TripMobileResource(TripCustomService tripCustomService) {
        this.tripCustomService = tripCustomService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<Trip> createTrip(@RequestBody TripCreateDTO dto) {
        UUID driverId = dto.getDriverId();
        Trip createdTrip = tripCustomService.createTripWithFee(dto, driverId);
        return ResponseEntity.ok(createdTrip);
    }

    @GetMapping("/{tripId}/requests/getall")
    public ResponseEntity<List<RequestTripCusDTO>> getRequestsForTrip(@PathVariable UUID tripId) {
        List<RequestTripCusDTO> dtos = tripCustomService.getRequestTripCusDTOsByTripId(tripId);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{tripId}/locations/update")
    public ResponseEntity<Void> updateTripStopLocations(
        @PathVariable UUID tripId,
        @RequestBody Set<TripStopLocationUpdateDTO> stopLocations
    ) {
        tripCustomService.updateTripStopLocations(tripId, stopLocations);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tripId}/trip/update")
    public ResponseEntity<Trip> updateTripInfo(@PathVariable UUID tripId, @RequestBody TripUpdateDTO dto) {
        Trip updatedTrip = tripCustomService.updateTripInfo(tripId, dto);
        return ResponseEntity.ok(updatedTrip);
    }

    @GetMapping("/{tripId}/trip/fulldetails")
    public ResponseEntity<TripCusDTO> getFullTrip(@PathVariable UUID tripId) {
        TripCusDTO trip = tripCustomService.getFullTrip(tripId);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}/resend-trip")
    public ResponseEntity<Trip> resendAndUpdateTrip(@PathVariable UUID tripId, @RequestBody TripUpdateDTO dto) {
        Trip updatedTrip = tripCustomService.resendAndUpdateTrip(tripId, dto);
        return ResponseEntity.ok(updatedTrip);
    }

    @PutMapping("/request-trips/{requestTripId}/accept")
    public ResponseEntity<RequestTrip> acceptRequestTrip(@PathVariable UUID requestTripId) {
        RequestTrip result = tripCustomService.acceptRequestTrip(requestTripId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/request-trips/{requestTripId}/reject")
    public ResponseEntity<RequestTrip> rejectRequestTrip(@PathVariable UUID requestTripId) {
        RequestTrip result = tripCustomService.rejectRequestTrip(requestTripId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/request-trips/{requestTripId}/check-in")
    public ResponseEntity<RequestTrip> checkIn(@PathVariable UUID requestTripId) {
        return ResponseEntity.ok(tripCustomService.checkIn(requestTripId));
    }

    @PutMapping("/request-trips/{requestTripId}/check-out")
    public ResponseEntity<RequestTrip> checkOut(@PathVariable UUID requestTripId) {
        return ResponseEntity.ok(tripCustomService.checkOut(requestTripId));
    }

    @GetMapping("/getAll/card")
    public ResponseEntity<List<TripCardDTO>> getAvailableTripsForPassenger() {
        List<TripCardDTO> tripCards = tripCustomService.getAvailableTripsForPassenger();
        return ResponseEntity.ok(tripCards);
    }
}
