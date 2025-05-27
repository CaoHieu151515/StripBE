package strip.service.mapper;

import org.mapstruct.*;
import strip.config.IgnoreUnmappedConfig;
import strip.domain.Driver;
import strip.domain.Report;
import strip.domain.Trip;
import strip.domain.User;
import strip.service.dto.DriverDTO;
import strip.service.dto.ReportDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ReportDTO toDto(Report s);

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
