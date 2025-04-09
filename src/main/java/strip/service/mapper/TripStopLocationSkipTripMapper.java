package strip.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import strip.domain.TripStopLocation;
import strip.service.dto.TripStopLocationSkipTripDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripStopLocationSkipTripMapper {
    TripStopLocationSkipTripDTO toDto(TripStopLocation entity);
}
