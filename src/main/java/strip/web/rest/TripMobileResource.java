package strip.web.rest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.domain.RequestTrip;
import strip.domain.Trip;
import strip.service.TripCustomService;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.FeedbackCreateDTO;
import strip.service.dto.FeedbackDTO;
import strip.service.dto.RequestTripCusDTO;
import strip.service.dto.TripCardDTO;
import strip.service.dto.TripCreateDTO;
import strip.service.dto.TripDetailDTO;
import strip.service.dto.TripDetailForDriverHistoryDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.TripStopLocationUpdateDTO;
import strip.service.dto.TripUpdateDTO;
import tech.jhipster.web.util.PaginationUtil;

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
    public ResponseEntity<TripDetailDTO> getFullTrip(@PathVariable UUID tripId) {
        TripDetailDTO trip = tripCustomService.getFullTrip(tripId);
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

    @PutMapping("/trips/{tripId}/complete")
    public ResponseEntity<Void> completeTrip(@PathVariable UUID tripId) {
        tripCustomService.markTripAsDone(tripId); // chỉ đánh dấu DONE
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trips/history")
    public ResponseEntity<List<TripListDTO>> getDriverTripHistory(@ParameterObject Pageable pageable) {
        Page<TripListDTO> page = tripCustomService.getTripHistoryForDriver(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{tripId}/view-detail")
    public ResponseEntity<TripDetailForDriverHistoryDTO> getDriverTripDetail(@PathVariable UUID tripId) {
        TripDetailForDriverHistoryDTO dto = tripCustomService.getTripDetailForDriver(tripId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getall/active")
    public ResponseEntity<List<TripListDTO>> getActiveTrips(@ParameterObject Pageable pageable) {
        Page<TripListDTO> page = tripCustomService.getActiveTripsForDriver(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/{tripId}/start")
    public ResponseEntity<Void> startTrip(@PathVariable UUID tripId) {
        tripCustomService.startTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trips/{tripId}/feedback")
    @PreAuthorize("hasAuthority('ROLE_PASSENGER')")
    public ResponseEntity<FeedbackDTO> giveFeedbackForDriver(@PathVariable UUID tripId, @RequestBody FeedbackCreateDTO dto) {
        FeedbackDTO result = tripCustomService.createPassengerFeedbackForDriver(tripId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/drivers/{driverId}/info")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<DriverInfoDTO> getDriverInfoWithRatings(@PathVariable UUID driverId) {
        DriverInfoDTO result = tripCustomService.getDriverInfoWithRatings(driverId);
        return ResponseEntity.ok(result);
    }
}
