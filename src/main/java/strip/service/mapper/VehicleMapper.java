package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Driver;
import strip.domain.Vehicle;
import strip.service.dto.DriverDTO;
import strip.service.dto.VehicleDTO;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    VehicleDTO toDto(Vehicle s);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
