package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Driver;
import strip.domain.Trip;
import strip.service.dto.DriverDTO;
import strip.service.dto.TripDTO;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMapper extends EntityMapper<TripDTO, Trip> {
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    TripDTO toDto(Trip s);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
