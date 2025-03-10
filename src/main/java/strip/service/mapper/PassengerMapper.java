package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Passenger;
import strip.domain.Trip;
import strip.domain.User;
import strip.service.dto.PassengerDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Passenger} and its DTO {@link PassengerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PassengerMapper extends EntityMapper<PassengerDTO, Passenger> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    PassengerDTO toDto(Passenger s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
