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
import strip.service.dto.TripDetailForDriverHistoryDTO;
import strip.service.dto.TripListDTO;
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

    @Mapping(source = "tripID", target = "stripID")
    @Mapping(source = "startDate", target = "startDay")
    @Mapping(source = "endDate", target = "endDay")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endlocation")
    @Mapping(source = "pricePerSeat", target = "price")
    @Mapping(source = "tripStatus", target = "status")
    @Mapping(source = "totalTime", target = "totalTime")
    TripListDTO toTripListDTO(Trip trip);

    @Mapping(source = "tripID", target = "tripID")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endLocation")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "condition", target = "condition")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "pricePerSeat", target = "pricePerSeat")
    @Mapping(source = "maxSeat", target = "maxSeat")
    @Mapping(source = "currentSeat", target = "currentSeat")
    @Mapping(source = "tripStatus", target = "tripStatus")
    @Mapping(source = "cancelReason", target = "cancelReason")
    @Mapping(source = "totalTime", target = "totalTime")
    @Mapping(source = "totalDistance", target = "totalDistance")
    @Mapping(target = "tripImgUrl", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "stoplocation", ignore = true)
    @Mapping(target = "request", ignore = true)
    TripDetailForDriverHistoryDTO toTripDetailForDriverHistoryDTO(Trip trip);
}
