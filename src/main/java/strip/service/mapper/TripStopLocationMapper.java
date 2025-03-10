package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Trip;
import strip.domain.TripStopLocation;
import strip.service.dto.TripDTO;
import strip.service.dto.TripStopLocationDTO;

/**
 * Mapper for the entity {@link TripStopLocation} and its DTO {@link TripStopLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripStopLocationMapper extends EntityMapper<TripStopLocationDTO, TripStopLocation> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    TripStopLocationDTO toDto(TripStopLocation s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);
}
