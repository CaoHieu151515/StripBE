package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Driver;
import strip.domain.Feedback;
import strip.domain.Trip;
import strip.domain.User;
import strip.service.dto.DriverDTO;
import strip.service.dto.FeedbackDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FeedbackDTO toDto(Feedback s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
