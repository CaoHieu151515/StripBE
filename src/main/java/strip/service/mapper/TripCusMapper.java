package strip.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.Trip;
import strip.domain.TripStopLocation;
import strip.domain.Vehicle;
import strip.service.ImageUrlService;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripStopLocationDTO;

@Mapper(componentModel = "spring", uses = { ImageUrlService.class })
public interface TripCusMapper {
    @Mapping(target = "tripID", source = "trip.tripID")
    @Mapping(target = "startLocation", source = "trip.startLocation")
    @Mapping(target = "endLocation", source = "trip.endLocation")
    @Mapping(target = "description", source = "trip.description")
    @Mapping(target = "condition", source = "trip.condition")
    @Mapping(target = "startDate", source = "trip.startDate")
    @Mapping(target = "endDate", source = "trip.endDate")
    @Mapping(target = "pricePerSeat", source = "trip.pricePerSeat")
    @Mapping(target = "maxSeat", source = "trip.maxSeat")
    @Mapping(target = "currentSeat", source = "trip.currentSeat")
    @Mapping(target = "tripStatus", source = "trip.tripStatus")
    @Mapping(target = "cancelReason", source = "trip.cancelReason")
    @Mapping(target = "totalTime", source = "trip.totalTime")
    @Mapping(target = "totalDistance", source = "trip.totalDistance")
    @Mapping(target = "vehicleID", source = "vehicle.vehicleID")
    @Mapping(target = "vehicleType", source = "vehicle.vehicleType")
    @Mapping(target = "vehicleNumber", source = "vehicle.vehicleNumber")
    @Mapping(target = "numberOfSeats", source = "vehicle.numberOfSeats")
    @Mapping(target = "vehicleColor", source = "vehicle.vehicleColor")
    @Mapping(target = "vehicleBrand", source = "vehicle.vehicleBrand")
    @Mapping(target = "driverName", ignore = true)
    @Mapping(target = "driverPhone", ignore = true)
    @Mapping(target = "tripImgUrl", ignore = true)
    @Mapping(target = "vehicleImageUrl", ignore = true)
    @Mapping(target = "stopLocations", ignore = true)
    TripCusDTO toDto(Trip trip, Driver driver, Vehicle vehicle);

    List<TripStopLocationDTO> toDto(List<TripStopLocation> entities);
}
