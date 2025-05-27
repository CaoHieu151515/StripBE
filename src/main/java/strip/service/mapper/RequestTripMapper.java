package strip.service.mapper;

import org.mapstruct.*;
import strip.config.IgnoreUnmappedConfig;
import strip.domain.RequestTrip;
import strip.domain.Trip;
import strip.domain.User;
import strip.service.dto.RequestTripDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link RequestTrip} and its DTO {@link RequestTripDTO}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface RequestTripMapper extends EntityMapper<RequestTripDTO, RequestTrip> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    RequestTripDTO toDto(RequestTrip s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
