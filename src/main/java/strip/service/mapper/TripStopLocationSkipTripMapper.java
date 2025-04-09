package strip.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import strip.domain.TripStopLocation;
import strip.service.dto.TripStopLocationSkipTripDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripStopLocationSkipTripMapper {
    @Mapping(source = "stopLocaID", target = "stopLocaID")
    @Mapping(source = "stopLoca", target = "stopLoca")
    @Mapping(source = "stoplocaPosition", target = "tripPositon")
    @Mapping(source = "stopLocaTime", target = "stopLocaTime")
    @Mapping(source = "estimatedTime", target = "estimatedTime")
    @Mapping(source = "estimatedKM", target = "estimatedKM")
    @Mapping(source = "stopLocaStatus", target = "stopLocaStatus")
    TripStopLocationSkipTripDTO toDto(TripStopLocation entity);
}
