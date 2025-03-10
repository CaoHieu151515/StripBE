package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Driver;
import strip.domain.Trip;
import strip.domain.Vehicle;
import strip.service.dto.DriverDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.VehicleDTO;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMapper extends EntityMapper<TripDTO, Trip> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    TripDTO toDto(Trip s);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
